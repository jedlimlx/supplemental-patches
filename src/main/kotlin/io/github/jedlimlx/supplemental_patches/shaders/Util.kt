package io.github.jedlimlx.supplemental_patches.shaders

import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.neoforged.fml.ModList
import kotlin.math.floor
import kotlin.math.log

const val BLOCK_PROPERTIES = "/shaders/block.properties"
const val ENTITY_PROPERTIES = "/shaders/entity.properties"
const val ITEM_PROPERTIES = "/shaders/item.properties"

class ShaderBuilder(
    val name: String,
    glsl: String,
    val blockSize: Int = 4
) {
    private val _glsl: String = glsl
    val glsl: String
        get() {
            val lst = Regex("deferredMaterial\\(\"(.*?)\"\\)").findAll(_glsl).toList().map {
                Pair(
                    it.groupValues[1],
                    ShaderResourceLoader.DEFERRED_MAP[it.groupValues[1]]?.index ?: throw IllegalArgumentException("No deferred material ${it.groupValues[1]} found.")
                )
            }

            var output = (if (needsVoxelisation) "const uint voxelNumbers[$blockSize] = uint[](${voxelNumber.joinToString(", ") { "${it}u" }});\nuint voxelNumber = voxelNumbers[mat % 4];\n" else "") + _glsl
            lst.forEach { output = output.replace("deferredMaterial(\"${it.first}\")", it.second.toString()) }
            return output
        }

    val mat: Array<MutableList<String>> = Array(blockSize) { mutableListOf() }

    var needsVoxelisation: Boolean = false
    val voxelNumber: IntArray = IntArray(blockSize) { -1 }

    var lightColour: List<Colour?> = listOf()
    var lightLevel: Int = 0
    var heldLighting: Boolean = false
    var translucent: Boolean = false
    var wavingObject: WavingObject? = null
    var colourConditions: List<String> = listOf()

    var reflectionHandlers: List<String?> = listOf()

    fun allIds() = mat.toList().flatten()

    fun needsVoxelisation(): ShaderBuilder {
        this.needsVoxelisation = true
        return this
    }

    fun lightColour(colour: Colour, conditions: List<String> = listOf()): ShaderBuilder {
        lightColour = listOf(colour)
        colourConditions = conditions
        return this
    }

    fun lightColour(colour: List<Colour?>, conditions: List<String> = listOf()): ShaderBuilder {
        lightColour = colour
        colourConditions = conditions
        return this
    }

    fun lightLevel(level: Int): ShaderBuilder {
        this.lightLevel = level
        return this
    }

    fun heldLighting(): ShaderBuilder {
        heldLighting = true
        return this
    }

    fun translucent(): ShaderBuilder {
        translucent = true
        return this
    }

    fun wavingObject(code: WavingObject): ShaderBuilder {
        wavingObject = code
        return this
    }

    fun reflectionHandler(handler: String?): ShaderBuilder {
        reflectionHandlers = List(blockSize) { handler }
        return this
    }

    fun reflectionHandlers(lst: List<String?>): ShaderBuilder {
        reflectionHandlers = lst
        return this
    }

    fun required(): Boolean = mat.any {
        it.any {
            val tokens = it.split(":")
            if (tokens.size == 1) true
            else ModList.get().isLoaded(tokens[0])
        }
    }

    fun register(lst: MutableList<ShaderBuilder>) = lst.add(this)
}

fun generateCode(
    variable: String, size: Int, initialId: Int,
    suffix: String = "", shaderProvider: (Int, Int) -> String? = { size, id -> "$id" }
): String = StringBuilder().apply {
    val blockSize = 1 shl floor(log(size.toDouble() - 0.1, 2.0)).toInt()
    val output = shaderProvider(size, initialId)
    if (output != null) {
        append("$output\n")
        return@apply
    }

    append("if ($variable < ${blockSize + initialId}$suffix) {\n")
    append(
        generateCode(variable, blockSize, initialId, suffix, shaderProvider).split("\n")
            .joinToString("\n") { if (it.isNotEmpty()) "    $it" else it })
    append("} else /*if ($variable < ${size + initialId}$suffix)*/ {\n")
    append(
        generateCode(variable, size - blockSize, blockSize + initialId, suffix, shaderProvider).split("\n")
            .joinToString("\n") { if (it.isNotEmpty()) "    $it" else it })
    append("}\n")
}.toString()

fun generateCode(
    variable: String, size: Int, initialId: Int,
    smallestBlock: Int, suffix: String = "", shaderProvider: (Int) -> String = { "$it" }
): String = generateCode(variable, size, initialId, suffix) { size, it ->
    if (size > smallestBlock) null else shaderProvider(it)
}

// computing pivots
fun computePivot(lst: List<Int>): Int = lst[lst.size / 2 - 1]

fun computeAllPivots(lst: List<Int>, depth: Int, variable: String, blockSize: Int, f: (Int, Int) -> String): String {
    val builder = StringBuilder()
    _computeAllPivots(lst, depth, variable, builder, blockSize, f)

    return builder.toString()
}

fun _computeAllPivots(lst: List<Int>, depth: Int, variable: String, builder: StringBuilder, blockSize: Int, f: (Int, Int) -> String) {
    with (builder) {
        if (lst.isEmpty()) return
        if (lst.size == 1) {
            append(f(lst[0], depth))
            return
        }

        val pivot = computePivot(lst)
        append("    ".repeat(depth) + "if ($variable < ${pivot + blockSize}) {\n")
        _computeAllPivots(lst.subList(0, lst.size / 2), depth + 1, variable, builder, blockSize, f)
        append("    ".repeat(depth) + "} else { // $variable >= ${pivot + blockSize}\n")
        _computeAllPivots(lst.subList(lst.size / 2, lst.size), depth + 1, variable, builder, blockSize, f)
        append("    ".repeat(depth) + "}\n")
    }
}

// removing id
fun removeId(id: String, string: String): String {
    val id = id.replace("minecraft:", "")
    val newString = Regex("(?<=\\s)(minecraft:)?$id( |\\r\\n|\\n|\\r|$)").replace(string, "")
    return Regex("\\\\( |\\r\\n|\\n|\\r)\\\\( |\\r\\n|\\n|\\r)( |\\r\\n|\\n|\\r|$)").replace(newString, "")
}

fun List<String>.conditions() = this.joinToString(" && ") {
    if (it.matches(Regex("^([A-Za-z0-9]|_)*$"))) "defined $it" else "($it)"
}

// rectangles
class Rectangle(var x1: Int, var y1: Int, var x2: Int, var y2: Int, val glsl: String) {
    fun canMergeX(rectangle: Rectangle): Boolean =
        x1 == rectangle.x1 && x2 == rectangle.x2 && (y1 == rectangle.y2 + 1 || y2 + 1 == rectangle.y1)

    fun canMergeY(rectangle: Rectangle): Boolean =
        y1 == rectangle.y1 && y2 == rectangle.y2 && (x1 == rectangle.x2 + 1 || x2 + 1 == rectangle.x1)

    fun mergeX(rectangle: Rectangle) {
        y1 = minOf(y1, rectangle.y1)
        y2 = maxOf(y2, rectangle.y2)
    }

    fun mergeY(rectangle: Rectangle) {
        x1 = minOf(x1, rectangle.x1)
        x2 = maxOf(x2, rectangle.x2)
    }

    fun copy(): Rectangle = Rectangle(x1, y1, x2, y2, glsl)

    override fun hashCode() = glsl.hashCode()
}

fun List<Rectangle>.splitX(threshold: Int) = Pair(
    this.filter { it.x1 <= threshold }.map { it.copy() }.onEach { it.x2 = minOf(it.x2, threshold) },
    this.filter { it.x2 > threshold }.map { it.copy() }.onEach { it.x1 = maxOf(it.x1, threshold + 1) }
)

fun List<Rectangle>.splitY(threshold: Int) = Pair(
    this.filter { it.y1 <= threshold }.map { it.copy() }.onEach { it.y2 = minOf(it.y2, threshold) },
    this.filter { it.y2 > threshold }.map { it.copy() }.onEach { it.y1 = maxOf(it.y1, threshold + 1) }
)

fun split(rectangles: List<Rectangle>, depth: Int = 0, splitX: Boolean = true): String {
    val indent = "    ".repeat(depth + 2)
    if (rectangles.isEmpty()) return ""
    if (rectangles.size == 1) {
        val it = rectangles[0]
        return StringBuilder().apply {
            append("${indent}if (texCoordScaled.x >= ${it.x1} && texCoordScaled.x < ${it.x2 + 1} " +
                    "&& texCoordScaled.y >= ${it.y1} && texCoordScaled.y < ${it.y2 + 1}) {\n")
            it.glsl.split("\n").forEach { append("$indent    $it\n") }
            append("${indent}}\n")
        }.toString()
    }

    val lst = rectangles.map { if (splitX) it.x1 else it.y1 }.toSet().sorted()
    val threshold = lst[lst.size / 2] - 1
    val (first, second) = if (splitX) rectangles.splitX(threshold) else rectangles.splitY(threshold)
    if (first.isEmpty() || second.isEmpty()) return split(rectangles, depth, !splitX)

    return StringBuilder().apply {
        append("${indent}if (texCoordScaled.${if (splitX) "x" else "y"} < ${threshold + 1}) {\n")
        append(split(first, depth + 1, !splitX))
        append("${indent}} else {\n")
        append(split(second, depth + 1, !splitX))
        append("${indent}}\n")
    }.toString()
}

// error output
fun <T>catchAndPrintError(f: () -> T): T {
    try {
        return f()
    } catch (e: Exception) {
        val stackTraceString = e.stackTraceToString()  // inform user about the exception
        Minecraft.getInstance().player?.sendSystemMessage(Component.nullToEmpty(stackTraceString))

        throw e  // throw the same exception anyway
    }
}
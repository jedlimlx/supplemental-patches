package io.github.jedlimlx.supplemental_patches.shaders

const val BLOCK_PROPERTIES = "/shaders/block.properties"
const val ENTITY_PROPERTIES = "/shaders/entity.properties"
const val ITEM_PROPERTIES = "/shaders/item.properties"

class ShaderBuilder(
    val name: String,
    glsl: String,
    mat0: List<String> = listOf(),
    mat1: List<String> = listOf(),
    mat2: List<String> = listOf(),
    mat3: List<String> = listOf()
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

            var output = (if (needsVoxelisation) "const uint voxelNumbers[4] = uint[](${voxelNumber.joinToString(", ") { "${it}u" }});\nuint voxelNumber = voxelNumbers[mat % 4];\n" else "") + _glsl
            lst.forEach { output = output.replace("deferredMaterial(\"${it.first}\")", it.second.toString()) }
            return output
        }

    val mat0 = mat0.toMutableList()
    val mat1 = mat1.toMutableList()
    val mat2 = mat2.toMutableList()
    val mat3 = mat3.toMutableList()

    var needsVoxelisation: Boolean = false
    val voxelNumber: IntArray = IntArray(4) { -1 }

    var lightColour: List<Colour?> = listOf()
    var lightLevel: Int = 0
    var heldLighting: Boolean = false
    var translucent: Boolean = false
    var wavingObject: WavingObject? = null
    var colourConditions: List<String> = listOf()

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

    fun register(lst: MutableList<ShaderBuilder>) = lst.add(this)
}

fun generateCode(
    variable: String, blockSize: Int, initialId: Int,
    smallestBlock: Int = 4, suffix: String = "", shaderProvider: (Int) -> String = { "$it" }
): String = StringBuilder().apply {
    if (blockSize == smallestBlock) {
        append("${shaderProvider(initialId)}\n")
        return@apply
    }

    append("if ($variable < ${blockSize / 2 + initialId}$suffix) {\n")
    append(
        generateCode(variable, blockSize / 2, initialId, smallestBlock, suffix, shaderProvider).split("\n")
            .joinToString("\n") { if (it.isNotEmpty()) "    $it" else it })
    append("} else /*if ($variable < ${blockSize + initialId}$suffix)*/ {\n")
    append(
        generateCode(variable, blockSize / 2, initialId + blockSize / 2, smallestBlock, suffix, shaderProvider).split("\n")
            .joinToString("\n") { if (it.isNotEmpty()) "    $it" else it })
    append("}\n")
}.toString()

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
        append("    ".repeat(depth) + "if ($variable >= ${pivot + blockSize}) {\n")
        _computeAllPivots(lst.subList(lst.size / 2, lst.size), depth + 1, variable, builder, blockSize, f)
        append("    ".repeat(depth) + "} else { // $variable < ${pivot + blockSize}\n")
        _computeAllPivots(lst.subList(0, lst.size / 2), depth + 1, variable, builder, blockSize, f)
        append("    ".repeat(depth) + "}\n")
    }
}

// removing id
fun removeId(id: String, string: String) = Regex("$id[ \n]").replace(string, "")

fun List<String>.conditions() = this.joinToString(" && ") {
    if (it.matches(Regex("^([A-Za-z0-9]|_)*$"))) "defined $it" else "($it)"
}
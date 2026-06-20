package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.collections.flatten
import kotlin.io.path.absolutePathString

fun injectBuffersIntoShaderCode(shaderCode: String, newBuffers: List<Pair<Int, String>>) = buildString {
	// manually replace funky code
	// TODO remove when SpaceEagle rewrites the section
	val shaderCode = shaderCode.replace(
		Regex("#endif\\s+#ifdef PHOTONICS_LIGHTING", RegexOption.MULTILINE),
		"#elif defined PHOTONICS_LIGHTING"
	)

	// find all areas without else branches
	val lines = shaderCode.lines()

	var stackSize = 0  // length of flattened stack
	val lst = Array(20) { false }  // could fail if there are more than 20 nested #if, #endifs but wtv
	val stack = arrayListOf<List<Int>>(listOf())  // store all buffers up to that point
	lines.forEach {
		val line = it.trimIndent()
		val indent = it.takeWhile { it.isWhitespace() }

		when {
			line.startsWith("#if") -> stack.add(listOf())
			line.startsWith("#endif") || line.startsWith("#elif") || line.startsWith("#else") -> {
				if (stack.last().isNotEmpty()) {
					appendLine("$indent    /* RENDERTARGETS: ${stack.flatten().joinToString(",")},${newBuffers.joinToString(",") { it.first.toString() }} */")
					newBuffers.forEach {
						appendLine("$indent    gl_FragData[$stackSize] = ${it.second}")
					}
				}

				if (line.startsWith("#endif")) {
					if (!lst[stack.size]) {
						stackSize -= stack.last().size
						stack[stack.size - 1] = listOf()

						// inject else before continuing
						appendLine("$indent#else")
						appendLine("$indent    /* RENDERTARGETS: ${stack.flatten().joinToString(",")},${newBuffers.joinToString(",") { it.first.toString() }} */")
						newBuffers.forEach {
							appendLine("$indent    gl_FragData[$stackSize] = ${it.second}")
						}
					} else {
						lst[stack.size] = false
						stackSize -= stack.last().size
						stack[stack.size - 1] = listOf()
					}
				} else {
					stackSize -= stack.last().size
					stack[stack.size - 1] = listOf()

					// indicate #if ... #endif branch includes #else
					if (line.startsWith("#else"))
						lst[stack.size] = true
				}
			}
			line.startsWith("/* DRAWBUFFERS") -> {
				val buffers = Regex("DRAWBUFFERS: *(\\d+)").find(line)!!.groupValues[1]
				stack[stack.size - 1] = buffers.map { it.digitToInt() }.filter { it !in stack.flatten() }
				stackSize += stack.last().size
			}
			line.startsWith("/* RENDERTARGETS") -> {
				val buffers = Regex("RENDERTARGETS: *((\\d+,)*\\d+)").find(line)!!.groupValues[1]
				stack[stack.size - 1] = buffers.split(",").map { it.toInt() }.filter { it !in stack.flatten() }
				stackSize += stack.last().size
			}
		}

		appendLine(it)
	}
}

fun findMatchingEndif(shaderCode: String, startIndex: Int): Int {
    val lines = shaderCode.substring(startIndex).lines()
    var depth = 0
    var currentOffset = startIndex

    for (line in lines) {
        val trimmed = line.trim()
        when {
            trimmed.startsWith("#if") -> depth++
            trimmed.startsWith("#endif") -> {
                depth--
                if (depth == 0) {
                    return currentOffset + line.indexOf("#endif")
                }
            }
        }

        currentOffset += line.length + 1 // +1 for the newline
    }

    return -1 // No matching #endif found
}

fun injectIntoFragmentMain(
    shaderCode: String,
    injectedStart: String,
    injectedEnd: String,
    fileName: String
): String {
    val fragmentStart = shaderCode.indexOf("#ifdef FRAGMENT_SHADER")
    val fragmentEnd = findMatchingEndif(shaderCode, fragmentStart)

    if (fragmentStart == -1 || fragmentEnd == -1) {
        throw MinecraftError("$fileName does not contain fragment shader.", fileName)
    }

    val fragmentBlock = shaderCode.substring(fragmentStart, fragmentEnd)
    val mainStart = fragmentBlock.indexOf("void main()")

    if (mainStart == -1) {
        throw MinecraftError("No 'main()' function found in fragment shader.", fileName)
    }

    // Get the absolute index of main() relative to the full shader
    val mainStartIndex = fragmentStart + mainStart
    val braceOpenIndex = shaderCode.indexOf('{', mainStartIndex)
    if (braceOpenIndex == -1) {
        throw MinecraftError("No opening brace '{' found after 'main()'.", fileName)
    }

    // Find matching closing brace
    var braceCount = 1
    var currentIndex = braceOpenIndex + 1
    while (currentIndex < shaderCode.length && braceCount > 0) {
        when (shaderCode[currentIndex]) {
            '{' -> braceCount++
            '}' -> braceCount--
        }
        currentIndex++
    }

    if (braceCount != 0) {
        throw MinecraftError("Unbalanced braces in fragment shader main().", fileName)
    }

    val braceCloseIndex = currentIndex

    // Extract parts of the shader
    val beforeMain = shaderCode.take(braceOpenIndex + 1)
    val mainBody = shaderCode.substring(braceOpenIndex + 1, braceCloseIndex - 1).trim()
    val afterMain = shaderCode.substring(braceCloseIndex - 1)

    // Inject code into main()
    val modifiedMainBody = buildString {
        appendLine()
        appendLine(injectedStart.prependIndent("    "))
        appendLine("    $mainBody")
        appendLine(injectedEnd.prependIndent("    "))
    }

    // Return new shader code
    return beforeMain + modifiedMainBody + afterMain
}

const val TOTAL_BUFFERS = 31

data class Buffer(
    val name: String,
    val imageFormat: String,
    val conditions: List<String>,
    val reads: List<String>,
    val writes: List<String>
)

val BUFFERS = arrayListOf<Buffer>()
fun injectBuffers(directory: Path) {
    // TODO generate full list of conditions to check for and locations to inject buffers
    val images = BUFFERS.groupBy { it.imageFormat }.map { it.value.chunked(4) }.flatten().toList()
    for (i in 0..<images.size) {
        UNIFORMS.add(
            Uniform(
                "colortex${TOTAL_BUFFERS - i}",
                "sampler2D",
                "",
                listOf()
            )
        )
    }

    // read
    val selectors = "rgba"
    val readCode = images.mapIndexed { i, it ->
        StringBuilder().apply {
            append("vec4 texture${TOTAL_BUFFERS - i} = texelFetch(colortex${TOTAL_BUFFERS - i}, texelCoord, 0);\n")

            it.forEachIndexed { j, it ->
                append("float ${it.name} = texture${TOTAL_BUFFERS - i}.${selectors[j]};\n")
            }
        }.toString()
    }.toList()

    images.forEachIndexed { i, it ->
        it.map { it.reads }.flatten().forEach { filePath ->
            val file = File(directory.absolutePathString() + filePath)
            file.writeText(
                injectIntoFragmentMain(
                    file.readText(),
                    readCode[i],
                    "",
                    filePath
                )
            )
        }
    }

    // writing
    val groupedWrites = images.map { image ->
        image.map { it.writes.map { Pair(image, it) } }.flatten().distinctBy { it.second }
    }.flatten().groupBy { it.second }
    groupedWrites.forEach { filePath, lst ->
        val lst = lst.map { it.first }

        val file = File(directory.absolutePathString() + filePath)
        val code = file.readText()
        val shaderCode = Regex("(/\\* DRAWBUFFERS(.|\\n|\\s)*?)}", RegexOption.MULTILINE).find(code)!!.groupValues[1]

        var newCode = injectBuffersIntoShaderCode(shaderCode, lst.map {
            Pair(
				TOTAL_BUFFERS - images.indexOf(it),
                "vec4(${(it.map { it.name } + List(4 - it.size) { "0.0" }).joinToString(", ")});"
            )
        }.toList())
        lst.forEach {
            it.filter { filePath !in it.writes }.forEach {
                newCode = newCode.replace(it.name, "0.0")
            }
        }

        file.writeText(
            code.replace(
                shaderCode,
                newCode
            )
        )
    }
}

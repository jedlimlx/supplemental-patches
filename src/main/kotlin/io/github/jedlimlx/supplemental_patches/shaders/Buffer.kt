package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

data class Buffer(
    val name: String,
    val byteSize: Int,
    val screenSized: Boolean,
    val bufferVariables: List<Pair<String, String>>
)

val BUFFERS = arrayListOf<Buffer>()
fun injectBuffers(directory: Path) {
    // injecting GLSL code that defines buffers
    val code = StringBuilder().apply {
        var count = 0
        BUFFERS.forEach {
            append("struct str${count++} {\n")
            it.bufferVariables.forEach {
                append("    ${it.first} ${it.second};\n")
            }
            append("}\n\n")
        }

        count = 0
        append("layout(std430, binding = 7) buffer ssBuffer {\n")
        BUFFERS.forEach {
            append("    str${count++} ${it.name}[];\n")
        }
        append("}\n\n")
    }

    val commonGlsl = File(directory.absolutePathString() + COMMON_GLSL_FILE)
    commonGlsl.appendText(code.toString())

    // injecting code into shaders.properties
    val totalByteSize = BUFFERS.sumOf { it.byteSize }
    val shaderPropertiesFile = File(directory.absolutePathString() + SHADER_PROPERTIES_FILE)
    if (totalByteSize > 0) {
        shaderPropertiesFile.appendText("# SSBOs added by Supplemental Patches\n")
        shaderPropertiesFile.appendText("    bufferObject.7 = $totalByteSize true 1.0 1.0\n")
    }
}
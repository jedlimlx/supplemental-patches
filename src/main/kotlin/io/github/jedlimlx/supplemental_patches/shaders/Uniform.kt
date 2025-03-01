package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

val UNIFORMS: ArrayList<Uniform> = arrayListOf()

data class Uniform(val name: String, val type: String, val code: String, val conditions: List<String>) {
    val custom: Boolean = code.isNotEmpty()
}

const val UNIFORMS_GLSL_FILE = "/shaders/lib/uniforms.glsl"
fun generateUniforms(directory: Path) {
    val uniformGlslCode = StringBuilder("\n\n${BANNER.replace("#", "//")}// Uniforms added by Supplemental Patches\n\n").apply {
        UNIFORMS.forEach {
            if (it.conditions.isNotEmpty()) {
                append("#if ${it.conditions.conditions()}\n")
                append("    uniform ${it.type} ${it.name};\n")
                append("#endif\n")
            } else append("uniform ${it.type} ${it.name};\n")
        }

        append("\n\n")
    }

    val uniformGlsl = File(directory.absolutePathString() + UNIFORMS_GLSL_FILE)
    uniformGlsl.appendText(uniformGlslCode.toString())

    val shaderPropertiesCode = StringBuilder("\n\n$BANNER# Uniforms added by Supplemental Patches\n\n").apply {
        val indent = "    ";
        UNIFORMS.filter { it.custom }.forEach {
            if (it.conditions.isNotEmpty()) {
                append("$indent#if ${it.conditions.conditions()}\n")
                append("${indent}uniform.${it.type}.${it.name} = ${it.code}\n")
                append("$indent#endif\n")
            } else append("${indent}uniform ${it.type} ${it.name}\n")
        }

        append("\n\n")
    }

    val shaderProperties = File(directory.absolutePathString() + SHADER_PROPERTIES_FILE)
    shaderProperties.appendText(shaderPropertiesCode.toString())
}

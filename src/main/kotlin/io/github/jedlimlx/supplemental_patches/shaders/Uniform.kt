package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

val UNIFORMS: ArrayList<Uniform> = arrayListOf()

data class Uniform(val name: String, val type: String, val code: String, val conditions: List<String>, val defaultValue: String? = null) {
    val custom: Boolean = code.isNotEmpty()
}

const val UNIFORMS_GLSL_FILE = "/shaders/lib/uniforms.glsl"
fun generateUniforms(directory: Path) {
    val uniformGlslCode = StringBuilder("\n\n${BANNER.replace("#", "//")}// Uniforms added by Supplemental Patches\n\n").apply {
        UNIFORMS.forEach {
            if (it.conditions.isNotEmpty() && it.defaultValue == null) {
                append("#if ${it.conditions.conditions()}\n")
                append("    uniform ${it.type} ${it.name};\n")
                append("#endif\n")
            } else if (it.defaultValue == null)
                append("uniform ${it.type} ${it.name};\n")
            else append("uniform ${it.type} ${it.name} = ${it.defaultValue};\n")
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
                append("$indent#else\n")
                append("${indent}uniform.${it.type}.${it.name} = ${it.defaultValue}\n")
                append("$indent#endif\n")
            } else append("${indent}uniform.${it.type}.${it.name} = ${it.code}\n")
        }

        append("\n\n")
    }

    val shaderProperties = File(directory.absolutePathString() + SHADER_PROPERTIES_FILE)
    shaderProperties.appendText(shaderPropertiesCode.toString())
}

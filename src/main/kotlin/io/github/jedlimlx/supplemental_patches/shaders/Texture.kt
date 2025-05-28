package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

val TEXTURES = mutableListOf<Texture>()

data class Texture(val texture: String, val name: String, val conditions: List<String>) {
    init {
        UNIFORMS.add(Uniform(name, "sampler2D", "", conditions))
    }
}

fun generateTextures(directory: Path) {
    val file = File(directory.absolutePathString() + SHADER_PROPERTIES_FILE)
    file.appendText(
        StringBuilder("# Textures added by Supplemental Patches\n").apply {
            val indent = "    "
            TEXTURES.forEach {
                if (it.conditions.isNotEmpty()) {
                    append("$indent#if ${it.conditions.conditions()}\n")
                    append("$indent${indent}customTexture.${it.name} = ${it.texture}\n")
                    append("${indent}#endif\n")
                } else {
                    append("${indent}customTexture.${it.name} = ${it.texture}\n")
                }
            }
        }.toString()
    )
}

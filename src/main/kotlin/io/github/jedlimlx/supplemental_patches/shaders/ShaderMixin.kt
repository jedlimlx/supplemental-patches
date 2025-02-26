package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

val MIXINS = arrayListOf<ShaderMixin>()

enum class ShaderMixinType {
    BEFORE,
    AFTER,
    REPLACE;

    companion object {
        fun fromString(type: String): ShaderMixinType = when (type) {
            "before" -> BEFORE
            "after" -> AFTER
            "replace" -> REPLACE
            else -> throw IllegalArgumentException("Unknown shader mixin type '$type'")
        }
    }
}

data class ShaderMixin(val path: String, val type: ShaderMixinType, val key: String, val code: String) {
    fun inject(directory: Path) {
        val file = File(directory.absolutePathString() + path)
        val tokens = file.readText().split(key)

        val indents = tokens.subList(0, tokens.size - 1).map {
            val temp = it.split("\n").last()
            val indent = " ".repeat(temp.length - temp.trimIndent().length)
            indent
        }
        val modifiedIndent = indents.map {
            if (type == ShaderMixinType.AFTER && key.matches(Regex("(.*?)\\{.*"))) {
                "$it    "
            } else it
        }

        val modifiedTokens = tokens.subList(0, tokens.size - 1).map {
            val lines = it.split("\n")
            lines.subList(0, lines.size - 1).joinToString("\n") + "\n"
        } + tokens.last()

        val newCode = when (type) {
            ShaderMixinType.BEFORE -> modifiedTokens.mapIndexed { idx, it ->
                if (idx != tokens.size - 1) "$it${code.prependIndent(modifiedIndent[idx])}\n" else it
            }.joinToString(key)

            ShaderMixinType.AFTER -> modifiedTokens.mapIndexed { idx, it ->
                if (idx != 0) "\n${code.prependIndent(modifiedIndent[idx - 1])}$it" else it
            }.joinToString(key)

            ShaderMixinType.REPLACE -> modifiedTokens.mapIndexed { idx, it ->
                if (idx != 0) "${code.prependIndent(modifiedIndent[idx])}$it"
            }.joinToString("\n")
        }

        file.writeText(newCode)
    }
}

fun generateShaderMixins(directory: Path) = MIXINS.forEach { it.inject(directory) }
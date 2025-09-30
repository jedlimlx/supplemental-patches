package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

val ATMOSPHERICS = mutableListOf<Atmospherics>()
data class Atmospherics(val libPath: String, val libCode: String, val mainCode: String, val conditions: List<String>)

const val COMPOSITE_PATH = "/shaders/program/composite1.glsl"
fun generateAtmospherics(directory: Path) {
    val file = File(directory.absolutePathString() + COMPOSITE_PATH)

    val compositeCode = StringBuilder().apply {
        val indent = "    "
        ATMOSPHERICS.forEach {
            append("$indent#if ${it.conditions.conditions()}\n")
            append(it.mainCode.prependIndent(indent.repeat(2)) + "\n")
            append("$indent#endif\n")
            append("\n")
        }
    }.toString()

    val compositeIncludes = StringBuilder().apply {
        val indent = "    "
        ATMOSPHERICS.forEach {
            append("#if ${it.conditions.conditions()}\n")
            append("$indent#include \"/lib/atmospherics/${it.libPath}\"\n")
            append("#endif\n")
            append("\n")
        }
    }.toString()

    file.writeText(
        file.readText().replace(
            "    if (isEyeInWater == 1) {",
            "$compositeCode\n    if (isEyeInWater == 1) {"
        )
    )

    file.writeText(
        file.readText().replaceFirst(
            "//Includes//",
            "//Includes//\n$compositeIncludes"
        )
    )

    ATMOSPHERICS.forEach {
        val file = File(directory.absolutePathString() + "/shaders/lib/atmospherics/${it.libPath}")
        file.writeText(it.libCode)
    }

    val conditions = ATMOSPHERICS.map { "(${it.conditions.conditions()})" }.joinToString(" || ")
    file.writeText(
        file.readText().replace(
            "defined NETHER_STORM || defined COLORED_LIGHT_FOG",
            "$conditions || defined NETHER_STORM || defined COLORED_LIGHT_FOG"
        )
    )
}

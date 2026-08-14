package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

val SKIES = mutableListOf<Sky>()

data class Sky(
    val name: String,
    val code: String,
    val dimension: String,
    val deferred: String,
    val reflection: String?,
    val conditions: List<String>
)

const val DEFERRED_PATH = "/shaders/program/deferred1.glsl"
const val REFLECTION_PATH = "/shaders/lib/materials/materialMethods/reflectionBackground.glsl"
const val REFLECTION_PATH_2 = "/shaders/lib/materials/materialMethods/reflections.glsl"
fun generateSkies(directory: Path) {
    // generate atmospheric libraries within atmospherics folder
    SKIES.forEach {
        val file = File(directory.absolutePathString() + "/shaders/lib/atmospherics/${it.name}")
        file.writeText(it.code)
    }

    val importCode = StringBuilder().apply {
        SKIES.forEach {
            append("#if ${(it.conditions + listOf("defined ${it.dimension}")).conditions()}\n")
            append("    #include \"/lib/atmospherics/${it.name}\"\n")
            append("#endif\n")
            append("\n")
        }
    }.toString()

	val importCodeNoReflection = StringBuilder().apply {
		SKIES.forEach {
			if (it.reflection == null) return@forEach
			append("#if ${(it.conditions + listOf("defined ${it.dimension}")).conditions()}\n")
			append("    #include \"/lib/atmospherics/${it.name}\"\n")
			append("#endif\n")
			append("\n")
		}
	}.toString()

    // injecting code into deferred1.glsl
    val deferredFile = File(directory.absolutePathString() + DEFERRED_PATH)
    deferredFile.writeText(
        deferredFile.readText().replace(
            Regex("#if defined END && defined END_STARS\r?\n    #include \"/lib/atmospherics/enderStars.glsl\""),
            "$importCode\n#if defined END && defined END_STARS\n    #include \"/lib/atmospherics/enderStars.glsl\""
        )
    )

    // injecting code into gbuffers_water.glsl / dh_water.glsl
    val reflectionPath = File(directory.absolutePathString() + REFLECTION_PATH_2)
    reflectionPath.writeText(
        reflectionPath.readText().replace(
            "#ifdef ATM_COLOR_MULTS",
			"$importCodeNoReflection\n#ifdef ATM_COLOR_MULTS"
        )
    )

    SKIES.forEach {
        val code = StringBuilder().apply {
            val indent = " ".repeat(16)
            if (it.conditions.isNotEmpty()) {
                append("$indent\n")
                append("$indent#if ${it.conditions.conditions()}\n")
                append("${it.deferred.prependIndent("$indent    ")}\n")
                append("$indent#endif\n")
            } else {
                append("$indent\n")
                append("$indent${it.deferred}\n")
            }
        }.toString()

        val regex = when (it.dimension) {
            "OVERWORLD" -> Regex("color.rgb \\+= nightNebula;\\r?\\n {12}#endif")
            "NETHER" -> Regex("color.rgb = netherColor \\* \\(1.0 - maxBlindnessDarkness\\);")
            "END" -> Regex("color.rgb = endSkyColor;")
            else -> Regex("^$")
        }

        val key = when (it.dimension) {
            "OVERWORLD" -> "color.rgb += nightNebula;\n            #endif"
            "NETHER" -> "color.rgb = netherColor * (1.0 - maxBlindnessDarkness);"
            "END" -> "color.rgb = endSkyColor;"
            else -> ""
        }

        deferredFile.writeText(
            deferredFile.readText().replace(regex, key + code)
        )
    }

    val reflectionFile = File(directory.absolutePathString() + REFLECTION_PATH)
    SKIES.forEach {
		if (it.reflection == null) return@forEach
        val code = StringBuilder().apply {
            val indent = " ".repeat(if (it.dimension == "END") 8 else 20)
            if (it.conditions.isNotEmpty()) {
                append("$indent\n")
                append("$indent#if ${it.conditions.conditions()}\n")
                append("$indent    ${it.reflection}\n")
                append("$indent#endif\n")
            } else {
                append("$indent\n")
                append("$indent${it.reflection}\n")
            }
        }.toString()

        val regex = when (it.dimension) {
            "OVERWORLD" -> Regex("skyReflection \\+= \\(DrawOverworldBeams\\(RVdotU, playerPos, viewPos\\) \\* 0\\.4 \\+ 0\\.6\\)\\.rgb \\* 0\\.08;\\r?\\n {20}#endif")
            "END" -> Regex("vec3 skyReflection = \\(endSkyColor \\+ enderNebula \\+ enderStars \\+ volumetricEffect.rgb\\) \\* skyLightFactor;\\r?\\n")
            else -> Regex("^$")
        }

        val key = when (it.dimension) {
            "OVERWORLD" -> "skyReflection += (DrawOverworldBeams(RVdotU, playerPos, viewPos) * 0.4 + 0.6).rgb * 0.08;\n                    #endif"
            "END" -> "vec3 skyReflection = (endSkyColor + enderNebula + enderStars + volumetricEffect.rgb) * skyLightFactor;\n"
            else -> ""
        }

        reflectionFile.writeText(
            reflectionFile.readText().replace(regex, key + code)
        )
    }
}

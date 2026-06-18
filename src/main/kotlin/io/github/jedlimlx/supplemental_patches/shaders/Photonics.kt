package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

fun formatJsonValue(value: String): String {
	return if (value.toDoubleOrNull() != null || value == "true" || value == "false" || value[0] == '~')
		value.replace("~", "")
	else "\"$value\""
}

data class LightGroupProperty(
	val name: String,
	val values: List<Pair<List<String>, String>>
) {
	fun toString(comma: Boolean): String = buildString {
		if (values.size == 1) {  // if only one condition
			val (condition, value) = values[0]
			val line = "\"$name\": ${formatJsonValue(value)}"
			if (condition.isEmpty()) append(line + if (comma) "," else "")
			else {
				appendLine("#if ${condition.conditions()}")
				appendLine("$line,")
				append("#endif")
			}
		} else {  // requires elif branch
			var first = true
			values.forEach { (condition, value) ->
				if (condition.isEmpty()) appendLine("#else")
				else if (first) appendLine("#if ${condition.conditions()}")
				else appendLine("#elif ${condition.conditions()}")

				appendLine("\"$name\": ${formatJsonValue(value)},")
				first = false
			}
			append("#endif")
		}
	}
}

data class LightGroup(
	val group: String,
	val blocks: MutableList<Pair<List<String>, List<String>>>,
	val properties: List<LightGroupProperty>,
	val overrides: MutableList<LightGroup>
) {
	override fun toString(): String = buildString {
		appendLine("\"$group\": {")
		for (i in 0..<properties.size) {
			append(properties[i].toString(i + 1 < properties.size).prependIndent("  "))
			if (i + 1 < properties.size) appendLine()
		}

		if (blocks.isNotEmpty()) {
			if (properties.isNotEmpty() && properties.last().values.size == 1 && properties.last().values[0].first.isEmpty())
				appendLine(",")
			else appendLine()

			appendLine("  \"blocks\": [")
			val filteredBlocks = blocks.filter { it.second.isNotEmpty() }
			filteredBlocks.forEachIndexed { index, (conditions, it) ->
				val comma = index < filteredBlocks.size - 1
				appendLine(
					buildString {
						if (conditions.isNotEmpty()) {
							appendLine("#if ${conditions.conditions()}")
							append(it.joinToString(",\n") { formatJsonValue(it) })
							appendLine(if (comma) "," else "")
							append("#endif")
						} else {
							append(it.joinToString(",\n") { formatJsonValue(it) })
							append(if (comma) "," else "")
						}
					}.prependIndent("    ")
				)
			}
			append("  ]")
		}

		if (overrides.isNotEmpty()) {
			if (blocks.isNotEmpty() || (properties.isNotEmpty() && properties.last().values.size == 1 && properties.last().values[0].first.isEmpty()))
				appendLine(",")
			else appendLine()

			appendLine("  \"overrides\": {")
			appendLine(overrides.joinToString(",\n").prependIndent("    "))
			append("  }")
		}

		append("\n}")
	}
}

class Defines(
	val colours: ArrayList<Pair<String, String>> = arrayListOf(),
	val intensities: ArrayList<Pair<String, Double>> = arrayListOf(),
	val radii: ArrayList<Pair<String, Double>> = arrayListOf(),
	val falloffs: ArrayList<Pair<String, Double>> = arrayListOf()
) {
	fun clear() {
		colours.clear()
		intensities.clear()
		radii.clear()
		falloffs.clear()
	}

	override fun toString(): String = buildString {
		appendLine("\"defines\": {")

		appendLine("  \"colors\": {")
		appendLine(colours.joinToString(",\n") { (k, v) -> "    \"$k\": \"$v\"" })
		appendLine("  },")

		appendLine("  \"intensities\": {")
		appendLine(intensities.joinToString(",\n") { (k, v) -> "    \"$k\": $v" })
		appendLine("  },")

		appendLine("  \"radii\": {")
		appendLine(radii.joinToString(",\n") { (k, v) -> "    \"$k\": $v" })
		appendLine("  },")

		appendLine("  \"falloffs\": {")
		appendLine(falloffs.joinToString(",\n") { (k, v) -> "    \"$k\": $v" })
		appendLine("  }")

		append("}")
	}
}

val DEFINES = Defines()
val LIGHT_GROUPS: MutableList<LightGroup> = arrayListOf()

val PH_LIGHTS_PATH = "/shaders/ph_lights.json"
fun constructPhLights(directory: Path) {
	val file = File(directory.absolutePathString() + PH_LIGHTS_PATH)
	var oldCode = file.readText()
	oldCode = oldCode.replace(Regex("(?s)\\{\\s*\"defines\"\\s*:\\s*\\{.*}\\s*$", RegexOption.MULTILINE), "")

	val newCode = oldCode + buildString {
		appendLine("{")
		append(DEFINES.toString().prependIndent("  "))
		appendLine(",")
		appendLine("  \"lights\": {")
		appendLine(LIGHT_GROUPS.joinToString(",\n") { it.toString().prependIndent("    ") })
		appendLine("  }")
		appendLine("}")
	}

	file.writeText(newCode)
}

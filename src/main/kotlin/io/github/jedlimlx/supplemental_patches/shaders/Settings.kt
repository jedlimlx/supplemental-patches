package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

fun generateShaderProperties(lst: List<Settings>): String = "<empty> <empty> " + lst.sortedWith(Comparator { o1, o2 ->
    return@Comparator if (o1.priority > o2.priority) -1 else if (o1.priority < o2.priority) 1 else o1.name.compareTo(o2.name)
}).joinToString(" ") {
    when (it.type) {
        SettingType.DIRECTORY -> "[${it.name}]"
        SettingType.INFORMATION -> it.name
        SettingType.DIVIDER -> "<empty> <empty>"
        SettingType.SETTING -> it.name
    }
}

enum class SettingType(val string: String) {
    DIRECTORY("directory"),
    INFORMATION("info"),
    SETTING("setting"),
    DIVIDER("divider");

    companion object {
        fun fromString(string: String): SettingType {
            return when (string) {
                "directory" -> DIRECTORY
                "info" -> INFORMATION
                "setting" -> SETTING
                "divider" -> DIVIDER
                else -> throw IllegalArgumentException("No setting type $string.")
            }
        }
    }
}

class Settings(
    val type: SettingType,
    val name: String,
    val priority: Int,
    val language: Map<String, Map<String, String>>,
    val conditions: List<Pair<String, String>>,
    val values: List<String>,
    val slider: Boolean = false
) {
    val children: MutableList<Settings> = arrayListOf()

    val commonGlsl: String
        get() {
            fun getCondition(condition: String) = if (condition.matches(Regex("^([A-Za-z0-9]|_)*$"))) "defined $condition" else condition

            val indent = " ".repeat(4)
            fun getDef(value: String): String =
                if (value == "true") "#define $name\n"
                else if (value != "false") "#define $name $value //[${values.joinToString(" ")}]\n"
                else "//#define $name\n"

            if (conditions.size == 1 && conditions[0].first == "else") return "$indent${getDef(conditions[0].second)}"
            return StringBuilder().apply {
                conditions.forEachIndexed { it, (condition, value) ->
                    when (condition) {
                        "else" -> append("$indent#else\n$indent$indent${getDef(value)}")
                        else -> {
                            if (it == 0) append("$indent#if $condition\n$indent$indent${getDef(value)}")
                            else append("$indent#elif $condition\n$indent$indent${getDef(value)}")
                        }
                    }
                }
                append("$indent#endif\n")
            }.toString()
        }

    val shaderProperties: String
        get() = generateShaderProperties(children)

    fun language(code: String): String = language[code]?.map { (key, value) ->
        val tokens = key.split(".")
        val temp = tokens[0]
        val temp2 = if (tokens.size > 1) "." + tokens.subList(1, tokens.size).joinToString(".") else ""

        if (temp == "comment") {
            if (type == SettingType.DIRECTORY) "screen.$name.comment=$value"
            else "option.$name.comment=$value"
        } else "$temp.$name$temp2=$value"
    }?.joinToString("\n") ?: ""
}

val SETTINGS = mutableListOf<Settings>()

const val COMMON_GLSL_FILE = "/shaders/lib/common.glsl"
const val SHADER_PROPERTIES_FILE = "/shaders/shaders.properties"
const val LANGUAGE_FILE = "/shaders/lang/en_US.lang"
fun generateSettings(directory: Path) {
    val sliders = mutableListOf<Settings>()
    val shaderPropertiesCode = StringBuilder("\n$BANNER# Settings added by Supplemental Patches\n\n").apply {
        fun recurse(settings: Settings, indent: String) {
            append("${indent}screen.${settings.name}=${settings.shaderProperties}\n")
            append("${indent}screen.${settings.name}.columns=2\n")
            settings.children.forEach {
                if (it.slider) sliders.add(it)
                if (it.type == SettingType.DIRECTORY) recurse(it, indent + " ".repeat(4))
            }
        }

        val indent = " ".repeat(4)
        append("${indent}screen.SUPPLEMENTAL_SETTINGS=${generateShaderProperties(SETTINGS)}\n")
        SETTINGS.forEach {
            if (it.type == SettingType.DIRECTORY) recurse(it, indent + " ".repeat(4))
        }
    }

    val shaderProperties = File(directory.absolutePathString() + SHADER_PROPERTIES_FILE)
    shaderProperties.writeText(
        shaderProperties.readText().replace(
            "<empty> <empty> [EUPHORIA_SETTINGS]",
            "<empty> <empty> [SUPPLEMENTAL_SETTINGS] [SUPPLEMENTAL_VERSION] [EUPHORIA_SETTINGS]"
        ).replace(
            "screen.EP_VERSION.columns=1",
            "screen.EP_VERSION.columns=1\n$shaderPropertiesCode"
        ).replace(
            "PURKINJE_RENDER_DISTANCE_FADE",
            "PURKINJE_RENDER_DISTANCE_FADE \\\n    ${sliders.joinToString(" ") { it.name }}"
        )
    )

    val langFile = File(directory.absolutePathString() + LANGUAGE_FILE)
    langFile.appendText(
        StringBuilder("\n$BANNER# Settings added by Supplemental Patches\n").apply {
            append("screen.SUPPLEMENTAL_SETTINGS=§dSupplemental Settings\n\n")
            fun recurse(setting: Settings) {
                val output = setting.language("en_US")
                if (output.isNotEmpty()) append(output + "\n")
                if (setting.type == SettingType.INFORMATION)
                    append("value.${setting.name}.0=\n")

                append("\n")
                setting.children.forEach { recurse(it) }
            }

            SETTINGS.forEach { recurse(it) }
        }.toString()
    )

    val commonGlsl = File(directory.absolutePathString() + COMMON_GLSL_FILE)
    commonGlsl.appendText(
        StringBuilder("\n${BANNER.replace("#", "//")}// Settings added by Supplemental Patches\n\n").apply {
            fun recurse(setting: Settings) {
                if (setting.type == SettingType.SETTING)
                    append(setting.commonGlsl + "\n")
                else if (setting.type == SettingType.INFORMATION)
                    append("    #define ${setting.name} 0 //[0]\n")

                setting.children.forEach { recurse(it) }
            }

            SETTINGS.forEach { recurse(it) }
        }.toString()
    )
}
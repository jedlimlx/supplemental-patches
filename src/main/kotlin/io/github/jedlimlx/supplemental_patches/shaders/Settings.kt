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
        SettingType.DIVIDER -> List(it.dividers) { "<empty>" }.joinToString(" ")
        SettingType.SETTING -> it.name
    }
}

enum class SettingType(val string: String) {
    DIRECTORY("directory"),
    INFORMATION("info"),
    SETTING("setting"),
    DIVIDER("divider");

    companion object {
        fun fromString(string: String, file: String): SettingType {
            return when (string) {
                "directory" -> DIRECTORY
                "info" -> INFORMATION
                "setting" -> SETTING
                "divider" -> DIVIDER
                else -> throw MinecraftError("\"$string\" is not a valid setting type. Valid types are [directory, info, setting, divider].", file)
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
    val slider: Boolean = false,
    settingsFile: String = "common.glsl",
    val activation: Boolean = false,
    val dividers: Int = 2
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

            if (conditions.size == 1 && conditions[0].first == "else") return getDef(conditions[0].second)
            return StringBuilder().apply {
                conditions.forEachIndexed { it, (condition, value) ->
                    when (condition) {
                        "else" -> append("#else\n$indent${getDef(value)}")
                        else -> {
                            if (it == 0) append("#if $condition\n$indent${getDef(value)}")
                            else append("#elif $condition\n$indent${getDef(value)}")
                        }
                    }
                }
                append("#endif\n")
            }.toString()
        }

    val shaderProperties: String
        get() = generateShaderProperties(children)

    val settingsFile: String = if (type == SettingType.INFORMATION) "settingsFileDefines.glsl" else settingsFile

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
val SETTINGS_BANNER = "\n${BANNER.replace("#", "//")}// Settings added by Supplemental Patches\n\n"

const val SHADER_SETTINGS_FOLDER = "/shaders/lib/shaderSettings/"
const val COMMON_GLSL_FILE = "/shaders/lib/common.glsl"

val visited: HashSet<String> = hashSetOf()
fun writeToSettingsFile(directory: Path, fileName: String, text: String) {
    if (fileName == "common.glsl") {
        val commonGlsl = File(directory.absolutePathString() + COMMON_GLSL_FILE)
        if (fileName !in visited) commonGlsl.appendText(SETTINGS_BANNER + text)
        else commonGlsl.appendText(text)
    } else {
        val file = File(directory.absolutePathString() + SHADER_SETTINGS_FOLDER + fileName)
        val lines = file.readText().lines()

        var count = 0
        while (lines[lines.size - count++ - 1].isEmpty()) {
            println(count)
        }

        val newText = lines.subList(0, lines.size - count).joinToString("\n") + "\n$text#endif"
        if (fileName !in visited) file.writeText(SETTINGS_BANNER + newText)
        else file.writeText(newText)
    }

    visited.add(fileName)
}

const val SHADER_PROPERTIES_FILE = "/shaders/shaders.properties"
const val LANGUAGE_FILE = "/shaders/lang/en_US.lang"
const val ACTIVATION_FILE = "/shaders/lib/shaderSettings/activateSettings.glsl"
fun generateSettings(directory: Path) {
    val activationFile = File(directory.absolutePathString() + ACTIVATION_FILE)

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
        append("${indent}screen.SUPPLEMENTAL_SETTINGS.columns=2\n")
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
            "CLOUD_LAYER2_SPEED_MULT WATER_CAUSTIC_STRENGTH",
            "CLOUD_LAYER2_SPEED_MULT WATER_CAUSTIC_STRENGTH \\\n    ${sliders.joinToString(" ") { it.name }}"
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

    fun recurse(setting: Settings) {
        if (setting.type == SettingType.SETTING) {
            writeToSettingsFile(directory, setting.settingsFile, setting.commonGlsl + "\n")
            if (setting.activation) activationFile.appendText("\n#ifdef ${setting.name}\n#endif")
        } else if (setting.type == SettingType.INFORMATION)
            writeToSettingsFile(directory, setting.settingsFile,"#define ${setting.name} 0 //[0]\n")

        setting.children.forEach { recurse(it) }
    }

    SETTINGS.forEach { recurse(it) }
}

data class SettingsFile(val name: String, val files: List<String>)

val SETTINGS_FILES = mutableListOf<SettingsFile>()
fun generateSettingsFiles(directory: Path) = SETTINGS_FILES.forEach {
    val file = File(directory.absolutePathString() + SHADER_SETTINGS_FOLDER + it.name)
    file.createNewFile()
    file.writeText(
        StringBuilder().apply {
            val snakeCase = Regex("(?<=.)[A-Z]").replace(it.name.replace(".glsl", ""), "_$0").uppercase()
            append("#ifndef ${snakeCase}_SETTINGS_FILE\n")
            append("#define ${snakeCase}_SETTINGS_FILE\n\n")
            append("#endif")
        }.toString()
    )

    it.files.forEach { file ->
        val temp = File(directory.absolutePathString() + "/shaders/$file")
        temp.writeText("#include \"${SHADER_SETTINGS_FOLDER.replace("/shaders", "")}${it.name}\"\n" + temp.readText())
    }
}
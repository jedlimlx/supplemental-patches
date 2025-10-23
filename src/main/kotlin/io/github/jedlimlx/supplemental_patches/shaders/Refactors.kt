package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

abstract class RefactorTypes {
    abstract fun header(header: String): String
    abstract fun function(function: String): String
}

class NewParameter(val name: String, val default: String): RefactorTypes() {
    override fun header(header: String): String {
        var tokens = header.split(")")
        tokens = tokens.subList(0, tokens.size - 1)
        return tokens.joinToString(")") + ", $name)"
    }

    override fun function(function: String): String {
        var tokens = function.split(")")
        tokens = tokens.subList(0, tokens.size - 1)
        return tokens.joinToString(")") + ", $default)"
    }
}

data class Refactor(
    val function: String,
    val originalFile: String,
    val changes: List<RefactorTypes>,
    val files: List<String>
)

fun findFunctionCalls(text: String, functionName: String): List<String> {
    val pattern = """$functionName\s*\((?:[^()"']|"[^"]*"|'[^']*'|\([^()]*\))*\)""".toRegex()
    return pattern.findAll(text).map { it.value }.toList()
}

val REFACTORS = arrayListOf<Refactor>()
fun refactorFunctions(directory: Path) {
    println("asd asd asd asd asd asd")
    REFACTORS.map {
        val originalFile = File(directory.absolutePathString() + it.originalFile)
        var newHeader = it.function
        it.changes.forEach {
            newHeader = it.header(newHeader)
        }
        originalFile.writeText(
            originalFile.readText().replace(it.function, newHeader)
        )

        it.files.map { path ->
            val file = File(directory.absolutePathString() + path)
            var code = file.readText()
            println("asd asd " + it.function.split("(").first().split(" ").last())
            findFunctionCalls(code, it.function.split("(").first().split(" ").last()).forEach { call ->
                var newCall = call
                it.changes.forEach {
                    newCall = it.function(newCall)
                }
                code = code.replace(call, newCall)
            }
            file.writeText(code)
        }
    }
}


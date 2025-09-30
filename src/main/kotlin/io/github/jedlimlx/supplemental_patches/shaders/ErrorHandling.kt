package io.github.jedlimlx.supplemental_patches.shaders

import io.github.jedlimlx.supplemental_patches.SupplementalPatches
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import kotlin.collections.ArrayList
import kotlin.collections.iterator


// for errors by Iris and within the resource-pack
class MinecraftError(
    override val message: String,
    val fileName: String?,
    val errorType: String = "ERROR"
): RuntimeException(message) {
    val index: Int
        get() {
            var count = 0
            while (stackTrace[count].fileName == "ErrorHandling.kt") count++

            return count
        }

    // formatting is as such
    // [ERROR]: $message
    // --> Error found in $filename, this file will not be loaded.
    // --> Thrown by ${method}(${kotlin file}:${line number})
    fun sendInChat(): Boolean {
        SupplementalPatches.LOGGER.warn(stackTrace.toList())

        val player = Minecraft.getInstance().player
        if (player != null) {
            player.sendSystemMessage(
                Component.literal("[$errorType]: $message")
                    .withStyle(ChatFormatting.RED)
                    .withStyle(ChatFormatting.BOLD)
            )
            if (fileName != null) {
                player.sendSystemMessage(
                    Component.literal("")
                        .withStyle(ChatFormatting.RED)
                        .append(Component.literal("--> Error found in "))
                        .append(Component.literal(fileName).withStyle(ChatFormatting.ITALIC))
                        .append(Component.literal(", this file will not be loaded."))
                )
            }
            player.sendSystemMessage(
                Component.literal("")
                    .withStyle(ChatFormatting.RED)
                    .append(Component.literal("--> Thrown by "))
                    .append(Component.literal(stackTrace[index].methodName).withStyle(ChatFormatting.ITALIC))
                    .append(Component.literal("("))
                    .append(Component.literal(stackTrace[index].fileName ?: "Unknown").withStyle(ChatFormatting.ITALIC))
                    .append(Component.literal(":"))
                    .append(Component.literal(stackTrace[index].lineNumber.toString()).withStyle(ChatFormatting.ITALIC))
                    .append(Component.literal(")"))
            )

            return true
        }

        return false
    }

    fun log() {
        SupplementalPatches.LOGGER.warn(
            "[ERROR] $message\n" +
            if (fileName != null) "--> Error found in $fileName, this file will not be loaded.\n" else "" +
            "--> Thrown by ${stackTrace[index].methodName}(${stackTrace[index].fileName ?: "Unknown"}:${stackTrace[index].lineNumber})"
        )
    }
}

fun withErrorHandling(f: () -> Unit) {
    try {
        f()
    } catch (e: MinecraftError) {
        if (!e.sendInChat()) e.log()
    }
}

inline fun <K, V> Map<out K, V>.forEachWithErrorHandling(crossinline action: (Map.Entry<K, V>) -> Unit) {
    for (element in this) {
        withErrorHandling {
            action(element)
        }
    }
}

inline fun <T> Iterable<T>.forEachWithErrorHandling(crossinline action: (T) -> Unit): Unit {
    for (element in this) {
        withErrorHandling {
            action(element)
        }
    }
}

inline fun <K, V, R> Map<out K, V>.mapWithErrorHandling(crossinline transform: (Map.Entry<K, V>) -> R): List<R> {
    val lst = ArrayList<R>(size)
    forEachWithErrorHandling { lst.add(transform(it)) }
    return lst
}

// for OpenGL debugging errors
// [$type]: $message
// --> Source: $source
// --> Severity: $severity
// --> Debug message by OpenGL
data class ShaderError(
    val source: String?,
    val type: String?,
    val severity: String?,
    val message: String,
    val origin: String
) {
    fun sendInChat() {
        val message = message.split("\n").last { it.isNotEmpty() }

        val player = Minecraft.getInstance().player
        if (player != null) {
            player.sendSystemMessage(
                Component.literal("** caused by **")
            )
            player.sendSystemMessage(
                Component.literal("[$type]: $message")
                    .withStyle(ChatFormatting.RED)
                    .withStyle(ChatFormatting.BOLD)
            )
            player.sendSystemMessage(
                Component.literal("")
                    .withStyle(ChatFormatting.RED)
                    .append(Component.literal("--> Source: $source"))
            )
            player.sendSystemMessage(
                Component.literal("")
                    .withStyle(ChatFormatting.RED)
                    .append(Component.literal("--> Severity: $severity"))
            )
            player.sendSystemMessage(
                Component.literal("")
                    .withStyle(ChatFormatting.RED)
                    .append(Component.literal("--> Debug message by $origin"))
            )
        }
    }

    companion object {
        @JvmField
        val shaderErrors = arrayListOf<ShaderError>()
    }
}

fun sendShaderErrorInChat(source: String?, type: String?, severity: String?, message: String, origin: String) {
    ShaderError.shaderErrors.add(ShaderError(source, type, severity, message, origin))
}
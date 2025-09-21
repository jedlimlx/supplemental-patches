package io.github.jedlimlx.supplemental_patches.shaders

import io.github.jedlimlx.supplemental_patches.SupplementalPatches
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import kotlin.collections.ArrayList


class MinecraftError(override val message: String, val fileName: String): RuntimeException(message) {
    val index: Int
        get() {
            var count = 0
            while (stackTrace[count].fileName == "ErrorHandling.kt") count++

            return count
        }

    // formatting is as such
    // [ERROR]: <message>
    // --> Error found in <filename>, this file will not be loaded.
    // --> Thrown by <method>(<kotlin file>:<line number>)
    fun sendInChat(): Boolean {
        SupplementalPatches.LOGGER.warn(stackTrace.toList())

        val player = Minecraft.getInstance().player
        if (player != null) {
            player.sendSystemMessage(
                Component.literal("[ERROR]: $message")
                    .withStyle(ChatFormatting.RED)
                    .withStyle(ChatFormatting.BOLD)
            )
            player.sendSystemMessage(
                Component.literal("")
                    .withStyle(ChatFormatting.RED)
                    .append(Component.literal("--> Error found in "))
                    .append(Component.literal(fileName).withStyle(ChatFormatting.ITALIC))
                    .append(Component.literal(", this file will not be loaded."))
            )
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
            "--> Error found in $fileName, this file will not be loaded.\n" +
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

inline fun <K, V, R> Map<out K, V>.mapWithErrorHandling(crossinline transform: (Map.Entry<K, V>) -> R): List<R> {
    val lst = ArrayList<R>(size)
    forEachWithErrorHandling { lst.add(transform(it)) }
    return lst
}

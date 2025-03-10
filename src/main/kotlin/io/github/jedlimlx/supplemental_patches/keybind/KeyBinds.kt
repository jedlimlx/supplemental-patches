package io.github.jedlimlx.supplemental_patches.keybind

import com.mojang.blaze3d.platform.InputConstants
import io.github.jedlimlx.supplemental_patches.MODID
import io.github.jedlimlx.supplemental_patches.keybind.KeyBinds.KB_REGENERATE_SHADERS
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.RegisterKeyMappingsEvent
import net.minecraftforge.client.settings.KeyConflictContext
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import org.lwjgl.glfw.GLFW

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object KeyBinds {
    val KB_REGENERATE_SHADERS by lazy {
        KeyMapping(
            "key.supplemental_patches.reload_shaders",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_INSERT,
            "key.categories.misc"
        )
    }

    @SubscribeEvent
    fun registerBindings(event: RegisterKeyMappingsEvent) {
        event.register(KB_REGENERATE_SHADERS)
    }
}

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.FORGE, value = [Dist.CLIENT])
object KeyHandler {
    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase === TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
            if (KB_REGENERATE_SHADERS.isDown) {
                KB_REGENERATE_SHADERS.consumeClick()

                val player = Minecraft.getInstance().player ?: return
                player.sendSystemMessage(Component.nullToEmpty(installShader()))
            }
        }
    }
}
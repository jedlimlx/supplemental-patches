package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.minecraft.network.chat.Component
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent


object ServerEvents {
    fun init() {
        MinecraftForge.EVENT_BUS.register(ServerEvents)
    }

    @SubscribeEvent
    fun playerLoggedInEvent(event: ClientPlayerNetworkEvent.LoggingIn) {
        event.player.sendSystemMessage(Component.nullToEmpty(installShader()))
    }
}
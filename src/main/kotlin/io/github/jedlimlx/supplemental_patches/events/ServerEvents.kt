package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.minecraft.network.chat.Component
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent
import net.neoforged.neoforge.common.NeoForge


object ServerEvents {
    fun init() {
        NeoForge.EVENT_BUS.register(ServerEvents)
    }

    @SubscribeEvent
    fun playerLoggedInEvent(event: ClientPlayerNetworkEvent.LoggingIn) {
        event.player.sendSystemMessage(Component.nullToEmpty(installShader()))
    }
}
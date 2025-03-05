package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.minecraft.network.chat.Component
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.api.distmarker.OnlyIn
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.SubscribeEvent


@OnlyIn(Dist.CLIENT)
object ClientEvents {
    fun init() {
        MinecraftForge.EVENT_BUS.register(ClientEvents)
    }

    @SubscribeEvent
    fun playerLoggedInEvent(event: ClientPlayerNetworkEvent.LoggingIn) {
        event.player.sendSystemMessage(Component.nullToEmpty(installShader()))
    }
}
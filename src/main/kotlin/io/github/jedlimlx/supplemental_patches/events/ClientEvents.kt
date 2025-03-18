package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.MODID
import io.github.jedlimlx.supplemental_patches.SupplementalPatches
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.minecraft.client.Minecraft
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MODID, value = [Dist.CLIENT])
object ClientEvents {
    // TODO reload shaders after mods are loaded, if possible
    @SubscribeEvent
    fun textureStitchedEvent(event: TextureAtlasStitchedEvent) {
        val textureAtlas = Minecraft.getInstance().particleEngine.textureAtlas
        if (event.atlas.location() == textureAtlas.location())
            SupplementalPatches.LOGGER.info(installShader())
    }
}
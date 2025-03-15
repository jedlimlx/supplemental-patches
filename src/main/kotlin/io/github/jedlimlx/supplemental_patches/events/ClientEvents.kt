package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.MODID
import io.github.jedlimlx.supplemental_patches.SupplementalPatches
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.minecraft.client.Minecraft
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID, value = [Dist.CLIENT])
object ClientEvents {
    // TODO reload shaders after mods are loaded, if possible
    @SubscribeEvent
    fun textureStitchedEvent(event: TextureStitchEvent.Post) {
        val textureAtlas = Minecraft.getInstance().particleEngine.textureAtlas
        if (event.atlas.location() == textureAtlas.location())
            SupplementalPatches.LOGGER.info(installShader())
    }
}
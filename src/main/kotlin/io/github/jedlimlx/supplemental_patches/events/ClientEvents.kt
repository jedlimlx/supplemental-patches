package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.MODID
import io.github.jedlimlx.supplemental_patches.SupplementalPatches
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.irisshaders.iris.Iris
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.PackSource
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.TextureAtlasStitchedEvent
import net.neoforged.neoforge.event.AddPackFindersEvent

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MODID, value = [Dist.CLIENT])
object ClientEvents {
    @SubscribeEvent
    fun textureStitchedEvent(event: TextureAtlasStitchedEvent) {
        val textureAtlas = Minecraft.getInstance().particleEngine.textureAtlas
        if (event.atlas.location() == textureAtlas.location()) {
            val string = installShader()
            SupplementalPatches.LOGGER.info(string)
            Minecraft.getInstance().player?.sendSystemMessage(Component.nullToEmpty(string))
            Iris.loadShaderpackWhenPossible()
        }
    }

    @SubscribeEvent
    fun addBuiltInPacks(event: AddPackFindersEvent) {
        event.addPackFinders(
            ResourceLocation.parse("supplemental_patches:resourcepacks/builtin_shaders"),
            PackType.CLIENT_RESOURCES,
            Component.translatable("key.supplemental_patches.builtin_shaders"),
            PackSource.BUILT_IN,
            true,
            Pack.Position.TOP
        )
    }
}
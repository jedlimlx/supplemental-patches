package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.MODID
import io.github.jedlimlx.supplemental_patches.SupplementalPatches
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.irisshaders.iris.Iris
import net.minecraft.SharedConstants
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.PathPackResources
import net.minecraft.server.packs.repository.Pack
import net.minecraft.server.packs.repository.Pack.ResourcesSupplier
import net.minecraft.server.packs.repository.PackSource
import net.minecraft.server.packs.repository.RepositorySource
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.event.AddPackFindersEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.common.Mod
import java.util.function.Consumer

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID, value = [Dist.CLIENT])
object ClientEvents {
    @SubscribeEvent
    fun textureStitchedEvent(event: TextureStitchEvent.Post) {
        val textureAtlas = Minecraft.getInstance().particleEngine.textureAtlas
        if (event.atlas.location() == textureAtlas.location()) {
            SupplementalPatches.LOGGER.info(installShader())
            Iris.loadShaderpackWhenPossible()
        }
    }

    @SubscribeEvent
    fun addBuiltInPacks(event: AddPackFindersEvent) {
        val mod = ModList.get().getModFileById("supplemental_patches")
        val file = mod.file.findResource(*arrayOf("resourcepacks/builtin_shaders"))
        event.addRepositorySource { packConsumer: Consumer<Pack?>? ->
            packConsumer!!.accept(
                Pack.create(
                    "supplemental_patches:builtin_shaders",
                    Component.translatable("key.supplemental_patches.builtin_shaders"),
                    true,
                    { path: String? -> PathPackResources(path, file, true) },
                    Pack.Info(
                        Component.translatable("key.supplemental_patches.builtin_shaders_description"),
                        SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES),
                        FeatureFlagSet.of()
                    ),
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    false,
                    PackSource.BUILT_IN
                )
            )
        }
    }
}
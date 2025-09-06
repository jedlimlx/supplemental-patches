package io.github.jedlimlx.supplemental_patches

import com.mojang.blaze3d.platform.InputConstants
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW
import java.util.concurrent.Executor


object SupplementalPatches: ClientModInitializer {
    @JvmField
    val LOGGER: Logger = LogManager.getLogger("supplemental_patches")

    val KB_REGENERATE_SHADERS = KeyBindingHelper.registerKeyBinding(
        KeyMapping(
            "key.supplemental_patches.reload_shaders",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_INSERT,
            "key.categories.misc"
        )
    )

    override fun onInitializeClient() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
            .registerReloadListener(object : IdentifiableResourceReloadListener {
                override fun reload(
                    preparationBarrier: PreparationBarrier,
                    resourceManager: ResourceManager,
                    preparationsProfiler: ProfilerFiller,
                    reloadProfiler: ProfilerFiller,
                    backgroundExecutor: Executor,
                    gameExecutor: Executor
                ) = ShaderResourceLoader.reload(
                    preparationBarrier,
                    resourceManager,
                    preparationsProfiler,
                    reloadProfiler,
                    backgroundExecutor,
                    gameExecutor
                )

                override fun getFabricId() = ResourceLocation("supplemental_patches:euphoria")
            })

        TextureStitchEvent.EVENT.register(TextureStitchEvent {
            if (it.location() == Minecraft.getInstance().particleEngine.textureAtlas.location()) {
                val string = installShader()
                LOGGER.info(string)
                Minecraft.getInstance().player?.sendSystemMessage(Component.nullToEmpty(string))
            }
        })

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            if (KB_REGENERATE_SHADERS.isDown) {
                KB_REGENERATE_SHADERS.consumeClick()

                val player = Minecraft.getInstance().player
                player?.sendSystemMessage(Component.nullToEmpty(installShader()))
            }
        })

        FabricLoader.getInstance().getModContainer("supplemental_patches").ifPresent {
            ResourceManagerHelper.registerBuiltinResourcePack(
                ResourceLocation("supplemental_patches:builtin_shaders"),
                it,
                Component.translatable("key.supplemental_patches.builtin_shaders"),
                ResourcePackActivationType.DEFAULT_ENABLED
            )
        }
    }
}
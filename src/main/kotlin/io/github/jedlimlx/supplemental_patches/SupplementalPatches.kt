package io.github.jedlimlx.supplemental_patches

import com.mojang.blaze3d.platform.InputConstants
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.minecraft.client.KeyMapping
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import net.minecraft.world.entity.player.Player
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.lwjgl.glfw.GLFW
import software.bernie.geckolib.cache.GeckoLibCache
import java.util.concurrent.CompletableFuture
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

        ClientEntityEvents.ENTITY_LOAD.register(ClientEntityEvents.Load { entity, _ ->
            if (entity is Player) {
                entity.sendSystemMessage(Component.nullToEmpty(installShader()))
            }
        })

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            while (KB_REGENERATE_SHADERS.isDown) {
                KB_REGENERATE_SHADERS.consumeClick()

                val player = Minecraft.getInstance().player ?: break
                player.sendSystemMessage(Component.nullToEmpty(installShader()))
            }
        })
    }
}
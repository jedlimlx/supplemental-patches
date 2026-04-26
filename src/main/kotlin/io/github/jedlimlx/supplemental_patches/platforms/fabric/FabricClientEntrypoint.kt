package io.github.jedlimlx.supplemental_patches.platforms.fabric

//? fabric {
/*import com.mojang.blaze3d.platform.InputConstants
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint
import io.github.jedlimlx.supplemental_patches.LOGGER
import io.github.jedlimlx.supplemental_patches.PLATFORM
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
import net.minecraft.network.chat.Component
import net.minecraft.server.packs.PackType
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.profiling.ProfilerFiller
import org.lwjgl.glfw.GLFW
import java.util.concurrent.Executor

@Entrypoint("client")
class FabricClientEntrypoint : ClientModInitializer {
	val KB_REGENERATE_SHADERS = KeyBindingHelper.registerKeyBinding(
		KeyMapping(
			"key.supplemental_patches.reload_shaders",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_INSERT,
			KeyMapping.CATEGORY_MISC
		)
	)

	override fun onInitializeClient() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
			.registerReloadListener(object : IdentifiableResourceReloadListener {
				override fun reload(
					preparationBarrier: PreparableReloadListener.PreparationBarrier,
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

				override fun getFabricId() = PLATFORM.getResourceLocation("supplemental_patches:euphoria")
			})

		TextureStitchEvent.EVENT.register(TextureStitchEvent {
			if ("particles" in it.location().toString()) {
				PLATFORM.particleAtlas = it

				val string = installShader()
				LOGGER.info(string)
				PLATFORM.sendSystemMessage(string)
			}
		})

		ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
			if (KB_REGENERATE_SHADERS.isDown) {
				KB_REGENERATE_SHADERS.consumeClick()

				PLATFORM.sendSystemMessage(installShader())
			}
		})

		FabricLoader.getInstance().getModContainer("supplemental_patches").ifPresent {
			ResourceManagerHelper.registerBuiltinResourcePack(
				PLATFORM.getResourceLocation("supplemental_patches:builtin_shaders"),
				it,
				Component.translatable("key.supplemental_patches.builtin_shaders"),
				ResourcePackActivationType.DEFAULT_ENABLED
			)
		}
	}
} *///?}

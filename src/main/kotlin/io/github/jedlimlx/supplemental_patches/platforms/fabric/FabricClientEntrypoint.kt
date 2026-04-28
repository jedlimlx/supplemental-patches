package io.github.jedlimlx.supplemental_patches.platforms.fabric

//? fabric {
import com.mojang.blaze3d.platform.InputConstants
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint
import io.github.jedlimlx.supplemental_patches.LOGGER
import io.github.jedlimlx.supplemental_patches.PLATFORM
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader
import io.github.jedlimlx.supplemental_patches.shaders.installShader
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.ResourcePackActivationType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.KeyMapping
import net.minecraft.network.chat.Component
import net.minecraft.server.packs.PackType
import org.lwjgl.glfw.GLFW

//? >=1.21.9 {
/*import net.fabricmc.fabric.api.resource.v1.ResourceLoader
*///?} else {

import net.minecraft.resources.ResourceLocation
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener
import net.minecraft.server.packs.resources.PreparableReloadListener
import net.minecraft.server.packs.resources.ResourceManager
import java.util.concurrent.Executor
//?}


@Entrypoint("client")
class FabricClientEntrypoint : ClientModInitializer {
	val KB_REGENERATE_SHADERS = KeyBindingHelper.registerKeyBinding(
		KeyMapping(
			"key.supplemental_patches.reload_shaders",
			InputConstants.Type.KEYSYM,
			GLFW.GLFW_KEY_INSERT,
			//? >=1.21.9 {
			/*KeyMapping.Category.MISC
			*///?} else {
			KeyMapping.CATEGORY_MISC
			//?}
		)
	)

	override fun onInitializeClient() {
		//? >=1.21.9 {
		/*ResourceLoader.get(PackType.CLIENT_RESOURCES)
			.registerReloader(
				PLATFORM.getResourceLocation("supplemental_patches:euphoria")
			) { sharedState, backgroundExecutor, stage, gameExecutor ->
				ShaderResourceLoader.reload(
					stage,
					sharedState.resourceManager(),
					backgroundExecutor,
					gameExecutor
				)
			}
		*///?} else {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES)
			.registerReloadListener(object : IdentifiableResourceReloadListener {
				override fun reload(
					preparationBarrier: PreparableReloadListener.PreparationBarrier,
					resourceManager: ResourceManager,
					backgroundExecutor: Executor,
					gameExecutor: Executor
				) = ShaderResourceLoader.reload(
					preparationBarrier,
					resourceManager,
					backgroundExecutor,
					gameExecutor
				)

				override fun getFabricId() = PLATFORM.getResourceLocation("supplemental_patches:euphoria")
			})
		 //?}

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
} //?}

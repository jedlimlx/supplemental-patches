package io.github.jedlimlx.supplemental_patches.platforms.neoforge

//? neoforge {
import io.github.jedlimlx.supplemental_patches.LOGGER
import io.github.jedlimlx.supplemental_patches.MOD_ID
import io.github.jedlimlx.supplemental_patches.PLATFORM
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
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent

@EventBusSubscriber(modid = MOD_ID, value = [Dist.CLIENT])
object NeoForgeClientEvents {
	@SubscribeEvent
	@JvmStatic
	fun textureStitchedEvent(event: TextureAtlasStitchedEvent) {
		val textureAtlas = Minecraft.getInstance().particleEngine.textureAtlas
		if (event.atlas.location() == textureAtlas.location()) {
			PLATFORM.particleAtlas = textureAtlas

			val string = installShader()
			LOGGER.info(string)
			PLATFORM.sendSystemMessage(string)
			Iris.loadShaderpackWhenPossible()
		}
	}

	@SubscribeEvent
	@JvmStatic
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

	@SubscribeEvent
	@JvmStatic
	fun onPlayerJoin(event: EntityJoinLevelEvent) {
		if (event.level.isClientSide) {
			var message: Component?
			while (true) {
				message = PLATFORM.messageQueue.removeFirstOrNull()
				if (message != null)
					Minecraft.getInstance().player!!.sendSystemMessage(message)
				else break
			}
		}
	}
}
//?}

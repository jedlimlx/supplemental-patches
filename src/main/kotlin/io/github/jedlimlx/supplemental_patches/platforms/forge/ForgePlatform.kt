package io.github.jedlimlx.supplemental_patches.platforms.forge

//? forge {
/*import io.github.jedlimlx.supplemental_patches.LOGGER
import io.github.jedlimlx.supplemental_patches.platforms.Platform
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.FMLPaths
import java.nio.file.Path

object ForgePlatform : Platform {
	override val loader = Platform.ModLoader.FORGE
	override val mcVersion = "1.20.1"
	override val shaderDirectory: Path = FMLPaths.GAMEDIR.get().resolve("shaderpacks")
	override val isDevelopmentEnvironment: Boolean = false

	override var particleAtlas: TextureAtlas? = null
	override val particleAtlasTextures: Collection<Identifier>
		get() = particleAtlas!!.textureLocations

	override val messageQueue: ArrayDeque<Component> = ArrayDeque()

	override fun modList(): List<String> = ModList.get().mods.map { it.modId }
	override fun isModLoaded(modId: String) = ModList.get().isLoaded(modId)

	override fun sendSystemMessage(message: String) {
		val player = Minecraft.getInstance().player
		if (player != null) {
			player.sendSystemMessage(Component.nullToEmpty(message))
		} else messageQueue.add(Component.nullToEmpty(message))
	}
	override fun sendSystemMessage(message: Component) {
		val player = Minecraft.getInstance().player
		if (player != null) {
			player.sendSystemMessage(message)
		} else messageQueue.add(message)
	}
	override fun getIdentifier(path: String) = Identifier.parse(path)
	override fun getIdentifier(namespace: String, path: String) = Identifier.fromNamespaceAndPath(namespace,path)
}
*///?}

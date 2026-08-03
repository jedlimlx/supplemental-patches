package io.github.jedlimlx.supplemental_patches.platforms.fabric

//? fabric {
import io.github.jedlimlx.supplemental_patches.platforms.Platform
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import java.nio.file.Path

object FabricPlatform : Platform {
	override val loader = Platform.ModLoader.FABRIC
	override val mcVersion: String
		get() = FabricLoader.getInstance().rawGameVersion
	override val isDevelopmentEnvironment: Boolean = false
	override val shaderDirectory: Path = FabricLoader.getInstance().gameDir.resolve("shaderpacks")

	override var particleAtlas: TextureAtlas? = null
	override val particleAtlasTextures: Collection<Identifier>
		get() = particleAtlas!!.texturesByName.keys

	override val messageQueue: ArrayDeque<Component> = ArrayDeque()

	override fun modList(): List<String> = FabricLoader.getInstance().allMods.map { it.metadata.id }
	override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)

	override fun sendSystemMessage(message: String) {
		val player = Minecraft.getInstance().player
		if (player != null) {
			//? >=26.1 {
			player.sendSystemMessage(Component.nullToEmpty(message));
			//?} >=1.21.4 {
			/*player.displayClientMessage(Component.nullToEmpty(message), false)
			*///?} else {
			/*player.sendSystemMessage(Component.nullToEmpty(message))
			*///?}
		} else messageQueue.add(Component.nullToEmpty(message))
	}
	override fun sendSystemMessage(message: Component) {
		val player = Minecraft.getInstance().player
		if (player != null) {
			//? >=26.1 {
			player.sendSystemMessage(message)
			//?} >=1.21.4 {
			/*player.displayClientMessage(message, false)
			*///?} else {
			/*player.sendSystemMessage(message)
			*///?}
		} else messageQueue.add(message)
	}

	override fun getIdentifier(path: String): Identifier {
		//? if =1.20.1 {
		/*return Identifier(path)
		*///?} elif >=1.21{
		return Identifier.parse(path)
		//?}
	}

	override fun getIdentifier(namespace: String, path: String): Identifier {
		//? if =1.20.1 {
		/*return Identifier(namespace, path)
		*///?} elif >=1.21{
		return Identifier.fromNamespaceAndPath(namespace, path)
		//?}
	}
} //?}

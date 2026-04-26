package io.github.jedlimlx.supplemental_patches.platforms.fabric

//? fabric {
/*import io.github.jedlimlx.supplemental_patches.platforms.Platform
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import java.nio.file.Path

object FabricPlatform : Platform {
	override val loader = Platform.ModLoader.FABRIC
	override val mcVersion: String
		get() = FabricLoader.getInstance().rawGameVersion
	override val isDevelopmentEnvironment: Boolean = false
	override val shaderDirectory: Path = FabricLoader.getInstance().gameDir.resolve("shaderpacks")

	override var particleAtlas: TextureAtlas? = null
	override val particleAtlasTextures: Collection<ResourceLocation>
		get() = particleAtlas!!.texturesByName.keys

	override fun modList(): List<String> = FabricLoader.getInstance().allMods.map { it.metadata.id }
	override fun isModLoaded(modId: String) = FabricLoader.getInstance().isModLoaded(modId)

	override fun sendSystemMessage(message: String) {
		Minecraft.getInstance().player?.sendSystemMessage(Component.nullToEmpty(message))
	}
	override fun sendSystemMessage(message: Component) {
		Minecraft.getInstance().player?.sendSystemMessage(message)
	}

	override fun getResourceLocation(path: String): ResourceLocation {
		//? if =1.20.1 {
		/*return ResourceLocation(path)
		*///?} elif >=1.21{
		return ResourceLocation.parse(path)
		//?}
	}

	override fun getResourceLocation(namespace: String, path: String): ResourceLocation {
		//? if =1.20.1 {
		/*return ResourceLocation(namespace, path)
		*///?} elif >=1.21{
		return ResourceLocation.fromNamespaceAndPath(namespace, path)
		//?}
	}
} *///?}

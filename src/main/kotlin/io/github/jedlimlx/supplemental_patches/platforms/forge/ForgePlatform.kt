package io.github.jedlimlx.supplemental_patches.platforms.forge

//? forge {
/*import io.github.jedlimlx.supplemental_patches.platforms.Platform
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.FMLPaths
import java.nio.file.Path

object ForgePlatform : Platform {
	override val loader = Platform.ModLoader.FORGE
	override val mcVersion = "1.20.1"
	override val shaderDirectory: Path = FMLPaths.GAMEDIR.get().resolve("shaderpacks")
	override val isDevelopmentEnvironment: Boolean = false

	override var particleAtlas: TextureAtlas? = null
	override val particleAtlasTextures: Collection<ResourceLocation>
		get() = particleAtlas!!.textureLocations

	override fun modList(): List<String> = ModList.get().mods.map { it.modId }
	override fun isModLoaded(modId: String) = ModList.get().isLoaded(modId)

	override fun sendSystemMessage(message: String) {
		Minecraft.getInstance().player?.sendSystemMessage(Component.nullToEmpty(message))
	}
	override fun sendSystemMessage(message: Component) {
		Minecraft.getInstance().player?.sendSystemMessage(message)
	}

	override fun getResourceLocation(path: String) = ResourceLocation.parse(path)
	override fun getResourceLocation(namespace: String, path: String) = ResourceLocation.fromNamespaceAndPath(namespace,path)
}
*///?}

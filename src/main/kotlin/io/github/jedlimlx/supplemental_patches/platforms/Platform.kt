package io.github.jedlimlx.supplemental_patches.platforms

import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import java.nio.file.Path

interface Platform {
    val loader: ModLoader
	val mcVersion: String
	val shaderDirectory: Path
	val isDevelopmentEnvironment: Boolean

	var particleAtlas: TextureAtlas?
	val particleAtlasTextures: Collection<ResourceLocation>

	val isDebug: Boolean
		get() = this.isDevelopmentEnvironment

	enum class ModLoader {
        FABRIC, NEOFORGE, FORGE, QUILT
    }

	fun modList(): List<String>
	fun isModLoaded(modId: String): Boolean

	fun sendSystemMessage(message: String)
	fun sendSystemMessage(message: Component)

	fun getResourceLocation(path: String): ResourceLocation
	fun getResourceLocation(namespace:String, path: String): ResourceLocation
}

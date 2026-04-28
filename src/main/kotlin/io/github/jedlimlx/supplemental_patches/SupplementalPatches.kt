package io.github.jedlimlx.supplemental_patches

import org.slf4j.LoggerFactory
import org.slf4j.Logger
import io.github.jedlimlx.supplemental_patches.platforms.Platform

//? fabric {
/*import io.github.jedlimlx.supplemental_patches.platforms.fabric.FabricPlatform
*///?} neoforge {
import io.github.jedlimlx.supplemental_patches.platforms.neoforge.NeoForgePlatform
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.loading.FMLEnvironment
//?} forge {
/*import io.github.jedlimlx.supplemental_patches.platforms.forge.ForgePlatform
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.FMLEnvironment
*///?}

const val MOD_ID: String =  /*$ mod_id*/ "supplemental_patches"
const val MOD_VERSION: String =  /*$ mod_version*/ "1.0.0-beta"
const val MOD_FRIENDLY_NAME: String =  /*$ mod_name*/ "Supplemental Patches"

@JvmField
val LOGGER: Logger = LoggerFactory.getLogger("supplemental_patches")

@JvmField
val PLATFORM: Platform
//? fabric {
/*= FabricPlatform
*///?} neoforge {
= NeoForgePlatform
//?} forge {
/*= ForgePlatform
*///?}

//? neoforge || forge {
@Mod(MOD_ID)
class SupplementalPatches {
	init {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			ShaderResourceLoader.registerListener()
		}
	}
}
//?}

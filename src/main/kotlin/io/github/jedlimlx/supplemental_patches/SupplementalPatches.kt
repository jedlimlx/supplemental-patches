package io.github.jedlimlx.supplemental_patches

import io.github.jedlimlx.supplemental_patches.events.ClientEvents
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader
import net.minecraft.resources.ResourceLocation.fromNamespaceAndPath
import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.fml.loading.FMLEnvironment
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


const val MODID = "supplemental_patches"

fun res(name: String) = fromNamespaceAndPath(MODID, name)

@Mod(MODID)
object SupplementalPatches {
    @JvmField
    val LOGGER: Logger = LogManager.getLogger("supplemental_patches")

    init {
        ClientEvents.init()

        if (FMLEnvironment.dist == Dist.CLIENT)
            ShaderResourceLoader.registerListener()
    }
}
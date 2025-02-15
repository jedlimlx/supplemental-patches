package io.github.jedlimlx.supplemental_patches

import io.github.jedlimlx.supplemental_patches.events.ServerEvents
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.common.Mod
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


const val MODID = "supplemental_patches"

fun res(name: String) = ResourceLocation(MODID, name)

@Mod(MODID)
object SupplementalPatches {
    @JvmField
    val LOGGER: Logger = LogManager.getLogger("supplemental_patches")

    init {
        ServerEvents.init()

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT) { Runnable { ShaderResourceLoader.registerListener() } }
    }
}
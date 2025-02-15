package io.github.jedlimlx.supplemental_patches.events

import io.github.jedlimlx.supplemental_patches.MODID
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = [Dist.CLIENT])
object ClientEvents {
}
package io.github.jedlimlx.supplemental_patches.mixins.biomes;

import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.juni.wetland_whimsy.datagen.registries.WetlandWhimsyBiomes;

@Mixin(value = WetlandWhimsyBiomes.class, remap = false)
public class WetlandWhimsyMixin {
    @Unique
    private static int currentId = BiomeUniforms.getBiomeMap().size();

    @Inject(method = "createKey", at = @At("TAIL"))
    private static void registerBiome(String string, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), currentId++);
    }
}
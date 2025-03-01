package io.github.jedlimlx.supplemental_patches.mixins.biomes;

import com.yungnickyoung.minecraft.yungscavebiomes.module.BiomeModule;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BiomeModule.class, remap = false)
public class YungsCaveBiomesMixin {
    @Unique
    private static int currentId = BiomeUniforms.getBiomeMap().size();

    @Inject(method = "register", at = @At("TAIL"))
    private static void registerBiome(String string, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), currentId++);
    }
}
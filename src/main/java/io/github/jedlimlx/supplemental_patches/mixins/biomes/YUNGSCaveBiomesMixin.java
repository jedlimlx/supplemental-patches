package io.github.jedlimlx.supplemental_patches.mixins.biomes;

//? =1.20.1 || =1.21.1 || ~26.1 {
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.yungnickyoung.minecraft.yungscavebiomes.module.BiomeModule;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded(value = "yungscavebiomes")
@Mixin(value = BiomeModule.class, remap = false)
public class YUNGSCaveBiomesMixin {
    @Inject(method = "register", at = @At("TAIL"))
    private static void registerBiome(String string, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), BiomeUniforms.getBiomeMap().size() + 1);
    }
}
//?}

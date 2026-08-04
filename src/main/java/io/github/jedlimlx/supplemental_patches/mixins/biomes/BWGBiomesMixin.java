package io.github.jedlimlx.supplemental_patches.mixins.biomes;

//? <26.1 {
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.potionstudios.biomeswevegone.world.level.levelgen.biome.BWGBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded(value = "biomeswevegone")
@Mixin(value = BWGBiomes.class, remap = false)
public class BWGBiomesMixin {
    @Inject(method = "createBiome", at = @At("TAIL"))
    private static void createBiome(String id, BWGBiomes.BiomeFactory biomeFactory, TagKey<Biome>[] tags, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), BiomeUniforms.getBiomeMap().size() + 1);
    }
}
//?}

package io.github.jedlimlx.supplemental_patches.mixins.biomes;

//? forge {
/*import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.bon.soulfulnether.worldgen.SoulfulBiomes;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@IfModLoaded(value = "soufulnether")
@Mixin(value = SoulfulBiomes.class, remap = false)
public class SoulfulNetherMixin {
    @Inject(method = "register", at = @At("TAIL"))
    private static void registerBiome(String string, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), BiomeUniforms.getBiomeMap().size() + 1);
    }
}
*///?}

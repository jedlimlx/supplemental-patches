package io.github.jedlimlx.supplemental_patches.mixins.biomes;

//? neoforge || forge {
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? neoforge {
import com.teamabnormals.environmental.core.registry.datapack.EnvironmentalBiomes;
 //?} forge {
/*import com.teamabnormals.environmental.core.registry.EnvironmentalBiomes;
*///?}

@IfModLoaded(value = "environmental")
@IfMinecraftVersion(minVersion = "1.20.1", maxVersion = "1.21.1")
@Mixin(value = EnvironmentalBiomes.class, remap = false)
public class EnvironmentalMixin {
	//? neoforge {
	@Inject(method = "create", at = @At("TAIL"))
	 //?} forge {
	/*@Inject(method = "createKey", at = @At("TAIL"))
	*///?}
    private static void registerBiome(String string, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), BiomeUniforms.getBiomeMap().size() + 1);
    }
}
//?}

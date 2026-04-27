package io.github.jedlimlx.supplemental_patches.mixins.biomes;

//? neoforge || forge {
/*import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? neoforge {
/^import com.teamabnormals.atmospheric.core.registry.datapack.AtmosphericBiomes;
^///?} forge {
/^import com.teamabnormals.atmospheric.core.registry.AtmosphericBiomes;
^///?}

@IfModLoaded(value = "atmospheric")
@IfMinecraftVersion(minVersion = "1.20.1", maxVersion = "1.21.1")
@Mixin(value = AtmosphericBiomes.class, remap = false)
public class AtmosphericMixin {
    @Inject(method = "createKey", at = @At("TAIL"))
    private static void registerBiome(String string, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), BiomeUniforms.getBiomeMap().size() + 1);
    }
}
*///?}

package io.github.jedlimlx.supplemental_patches.mixins.biomes;

//? (neoforge || fabric) && >=1.21.1 {
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//? >=26.1 {
/*import net.penumbra.enderscape.registry.level.EnderscapeBiomes;
*///?} else {
import net.bunten.enderscape.registry.EnderscapeBiomes;
//?}

@IfModLoaded(value = "enderscape")
@IfMinecraftVersion(minVersion = "1.21.1")
@Mixin(value = EnderscapeBiomes.class, remap = false)
public class EnderscapeBiomesMixin {
    @Inject(method = "register", at = @At("TAIL"))
    private static void registerBiome(String string, CallbackInfoReturnable<ResourceKey<Biome>> cir) {
        BiomeUniforms.getBiomeMap().put(cir.getReturnValue(), BiomeUniforms.getBiomeMap().size() + 1);
    }
}
//?}

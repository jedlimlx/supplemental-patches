package io.github.jedlimlx.supplemental_patches.mixins;

import io.github.jedlimlx.supplemental_patches.shaders.BiomeUniformsKt;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

@Mixin(value = BiomeUniforms.class, remap = false)
public class BiomeUniformsMixin {
    @Shadow
    static IntSupplier playerI(ToIntFunction<LocalPlayer> function) {
        return () -> 0;
    }

    @Inject(
        method = "addBiomeUniforms",
        at = @At("HEAD")
    )
    private static void addBiomeUniforms(UniformHolder uniforms, CallbackInfo ci) {
        uniforms.uniform1i(
            UniformUpdateFrequency.PER_TICK,
            "moddedBiome",
            playerI((player) -> BiomeUniformsKt.get_biomeMap().getInt(player.level().getBiome(player.blockPosition()).unwrapKey().orElse(null)))
        );
    }
}

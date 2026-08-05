package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? fabric && < 26.2 {
/*//~ if >=26.1 'server' -> 'world'
import com.github.thedeathlycow.scorchful.world.Sandstorms;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
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

@IfModLoaded(value = "scorchful")
@Mixin(value = BiomeUniforms.class, remap = false)
public class ScorchfulSandstormUniforms {
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
            "scorchfulSandstorm",
            playerI(
                (player) -> {
                    if (Sandstorms.hasRegularSandStorms(player.level().getBiome(player.blockPosition()))) return 1;
                    else if (Sandstorms.hasRedSandStorms(player.level().getBiome(player.blockPosition()))) return 2;
                    return 0;
                }
            )
        );
    }
}
*///?}

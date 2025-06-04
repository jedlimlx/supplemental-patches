package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.bunten.enderscape.biome.util.SkyParameters;
import net.irisshaders.iris.gl.uniform.FloatSupplier;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.ToFloatFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Optional;
import java.util.function.IntSupplier;
import java.util.function.ToIntFunction;

import static net.bunten.enderscape.registry.EnderscapeBiomes.DEFAULT_NEBULA_ALPHA;
import static net.bunten.enderscape.registry.EnderscapeBiomes.DEFAULT_NEBULA_COLOR;
@Restriction(require = @Condition("enderscape"))
@Mixin(value = BiomeUniforms.class, remap = false)
public abstract class EnderscapeAtmosphereUniforms {
    @Shadow
    static IntSupplier playerI(ToIntFunction<LocalPlayer> function) { return () -> 0; }

    private static Color getNebulaColor(LocalPlayer player) {
        Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
        return temp.map(skyParameters -> new Color(skyParameters.nebulaColor())).orElse(new Color(DEFAULT_NEBULA_COLOR));
    }

    @Inject(
        method = "addBiomeUniforms",
        at = @At("TAIL"),
        remap = false
    )
    private static void addBiomeUniforms(UniformHolder uniforms, CallbackInfo ci) {
        uniforms.uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaRed",
            playerI(
                (player) -> getNebulaColor(player).getRed()
            )
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaGreen",
            playerI(
                (player) -> getNebulaColor(player).getGreen()
            )
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaBlue",
            playerI(
                (player) -> getNebulaColor(player).getBlue()
            )
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaAlpha",
            () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
                    return temp.map(SkyParameters::nebulaAlpha).orElse(DEFAULT_NEBULA_ALPHA);
                } else return DEFAULT_NEBULA_ALPHA;
            }
        );
    }
}

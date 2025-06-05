package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.bunten.enderscape.biome.util.SkyParameters;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Optional;

import static net.bunten.enderscape.registry.EnderscapeBiomes.DEFAULT_NEBULA_ALPHA;
import static net.bunten.enderscape.registry.EnderscapeBiomes.DEFAULT_NEBULA_COLOR;
@Restriction(require = @Condition("enderscape"))
@Mixin(value = BiomeUniforms.class, remap = false)
public abstract class EnderscapeAtmosphereUniforms {
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
        uniforms.uniform3f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaColor",
            () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                Color color;
                if (player != null) color = getNebulaColor(player);
                else color = new Color(DEFAULT_NEBULA_COLOR);
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
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

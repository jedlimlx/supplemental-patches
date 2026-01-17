package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.bunten.enderscape.registry.EnderscapeEnvironmentAttributes;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.uniforms.BiomeUniforms;
import net.minecraft.client.Minecraft;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Restriction(require = @Condition("enderscape"))
@Mixin(value = BiomeUniforms.class, remap = false)
public abstract class EnderscapeAtmosphereUniforms {
    private static Color getNebulaColor() {
        EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
        return new Color(probe.getValue(EnderscapeEnvironmentAttributes.NEBULA_COLOR, 1.0f));
    }

    private static Color getFlashColor() {
        EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
        return new Color(probe.getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, 1.0f));
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
                Color color = getNebulaColor();
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
        ).uniform3f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeFlashColor",
            () -> {
                Color color = getFlashColor();
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaAlpha",
            () -> {
                EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
                return probe.getValue(EnderscapeEnvironmentAttributes.NEBULA_ALPHA, 1.0f);
            }
        );
    }
}

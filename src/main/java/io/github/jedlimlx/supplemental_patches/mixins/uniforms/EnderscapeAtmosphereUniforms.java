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
                EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
                Color color = new Color(probe.getValue(EnderscapeEnvironmentAttributes.NEBULA_COLOR, 1.0f));
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
        ).uniform3f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeFlashColor",
            () -> {
                EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
                Color color = new Color(probe.getValue(EnvironmentAttributes.SKY_LIGHT_COLOR, 1.0f));
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaAlpha",
            () -> {
                EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
                return probe.getValue(EnderscapeEnvironmentAttributes.NEBULA_ALPHA, 1.0f);
            }
        ).uniform3f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeStarColor",
            () -> {
                EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
                Color color = new Color(probe.getValue(EnderscapeEnvironmentAttributes.STAR_COLOR, 1.0f));
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeStarAlpha",
            () -> {
                EnvironmentAttributeProbe probe = Minecraft.getInstance().gameRenderer.getMainCamera().attributeProbe();
                return probe.getValue(EnderscapeEnvironmentAttributes.STAR_ALPHA, 1.0f);
            }
        );
    }
}

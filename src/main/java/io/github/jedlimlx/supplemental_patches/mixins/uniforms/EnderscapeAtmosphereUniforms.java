package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? (neoforge || fabric) && >=1.21.1 {
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
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

import static net.bunten.enderscape.registry.EnderscapeBiomes.*;

//? neoforge && 1.21.1 {
import net.bunten.enderscape.biome.util.SkyParameters;
 //?} fabric && <=1.21.10 {
/*import net.bunten.enderscape.biome.util.BiomeParameters;
*///?} else {
/*import net.bunten.enderscape.registry.EnderscapeEnvironmentAttributes;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
*///?}

//~ if neoforge 'BiomeParameters' -> 'SkyParameters' {
//~ if neoforge 'findFor' -> 'getSkyParametersFor' {
@IfModLoaded(value = "enderscape")
@IfMinecraftVersion(minVersion = "1.21.1")
@Mixin(value = BiomeUniforms.class, remap = false)
public abstract class EnderscapeAtmosphereUniforms {
    @Inject(
        method = "addBiomeUniforms",
        at = @At("TAIL"),
        remap = false
    )
    private static void addBiomeUniforms(UniformHolder uniforms, CallbackInfo ci) {
		//? >=1.21.11 {
		/*uniforms.uniform3f(
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
		*///?} else {
        uniforms.uniform3f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeNebulaColor",
            () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                Color color;
                if (player != null) {
					Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
					color = temp.map(it -> new Color(it.nebulaColor())).orElse(new Color(DEFAULT_NEBULA_COLOR));
				} else color = new Color(DEFAULT_NEBULA_COLOR);
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
        ).uniform3f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeStarColor",
            () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                Color color;
                if (player != null) {
					Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
					color = temp.map(it -> new Color(it.starColor())).orElse(new Color(DEFAULT_STAR_COLOR));
				} else color = new Color(DEFAULT_STAR_COLOR);
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeStarAlpha",
            () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
					Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
					return temp.map(SkyParameters::starAlpha).orElse(DEFAULT_STAR_ALPHA);
                } else return DEFAULT_STAR_ALPHA;
            }
        );
		//?}
    }
}
//~}
//~}
//?}

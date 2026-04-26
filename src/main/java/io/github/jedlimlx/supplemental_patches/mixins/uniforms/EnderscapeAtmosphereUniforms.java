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

//? neoforge {
import net.bunten.enderscape.biome.util.SkyParameters;
 //?} fabric {
/*import net.bunten.enderscape.biome.util.BiomeParameters;
*///?}

@IfModLoaded(value = "enderscape")
@IfMinecraftVersion(minVersion = "1.21.1")
@Mixin(value = BiomeUniforms.class, remap = false)
public abstract class EnderscapeAtmosphereUniforms {
    private static Color getNebulaColor(LocalPlayer player) {
		//? neoforge {
		Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
		 //?} fabric {
		/*Optional<BiomeParameters> temp = BiomeParameters.findFor(player.level().getBiome(player.blockPosition()));
		*///?}
        return temp.map(it -> new Color(it.nebulaColor())).orElse(new Color(DEFAULT_NEBULA_COLOR));
    }

    private static Color getStarColor(LocalPlayer player) {
		//? neoforge {
        Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
		//?} fabric {
		/*Optional<BiomeParameters> temp = BiomeParameters.findFor(player.level().getBiome(player.blockPosition()));
		*///?}
        return temp.map(it -> new Color(it.starColor())).orElse(new Color(DEFAULT_STAR_COLOR));
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
					//? neoforge {
					Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
                    return temp.map(SkyParameters::nebulaAlpha).orElse(DEFAULT_NEBULA_ALPHA);
					 //?} fabric {
					/*Optional<BiomeParameters> temp = BiomeParameters.findFor(player.level().getBiome(player.blockPosition()));
					return temp.map(BiomeParameters::nebulaAlpha).orElse(DEFAULT_NEBULA_ALPHA);
					*///?}
                } else return DEFAULT_NEBULA_ALPHA;
            }
        ).uniform3f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeStarColor",
            () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                Color color;
                if (player != null) color = getStarColor(player);
                else color = new Color(DEFAULT_STAR_COLOR);
                return new Vector3f(color.getRed(), color.getGreen(), color.getBlue());
            }
        ).uniform1f(
            UniformUpdateFrequency.PER_TICK,
            "enderscapeStarAlpha",
            () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
					//? neoforge {
					Optional<SkyParameters> temp = SkyParameters.getSkyParametersFor(player.level().getBiome(player.blockPosition()));
                    return temp.map(SkyParameters::starAlpha).orElse(DEFAULT_STAR_ALPHA);
					 //?} fabric {
					/*Optional<BiomeParameters> temp = BiomeParameters.findFor(player.level().getBiome(player.blockPosition()));
					return temp.map(BiomeParameters::starAlpha).orElse(DEFAULT_STAR_ALPHA);
					*///?}
                } else return DEFAULT_STAR_ALPHA;
            }
        );
    }
}
//?}

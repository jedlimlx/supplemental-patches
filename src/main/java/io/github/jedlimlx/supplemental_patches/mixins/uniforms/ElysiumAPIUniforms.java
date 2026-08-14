package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? neoforge {
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.shaderpack.properties.PackDirectives;
import net.irisshaders.iris.uniforms.CommonUniforms;
import net.irisshaders.iris.uniforms.FrameUpdateNotifier;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.tuple.Triple;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.jadenxgamer.elysium_api.ElysiumAPI;

@IfModLoaded(value = "elysium_api")
@IfMinecraftVersion(minVersion = "1.21.1", maxVersion="1.21.1")
@Mixin(CommonUniforms.class)
public class ElysiumAPIUniforms {
	@Inject(
		method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
		at = @At("TAIL"),
		remap = false
	)
	private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
		uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "elysiumAmbientBrightness", ElysiumAPIUniforms::getAmbientBrightness);
	}

	private static float getAmbientBrightness() {
		Player player = Minecraft.getInstance().player;
		if (player != null) {
			Triple<Vector3f, Vector3f, Float> settings = ElysiumAPI.LIGHTMAP_SETTINGS.getSettings(player);
			return settings.getRight();
		} else {
			return 0.0f;
		}
	}
}
//?}

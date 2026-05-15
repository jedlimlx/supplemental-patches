package io.github.jedlimlx.supplemental_patches.mixins;

import net.irisshaders.iris.uniforms.CapturedRenderingState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapturedRenderingState.class)
public class CapturedRenderingStateMixin {
	@Inject(
		method = "setCurrentBlockEntity",
		at = @At("RETURN"),
		remap = false
	)
	public void setCurrentBlockEntity(int entity, CallbackInfo ci) {

	}
}

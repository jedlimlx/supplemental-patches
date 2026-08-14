//package io.github.jedlimlx.supplemental_patches.mixins.fixes;
//
//import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
//import com.moulberry.mixinconstraints.annotations.IfModLoaded;
//import net.jadenxgamer.netherexp.client.rendering.JNERenderStateShard;
//import net.minecraft.client.renderer.GameRenderer;
//import net.minecraft.client.renderer.ShaderInstance;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//@IfModLoaded(value = "netherexp")
//@IfMinecraftVersion(minVersion = "1.21.1", maxVersion = "1.21.1")
//@Mixin(value = JNERenderStateShard.class, remap = false)
//public class JNERenderStateShardMixin {
//	@Inject(
//		method = "getRenderTypeEntityAdditive",
//		at = @At("RETURN"),
//		cancellable = true
//	)
//	private static void getRenderTypeEntityAdditive(CallbackInfoReturnable<ShaderInstance> cir) {
//		cir.setReturnValue(GameRenderer.getRendertypeEntityAlphaShader());
//	}
//
//	@Inject(
//		method = "getRenderTypeNoShadeEntityCutout",
//		at = @At("RETURN"),
//		cancellable = true
//	)
//	private static void getRenderTypeNoShadeEntityCutout(CallbackInfoReturnable<ShaderInstance> cir) {
//		cir.setReturnValue(GameRenderer.getRendertypeEntityCutoutNoCullShader());
//	}
//
//	@Inject(
//		method = "getRenderTypeNoShadeEntityCutoutNoCull",
//		at = @At("RETURN"),
//		cancellable = true
//	)
//	private static void getRenderTypeNoShadeEntityCutoutNoCull(CallbackInfoReturnable<ShaderInstance> cir) {
//		cir.setReturnValue(GameRenderer.getRendertypeEntityCutoutNoCullShader());
//	}
//}

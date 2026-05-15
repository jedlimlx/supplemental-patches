package io.github.jedlimlx.supplemental_patches.mixins.fixes;

import com.bawnorton.mixinsquared.TargetHandler;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.client.resources.model.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(value = DecoratedPotRenderer.class, priority = 1500)
public class DecoratedPotRendererMixinMixin {
	@TargetHandler(
		mixin = "com.teamabnormals.clayworks.core.mixin.DecoratedPotRendererMixin",
		name = "render(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
	)
	@Redirect(
		method = "@MixinSquared:Handler",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
		)
	)
	private VertexConsumer replaceRenderType(Material instance, MultiBufferSource buffer, Function function) {
		return instance.buffer(buffer, RenderType::entitySmoothCutout);
	}
}

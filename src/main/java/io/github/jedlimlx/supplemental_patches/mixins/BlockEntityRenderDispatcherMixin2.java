package io.github.jedlimlx.supplemental_patches.mixins;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.jedlimlx.supplemental_patches.SupplementalPatchesKt;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.irisshaders.iris.layer.BlockEntityRenderStateShard;
import net.irisshaders.iris.layer.BufferSourceWrapper;
import net.irisshaders.iris.layer.OuterWrappedRenderType;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.uniforms.CapturedRenderingState;
import net.irisshaders.iris.vertices.ImmediateState;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BlockEntityRenderDispatcher.class, priority = 10000)
public class BlockEntityRenderDispatcherMixin2 {
	private static final String RUN_REPORTED =
		"Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;tryRender(Lnet/minecraft/world/level/block/entity/BlockEntity;Ljava/lang/Runnable;)V";

	@Inject(method = "render", at = @At(value = "INVOKE", target = RUN_REPORTED, shift = At.Shift.AFTER))
	private void iris$afterRender(BlockEntity blockEntity, float tickDelta, PoseStack matrix,
								  MultiBufferSource bufferSource, CallbackInfo ci) {
//		if (blockEntity instanceof WallLanternBlockTile lantern) {
//			Object2IntMap<BlockState> blockStateIds = WorldRenderingSettings.INSTANCE.getBlockStateIds();
//			int intId = blockStateIds.getOrDefault(lantern.getHeldBlock().getBlock().defaultBlockState(), -1);
//			CapturedRenderingState.INSTANCE.setCurrentBlockEntity(intId);
//		}
//
//		SupplementalPatchesKt.LOGGER.info("a " + CapturedRenderingState.INSTANCE.getCurrentRenderedBlockEntity());
	}
}

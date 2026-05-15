package io.github.jedlimlx.supplemental_patches.mixins;
import io.github.jedlimlx.supplemental_patches.SupplementalPatchesKt;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.irisshaders.iris.layer.BlockEntityRenderStateShard;
import net.irisshaders.iris.layer.BufferSourceWrapper;
import net.irisshaders.iris.layer.OuterWrappedRenderType;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.uniforms.CapturedRenderingState;
import net.irisshaders.iris.vertices.ImmediateState;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FrameBlockTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = BlockEntityRenderDispatcher.class, priority = 10000)
public class BlockEntityRenderDispatcherMixin {
	@ModifyVariable(
		method = "render",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/entity/BlockEntityType;isValid(Lnet/minecraft/world/level/block/state/BlockState;)Z"
		), allow = 1, require = 1, argsOnly = true
	)
	private MultiBufferSource iris$wrapBufferSource(MultiBufferSource bufferSource, BlockEntity blockEntity) {
		Object2IntMap<BlockState> blockStateIds = WorldRenderingSettings.INSTANCE.getBlockStateIds();
		if (blockStateIds == null || !ImmediateState.isRenderingLevel) {
			return bufferSource;
		}

		int intId;
		if (blockEntity instanceof WallLanternBlockTile lantern) {
			intId = blockStateIds.getOrDefault(lantern.getHeldBlock().getBlock().defaultBlockState(), -1);
		} else if (blockEntity instanceof CandleSkullBlockTile skull) {
			intId = blockStateIds.getOrDefault(
				skull.getCandle().getBlock().defaultBlockState().setValue(BlockStateProperties.LIT, true),
				-1
			);
		} else if (blockEntity instanceof LiquidCauldronBlockTile cauldron) {
			//intId = cauldron.getSoftFluidTank().getFluid()
			intId = -1;
		} else {
			BlockState state = blockEntity.getBlockState();
			intId = blockStateIds.getOrDefault(state, -1);
		}

		CapturedRenderingState.INSTANCE.setCurrentBlockEntity(intId);

		return new BufferSourceWrapper(
			((BufferSourceWrapper) bufferSource).getOriginal(),  // because the Iris one will run first
			(renderType) -> OuterWrappedRenderType.wrapExactlyOnce("iris:block_entity", renderType, BlockEntityRenderStateShard.INSTANCE)
		);
	}
}

package io.github.jedlimlx.supplemental_patches.mixins;
import com.bawnorton.mixinsquared.TargetHandler;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.uniforms.CapturedRenderingState;
import net.mehvahdjukaar.amendments.common.tile.CandleSkullBlockTile;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockEntityRenderDispatcher.class, priority = 1500)
public class BlockEntityRenderDispatcherMixin {
	@TargetHandler(
		mixin = "net.irisshaders.iris.mixin.entity_render_context.MixinBlockEntityRenderDispatcher",
		name = "iris$wrapBufferSource"
	)
	@Inject(
		method = "@MixinSquared:Handler",
		at = @At(
			value = "INVOKE",
			target = "net/irisshaders/iris/uniforms/CapturedRenderingState.setCurrentBlockEntity (I)V",
			shift = At.Shift.AFTER
		)
	)
	private void getBlockEntityId(MultiBufferSource bufferSource, BlockEntity blockEntity, CallbackInfoReturnable<MultiBufferSource> cir) {
		int intId;
		Object2IntMap<BlockState> blockStateIds = WorldRenderingSettings.INSTANCE.getBlockStateIds();
		switch (blockEntity) {
			case WallLanternBlockTile lantern ->
				intId = blockStateIds.getOrDefault(lantern.getHeldBlock().getBlock().defaultBlockState(), -1);
			case LiquidCauldronBlockTile cauldron ->
				//intId = cauldron.getSoftFluidTank().getFluid()
				intId = -1;
			default -> {
				BlockState state = blockEntity.getBlockState();
				intId = blockStateIds.getOrDefault(state, -1);
			}
		}

		CapturedRenderingState.INSTANCE.setCurrentBlockEntity(intId);
	}
}

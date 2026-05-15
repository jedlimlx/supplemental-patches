package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? =1.20.1 || =1.21.1 {
import com.mojang.blaze3d.vertex.PoseStack;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import io.github.jedlimlx.supplemental_patches.SupplementalPatchesKt;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.uniforms.CapturedRenderingState;
import net.mehvahdjukaar.amendments.client.renderers.WallLanternBlockTileRenderer;
import net.mehvahdjukaar.amendments.common.tile.WallLanternBlockTile;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded(value = "amendments")
@IfMinecraftVersion(minVersion = "1.20.1", maxVersion="1.21.1")
@Mixin(value = WallLanternBlockTileRenderer.class, remap = false)
public class AmendmentsWallLanternMixin {
    private static int previousE;

    @Inject(
        method = "render(Lnet/mehvahdjukaar/amendments/common/tile/WallLanternBlockTile;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At("HEAD")
    )
    private void changeId(WallLanternBlockTile tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn, CallbackInfo ci) {
        if (WorldRenderingSettings.INSTANCE.getEntityIds() == null) return;

        previousE = CapturedRenderingState.INSTANCE.getCurrentRenderedBlockEntity();

		int id = WorldRenderingSettings.INSTANCE.getBlockStateIds().applyAsInt(tile.getHeldBlock().getBlock().defaultBlockState());
		CapturedRenderingState.INSTANCE.setCurrentBlockEntity(id);
    }

    @Inject(
        method = "render(Lnet/mehvahdjukaar/amendments/common/tile/WallLanternBlockTile;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At("RETURN")
    )
    private void changeId2(CallbackInfo ci) {
		SupplementalPatchesKt.LOGGER.info("asd " + CapturedRenderingState.INSTANCE.getCurrentRenderedBlockEntity());
		CapturedRenderingState.INSTANCE.setCurrentBlockEntity(previousE);
    }
}
//?}

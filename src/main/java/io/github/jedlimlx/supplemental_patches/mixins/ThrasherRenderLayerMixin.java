package io.github.jedlimlx.supplemental_patches.mixins;

//? neoforge || forge {
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import com.teamabnormals.upgrade_aquatic.client.renderer.entity.layers.ThrasherRenderLayer;

@IfModLoaded(value = "upgrade_aquatic")
@Mixin(ThrasherRenderLayer.class)
public class ThrasherRenderLayerMixin {
    @Redirect(
        method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILcom/teamabnormals/upgrade_aquatic/common/entity/monster/Thrasher;FFFFFF)V",
        at = @At(
            value = "INVOKE",
            target = "com/teamabnormals/blueprint/client/BlueprintRenderTypes.getUnshadedCutoutEntity (Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;"
        ),
        remap = false
    )
    public RenderType render(ResourceLocation texture, boolean outline) {
        // TODO revert to Blueprint shaders when no shaderpack is enabled
        return RenderType.entityTranslucent(texture);
    }
}
//?}

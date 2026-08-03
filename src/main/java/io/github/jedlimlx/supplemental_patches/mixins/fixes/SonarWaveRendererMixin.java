package io.github.jedlimlx.supplemental_patches.mixins.fixes;

//? neoforge || forge {
/*import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import com.teamabnormals.upgrade_aquatic.client.renderer.entity.SonarWaveRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@IfModLoaded(value = "upgrade_aquatic")
@Mixin(SonarWaveRenderer.class)
public class SonarWaveRendererMixin {
    @Redirect(
        method = "render(Lcom/teamabnormals/upgrade_aquatic/common/entity/projectile/SonarWave;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lcom/teamabnormals/blueprint/client/BlueprintRenderTypes;getUnshadedTranslucentEntity(Lnet/minecraft/resources/Identifier;Z)Lnet/minecraft/client/renderer/RenderType;"
        ),
        remap = false
    )
    public RenderType render(Identifier texture, boolean outline) {
        // TODO revert to Blueprint shaders when no shaderpack is enabled
        return RenderType.entityTranslucent(texture);
    }
}
*///?}

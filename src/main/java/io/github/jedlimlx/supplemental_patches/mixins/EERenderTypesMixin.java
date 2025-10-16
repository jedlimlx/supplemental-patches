package io.github.jedlimlx.supplemental_patches.mixins;

import com.teamabnormals.endergetic.core.other.EERenderTypes;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Restriction(require = @Condition("endergetic"))
@Mixin(EERenderTypes.class)
public class EERenderTypesMixin {
    @Redirect(
        method = "<clinit>",
        at = @At(
                value = "INVOKE",
                target = "Lcom/teamabnormals/blueprint/client/BlueprintRenderTypes;getUnshadedCutoutEntity(Lnet/minecraft/resources/ResourceLocation;Z)Lnet/minecraft/client/renderer/RenderType;"
        ),
        remap = false
    )
    private static RenderType getUnshadedCutoutEntity(ResourceLocation texture, boolean outline) {
        return RenderType.entityCutoutNoCull(texture);
    }
}

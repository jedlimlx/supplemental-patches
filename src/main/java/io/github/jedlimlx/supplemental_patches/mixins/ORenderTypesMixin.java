package io.github.jedlimlx.supplemental_patches.mixins;

import galena.doom_and_gloom.client.ORenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ORenderTypes.class, remap = false)
public class ORenderTypesMixin {
    @Inject(
        method = "lambda$static$2",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void lambda$static$2(ResourceLocation t, CallbackInfoReturnable<RenderType> cir) {
        cir.setReturnValue(RenderType.entityTranslucent(t));
    }
}

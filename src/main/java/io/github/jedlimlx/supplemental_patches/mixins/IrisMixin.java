package io.github.jedlimlx.supplemental_patches.mixins;

import net.irisshaders.iris.Iris;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Iris.class, remap = false)
public class IrisMixin {
    @Redirect(
        method = "onRenderSystemInit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/irisshaders/iris/Iris;loadShaderpack()V"
        )
    )
    private static void method() {}
}

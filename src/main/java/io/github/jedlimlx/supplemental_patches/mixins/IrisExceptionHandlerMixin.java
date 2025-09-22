package io.github.jedlimlx.supplemental_patches.mixins;

import io.github.jedlimlx.supplemental_patches.shaders.MinecraftError;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.IrisLogging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static io.github.jedlimlx.supplemental_patches.shaders.ShaderError.shaderErrors;


@Mixin(value = Iris.class, remap = false)
public class IrisExceptionHandlerMixin {
    @Redirect(
        method = "createPipeline",
        at = @At(
            value = "INVOKE",
            target = "net/irisshaders/iris/IrisLogging.error (Ljava/lang/String;Ljava/lang/Throwable;)V"
        )
    )
    private static void logError(IrisLogging instance, String error, Throwable t) {
        (new MinecraftError(t.getMessage(), null, "IRIS ERROR")).sendInChat();
        instance.error("Failed to create shader rendering pipeline, disabling shaders!", t);

        shaderErrors.get(shaderErrors.size() - 1).sendInChat();
    }
}

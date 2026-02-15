package io.github.jedlimlx.supplemental_patches.mixins;

import io.github.jedlimlx.supplemental_patches.TextureStitchEvent;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public class TextureAtlasMixin {
    @Inject(
        method = "method_45848",  // TODO find a better workaround than replacing this with method_45848 when building JAR
        at = @At("RETURN")
    )
    public void upload(SpriteLoader.Preparations preparations, CallbackInfo ci) {
        TextureStitchEvent.Companion.getEVENT().invoker().afterStitched((TextureAtlas) (Object) this);
    }
}

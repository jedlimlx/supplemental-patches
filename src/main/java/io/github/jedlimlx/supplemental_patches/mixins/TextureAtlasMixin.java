package io.github.jedlimlx.supplemental_patches.mixins;

//? fabric {
/*import io.github.jedlimlx.supplemental_patches.platforms.fabric.TextureStitchEvent;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureAtlas.class)
public class TextureAtlasMixin {
	@Inject(
		method = "upload",
		at = @At("RETURN")
	)
	public void upload(SpriteLoader.Preparations preparations, CallbackInfo ci) {
		TextureStitchEvent.Companion.getEVENT().invoker().afterStitched((TextureAtlas) (Object) this);
	}
}
*///?}

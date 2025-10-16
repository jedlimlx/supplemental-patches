package io.github.jedlimlx.supplemental_patches.mixins;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.jadenxgamer.netherexp.event.JNEClientEvents;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("netherexp"))
@Mixin(value = JNEClientEvents.class, remap = false)
public class JNEClientEventsMixin {
    @Inject(
        method = "postEffectRender",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void postEffectRender(RenderLevelStageEvent event, CallbackInfo ci) {
        // ci.cancel();
    }
}

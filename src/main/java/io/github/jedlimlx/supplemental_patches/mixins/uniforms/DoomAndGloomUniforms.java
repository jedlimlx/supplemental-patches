package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.shaderpack.properties.PackDirectives;
import net.irisshaders.iris.uniforms.CommonUniforms;
import net.irisshaders.iris.uniforms.FrameUpdateNotifier;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("doom_and_gloom"))
@Mixin(CommonUniforms.class)
public class DoomAndGloomUniforms {
    @Inject(
        method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
        at = @At("TAIL"),
        remap = false
    )
    private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
//        uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "doomAndGloomFog", DoomAndGloomUniforms::getDoomAndGloomFog);
    }

//    private static float getDoomAndGloomFog() {
//        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
//        if (cameraEntity instanceof LivingEntity) {
//            MobEffectInstance fog = ((LivingEntity)cameraEntity).getEffect(galena.doom_and_gloom.index.OEffects.FOG);
//            if (fog != null) {
//                return 1.0F;
//            } else return 0.0F;
//        }
//
//        return 0.0F;
//    }
}

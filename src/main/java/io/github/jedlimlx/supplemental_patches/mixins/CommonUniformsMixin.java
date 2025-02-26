package io.github.jedlimlx.supplemental_patches.mixins;

import galena.oreganized.index.OEffects;
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

@Mixin(CommonUniforms.class)
public class CommonUniformsMixin {
    @Inject(
        method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
        at = @At("TAIL"),
        remap = false
    )
    private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
        uniforms
                .uniform1f(UniformUpdateFrequency.PER_FRAME, "brainDamage", CommonUniformsMixin::getBrainDamage)
                .uniform1f(UniformUpdateFrequency.PER_FRAME, "doomAndGloomFog", CommonUniformsMixin::getDoomAndGloomFog);
    }

//    @Inject(
//        method = "isEyeInWater()I",
//        at = @At(value = "TAIL"),
//        remap = false
//    )
//    private static void isEyeInWater(CallbackInfoReturnable<Integer> cir) {
//        cir.setReturnValue(0);
//    }

    private static float getBrainDamage() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity instanceof LivingEntity) {
            MobEffectInstance brainDamage = ((LivingEntity)cameraEntity).getEffect(OEffects.STUNNING.get());
            if (brainDamage != null) {
                return brainDamage.getAmplifier();
            } else return 0.0F;
        }

        return 0.0F;
    }

    private static float getDoomAndGloomFog() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity instanceof LivingEntity) {
            MobEffectInstance fog = ((LivingEntity)cameraEntity).getEffect(galena.doom_and_gloom.index.OEffects.FOG.get());
            if (fog != null) {
                return 1.0F;
            } else return 0.0F;
        }

        return 0.0F;
    }
}

package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? fabric && >= 1.21.1 {
/*import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.frozenblock.trailiertales.registry.TTMobEffects;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.shaderpack.properties.PackDirectives;
import net.irisshaders.iris.uniforms.CapturedRenderingState;
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

@IfModLoaded(value = "trailiertales")
@Mixin(CommonUniforms.class)
public class HauntUniforms {
    @Inject(
            method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
            at = @At("TAIL"),
            remap = false
    )
    private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
        uniforms
                .uniform1f(UniformUpdateFrequency.PER_FRAME, "haunt", HauntUniforms::getHaunt)
                .uniform1f(UniformUpdateFrequency.PER_FRAME, "hauntFactor", HauntUniforms::getHauntFactor);
    }

    private static float getHaunt() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity instanceof LivingEntity) {
            MobEffectInstance haunt = ((LivingEntity)cameraEntity).getEffect(TTMobEffects.HAUNT);
            if (haunt != null) {
                if (haunt.isInfiniteDuration()) return 1.0F;
                return org.joml.Math.clamp(0.0F, 1.0F, haunt.getDuration() / 5.0F);
            }
        }

        return 0.0F;
    }

    private static float getHauntFactor() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity instanceof LivingEntity) {
            MobEffectInstance haunt = ((LivingEntity)cameraEntity).getEffect(TTMobEffects.HAUNT);
            if (haunt != null) {
                return haunt.getBlendFactor((LivingEntity)cameraEntity, CapturedRenderingState.INSTANCE.getTickDelta());
            }
        }

        return 0.0F;
    }
}
*///?}

package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

import com.yungnickyoung.minecraft.yungscavebiomes.client.render.sandstorm.ISandstormClientDataProvider;
import com.yungnickyoung.minecraft.yungscavebiomes.client.render.sandstorm.SandstormClientData;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.shaderpack.properties.PackDirectives;
import net.irisshaders.iris.uniforms.CommonUniforms;
import net.irisshaders.iris.uniforms.FrameUpdateNotifier;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition("yungscavebiomes"))
@Mixin(CommonUniforms.class)
public class SandstormUniforms {
    @Inject(
        method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
        at = @At("TAIL"),
        remap = false
    )
    private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
        uniforms
                .uniform1f(UniformUpdateFrequency.PER_FRAME, "sandstorm", SandstormUniforms::getSandstorm)
                .uniform3f(UniformUpdateFrequency.PER_FRAME, "sandstormWindDirection", SandstormUniforms::getSandstormWindDirection);
    }

    private static float getSandstorm() {
        SandstormClientData data = ((ISandstormClientDataProvider) Minecraft.getInstance().level).getSandstormClientData();
        if (data.isSandstormActive()) return 1.0F;
        else return 0.0F;
    }

    private static Vector3f getSandstormWindDirection() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();

        if (cameraEntity instanceof LivingEntity) {
            SandstormClientData data = ((ISandstormClientDataProvider) Minecraft.getInstance().level).getSandstormClientData();
            return data.getSandstormParticleSpeedVector(cameraEntity.getX(), cameraEntity.getY(), cameraEntity.getZ(), new Vector3f(0, 0, 0));
        }

        return new Vector3f(0, 0, 0);
    }
}

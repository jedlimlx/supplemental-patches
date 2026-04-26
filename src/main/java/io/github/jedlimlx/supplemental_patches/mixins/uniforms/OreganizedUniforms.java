package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? neoforge || forge {
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
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

@IfModLoaded(value = "oreganized")
@IfMinecraftVersion(minVersion = "1.20.1", maxVersion="1.21.1")
@Mixin(CommonUniforms.class)
public class OreganizedUniforms {
    @Inject(
        method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
        at = @At("TAIL"),
        remap = false
    )
    private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
        uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "brainDamage", OreganizedUniforms::getBrainDamage);
    }

    private static float getBrainDamage() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity instanceof LivingEntity) {
			//? neoforge {
			MobEffectInstance brainDamage = ((LivingEntity)cameraEntity).getEffect(OEffects.STUNNING);
			//?} forge {
			/*MobEffectInstance brainDamage = ((LivingEntity)cameraEntity).getEffect(OEffects.STUNNING.get());
			 *///?}
            if (brainDamage != null) {
                return brainDamage.getAmplifier();
            } else return 0.0F;
        }

        return 0.0F;
    }
}
//?}

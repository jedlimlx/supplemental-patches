package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? neoforge || forge {
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.shaderpack.properties.PackDirectives;
import net.irisshaders.iris.uniforms.CommonUniforms;
import net.irisshaders.iris.uniforms.FrameUpdateNotifier;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? neoforge {
import net.jadenxgamer.netherexp.registry.JNEMobEffects;
import net.jadenxgamer.netherexp.registry.JNEFluids;
//?} forge {
/*import net.jadenxgamer.netherexp.registry.effect.JNEMobEffects;
import net.jadenxgamer.netherexp.registry.fluid.JNEFluids;
*///?}

@IfModLoaded(value = "netherexp")
@IfMinecraftVersion(minVersion = "1.20.1", maxVersion="1.21.1")
@Mixin(CommonUniforms.class)
public class JNEUniforms {
    @Inject(
        method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
        at = @At("TAIL"),
        remap = false
    )
    private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
        uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "betrayed", JNEUniforms::getBetrayedEffect);
		uniforms.uniform1f(UniformUpdateFrequency.PER_FRAME, "inEctoplasm", JNEUniforms::checkInEctoplasm);
    }

    private static float getBetrayedEffect() {
        Entity cameraEntity = Minecraft.getInstance().getCameraEntity();
        if (cameraEntity instanceof LivingEntity) {
			//? neoforge {
			MobEffectInstance betrayed = ((LivingEntity)cameraEntity).getEffect(JNEMobEffects.BETRAYED);
			//?} forge {
			/*MobEffectInstance betrayed = ((LivingEntity)cameraEntity).getEffect(JNEMobEffects.BETRAYED.get());
			 *///?}
            if (betrayed != null) {
                if (betrayed.isInfiniteDuration() || betrayed.getDuration() > 10.0F) return 1.0F;
                else return betrayed.getDuration() / 10.0F;
            } else return 0.0F;
        }

        return 0.0F;
    }

	private static float checkInEctoplasm() {
		Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
		Vec3 cameraPos = camera.getPosition();
		BlockPos blockPos = camera.getBlockPosition();

		FluidState fluidState = Minecraft.getInstance().level.getFluidState(blockPos);
		if (fluidState != null && fluidState.getFluidType() == JNEFluids.ECTOPLASM_TYPE.get()) {
			double fluidHeight = blockPos.getY() + fluidState.getHeight(Minecraft.getInstance().level, blockPos);
			if (cameraPos.y() < fluidHeight) return 1.0F;
		}

		return 0.0F;
	}
}
//?}

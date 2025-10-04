package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

import dev.corgitaco.enhancedcelestials.EnhancedCelestials;
import dev.corgitaco.enhancedcelestials.api.lunarevent.LunarEvent;
import dev.corgitaco.enhancedcelestials.lunarevent.EnhancedCelestialsLunarForecastWorldData;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.irisshaders.iris.gl.uniform.UniformHolder;
import net.irisshaders.iris.gl.uniform.UniformUpdateFrequency;
import net.irisshaders.iris.shaderpack.properties.PackDirectives;
import net.irisshaders.iris.uniforms.CommonUniforms;
import net.irisshaders.iris.uniforms.FrameUpdateNotifier;
import net.minecraft.client.Minecraft;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Restriction(require = @Condition("enhancedcelestials"))
@Mixin(CommonUniforms.class)
public class EnhancedCelestialsUniforms {
    @Inject(
        method = "generalCommonUniforms(Lnet/irisshaders/iris/gl/uniform/UniformHolder;Lnet/irisshaders/iris/uniforms/FrameUpdateNotifier;Lnet/irisshaders/iris/shaderpack/properties/PackDirectives;)V",
        at = @At("TAIL"),
        remap = false
    )
    private static void generalCommonUniforms(UniformHolder uniforms, FrameUpdateNotifier updateNotifier, PackDirectives directives, CallbackInfo ci) {
        uniforms
            .uniform1f(UniformUpdateFrequency.PER_FRAME, "moonSize", EnhancedCelestialsUniforms::getMoonSize)
            .uniform3f(UniformUpdateFrequency.PER_FRAME, "moonColor", EnhancedCelestialsUniforms::getMoonColor)
            .uniform3f(UniformUpdateFrequency.PER_FRAME, "skylightColor", EnhancedCelestialsUniforms::getSkylightColor);
    }

    private static Vector3f getSkylightColor() {
        try {
            Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(Minecraft.getInstance().level);
            EnhancedCelestialsLunarForecastWorldData data = lunarForecastWorldData.orElseThrow();

            LunarEvent event = data.currentLunarEvent();
            return event.getClientSettings().colorSettings().getGLSkyLightColor();
        } catch (Exception e) {
            return new Vector3f(1.0f);
        }
    }

    private static Vector3f getMoonColor() {
        try {
            Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(Minecraft.getInstance().level);
            EnhancedCelestialsLunarForecastWorldData data = lunarForecastWorldData.orElseThrow();

            LunarEvent event = data.currentLunarEvent();
            return event.getClientSettings().colorSettings().getGLMoonColor();
        } catch (Exception e) {
            return new Vector3f(1.0f);
        }
    }

    private static float getMoonSize() {
        try {
            Optional<EnhancedCelestialsLunarForecastWorldData> lunarForecastWorldData = EnhancedCelestials.lunarForecastWorldData(Minecraft.getInstance().level);
            EnhancedCelestialsLunarForecastWorldData data = lunarForecastWorldData.orElseThrow();

            LunarEvent event = data.currentLunarEvent();
            return event.getClientSettings().moonSize();
        } catch (Exception e) {
            return 20.0f;
        }
    }
}

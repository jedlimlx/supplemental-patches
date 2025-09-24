package io.github.jedlimlx.supplemental_patches.mixins;

import io.github.jedlimlx.supplemental_patches.shaders.ErrorHandlingKt;
import net.irisshaders.iris.gl.GLDebug;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintStream;

@Mixin(value = GLDebug.class, remap = false)
public abstract class GLDebugMixin {
    @Shadow
    private static String getDebugSource(int source) { return null; }

    @Shadow
    private static String getDebugType(int source) { return null; }

    @Shadow
    private static String getDebugSeverity(int source) { return null; }

    @Shadow
    private static String getSourceARB(int source) { return null; }

    @Shadow
    private static String getTypeARB(int source) { return null; }

    @Shadow
    private static String getSeverityARB(int source) { return null; }

    @Shadow
    private static String getCategoryAMD(int category) { return null; }

    @Shadow
    private static String getSeverityAMD(int category) { return null; }

    @Inject(
        method = "lambda$setupDebugMessageCallback$0",
        at = @At("HEAD")
    )
    private static void openGlDebugCallback(
        PrintStream stream, int source, int type, int id, int severity,
        int length, long message, long userParam, CallbackInfo ci
    ) {
        ErrorHandlingKt.sendShaderErrorInChat(
            getDebugSource(source),
            getDebugType(type),
            getDebugSeverity(severity),
            GLDebugMessageCallback.getMessage(length, message),
            "OpenGL"
        );
    }

    @Inject(
        method = "lambda$setupDebugMessageCallback$1",
        at = @At("HEAD")
    )
    private static void openGlDebugCallback2(
        PrintStream stream, int source, int type, int id, int severity,
        int length, long message, long userParam, CallbackInfo ci
    ) {
        ErrorHandlingKt.sendShaderErrorInChat(
            getDebugSource(source),
            getDebugType(type),
            getDebugSeverity(severity),
            GLDebugMessageCallback.getMessage(length, message),
            "OpenGL"
        );
    }

    @Inject(
            method = "lambda$setupDebugMessageCallback$2",
            at = @At("HEAD")
    )
    private static void arbDebugCallback(
        PrintStream stream, int source, int type, int id, int severity,
        int length, long message, long userParam, CallbackInfo ci
    ) {
        ErrorHandlingKt.sendShaderErrorInChat(
            getSourceARB(source),
            getTypeARB(type),
            getSeverityARB(severity),
            GLDebugMessageCallback.getMessage(length, message),
            "ARB"
        );
    }

    @Inject(
        method = "lambda$setupDebugMessageCallback$3",
        at = @At("HEAD")
    )
    private static void amdDebugCallback(
        PrintStream stream, int id, int category, int severity, int length,
        long message, long userParam, CallbackInfo ci
    ) {
        ErrorHandlingKt.sendShaderErrorInChat(
            getCategoryAMD(category),
            "ERROR",
            getSeverityAMD(severity),
            GLDebugMessageCallback.getMessage(length, message),
            "AMD"
        );
    }
}

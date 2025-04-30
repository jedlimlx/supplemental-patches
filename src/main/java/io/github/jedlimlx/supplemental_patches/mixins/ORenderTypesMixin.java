package io.github.jedlimlx.supplemental_patches.mixins;

import galena.doom_and_gloom.client.ORenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(value = ORenderTypes.class, remap = false)
public class ORenderTypesMixin {
    @Final
    @Shadow
    public static final Function<ResourceLocation, RenderType> ADDITIVE_TRANSLUCENCY = RenderType::entityTranslucent;
}

package io.github.jedlimlx.supplemental_patches.mixins;

import galena.doom_and_gloom.client.DGRenderTypes;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@Restriction(require = @Condition("doom_and_gloom"))
@Mixin(value = DGRenderTypes.class, remap = false)
public class DGRenderTypesMixin {
    @Final
    @Shadow
    public static final Function<ResourceLocation, RenderType> ADDITIVE_TRANSLUCENCY = RenderType::entityTranslucent;
}

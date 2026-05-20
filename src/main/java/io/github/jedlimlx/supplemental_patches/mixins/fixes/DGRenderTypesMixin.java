package io.github.jedlimlx.supplemental_patches.mixins.fixes;

//? 1.20.1 || 1.21.1 {
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import galena.doom_and_gloom.client.DGRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Function;

@IfModLoaded(value = "doom_and_gloom")
@IfMinecraftVersion(minVersion = "1.20.1", maxVersion="1.21.1")
@Mixin(value = DGRenderTypes.class, remap = false)
public class DGRenderTypesMixin {
//    @Final
//    @Shadow
//    public static final Function<ResourceLocation, RenderType> ADDITIVE_TRANSLUCENCY = RenderType::entityTranslucent;
}
//?}

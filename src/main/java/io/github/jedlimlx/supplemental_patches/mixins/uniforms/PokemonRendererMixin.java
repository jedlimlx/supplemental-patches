package io.github.jedlimlx.supplemental_patches.mixins.uniforms;

//? =1.20.1 || =1.21.1 {
import com.cobblemon.mod.common.client.render.pokemon.PokemonRenderer;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.pokemon.Species;
import com.mojang.blaze3d.vertex.PoseStack;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import net.irisshaders.iris.shaderpack.materialmap.NamespacedId;
import net.irisshaders.iris.shaderpack.materialmap.WorldRenderingSettings;
import net.irisshaders.iris.uniforms.CapturedRenderingState;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@IfModLoaded(value = "cobblemon")
@IfMinecraftVersion(minVersion = "1.20.1", maxVersion="1.21.1")
@Mixin(value = PokemonRenderer.class, remap = false)
public class PokemonRendererMixin {
    private static int previousE;

    @Inject(
        method = "render(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("HEAD")
    )
    private void changeId(PokemonEntity entity, float entityYaw, float partialTicks, PoseStack poseMatrix, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (WorldRenderingSettings.INSTANCE.getEntityIds() == null) return;

        Species species = entity.getPokemon().getSpecies();
        String unformattedShowdownId = species.getName().toLowerCase().replaceAll("[^a-z0-9]+", "");

        NamespacedId pokemonId = new NamespacedId(species.resourceIdentifier.getNamespace(), unformattedShowdownId);

        previousE = CapturedRenderingState.INSTANCE.getCurrentRenderedEntity();
        CapturedRenderingState.INSTANCE.setCurrentEntity(WorldRenderingSettings.INSTANCE.getEntityIds().applyAsInt(pokemonId));
    }

    @Inject(
        method = "render(Lcom/cobblemon/mod/common/entity/pokemon/PokemonEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("RETURN")
    )
    private void changeId2(CallbackInfo ci) {
        if (previousE != 0) {
            CapturedRenderingState.INSTANCE.setCurrentEntity(previousE);
            previousE = 0;
        }
    }
}
//?}

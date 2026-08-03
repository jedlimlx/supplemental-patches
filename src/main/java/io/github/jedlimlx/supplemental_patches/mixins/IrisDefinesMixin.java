package io.github.jedlimlx.supplemental_patches.mixins;

import io.github.jedlimlx.supplemental_patches.shaders.BiomeUniformsKt;
import net.irisshaders.iris.helpers.StringPair;
import net.irisshaders.iris.shaderpack.IrisDefines;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Locale;

import static io.github.jedlimlx.supplemental_patches.SupplementalPatchesKt.PLATFORM;

@Mixin(IrisDefines.class)
public class IrisDefinesMixin {
    @ModifyVariable(
        method = "createIrisReplacements()Lcom/google/common/collect/ImmutableList;",
        at = @At("STORE"),
        ordinal = 0,
        remap = false
    )
    private static ArrayList<StringPair> createStandardEnvironmentDefines(ArrayList<StringPair> lst) {
        for (String modId : PLATFORM.modList()) {
            lst.add(new StringPair("MOD_" + modId.toUpperCase(), ""));
        }

        BiomeUniformsKt.getBiomeMap().forEach(
            (biome, id) -> lst.add(
                new StringPair(
                    "MOD_BIOME_" + biome.identifier().getPath().toUpperCase(Locale.ROOT),
                    String.valueOf(id)
                )
            )
        );

        return lst;
    }
}

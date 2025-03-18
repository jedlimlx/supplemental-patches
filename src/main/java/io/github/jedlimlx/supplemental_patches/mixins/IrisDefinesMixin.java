package io.github.jedlimlx.supplemental_patches.mixins;


import io.github.jedlimlx.supplemental_patches.shaders.BiomeUniformsKt;
import net.irisshaders.iris.helpers.StringPair;
import net.irisshaders.iris.shaderpack.IrisDefines;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;
import java.util.Locale;

@Mixin(IrisDefines.class)
public class IrisDefinesMixin {
    @ModifyVariable(
        method = "createIrisReplacements()Lcom/google/common/collect/ImmutableList;",
        at = @At("STORE"),
        ordinal = 0,
        remap = false
    )
    private static ArrayList<StringPair> createStandardEnvironmentDefines(ArrayList<StringPair> lst) {
        for (IModInfo mod : ModList.get().getMods()) {
            lst.add(new StringPair("MOD_" + mod.getModId().toUpperCase(), ""));
        }

        BiomeUniformsKt.getBiomeMap().forEach(
            (biome, id) -> lst.add(
                new StringPair(
                    "MOD_BIOME_" + biome.location().getPath().toUpperCase(Locale.ROOT),
                    String.valueOf(id)
                )
            )
        );

        return lst;
    }
}

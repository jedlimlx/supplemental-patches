package io.github.jedlimlx.supplemental_patches.mixins;


import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.irisshaders.iris.gl.shader.StandardMacros;
import net.irisshaders.iris.helpers.StringPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.ArrayList;

@Mixin(StandardMacros.class)
public class StandardMacrosMixin {
    @ModifyVariable(
        method = "createStandardEnvironmentDefines()Lcom/google/common/collect/ImmutableList;",
        at = @At("STORE"),
        ordinal = 0,
        remap = false
    )
    private static ArrayList<StringPair> createStandardEnvironmentDefines(ArrayList<StringPair> lst) {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            lst.add(new StringPair("MOD_" + mod.getMetadata().getId().toUpperCase().replace("-", "_"), ""));
        }

        return lst;
    }
}

package io.github.jedlimlx.supplemental_patches.mixins;


import net.irisshaders.iris.gl.shader.StandardMacros;
import net.irisshaders.iris.helpers.StringPair;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
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
        for (IModInfo mod : ModList.get().getMods()) {
            lst.add(new StringPair("MOD_" + mod.getModId().toUpperCase(), ""));
        }

        return lst;
    }
}

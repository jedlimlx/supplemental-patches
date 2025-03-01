package io.github.jedlimlx.supplemental_patches.mixins;

import com.google.common.collect.ImmutableMap;
import net.minecraftforge.fml.ModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class SupplementalPatchesMixinPlugin implements IMixinConfigPlugin {
    private static final Supplier<Boolean> TRUE = () -> true;

    private static final Map<String, Supplier<Boolean>> CONDITIONS = ImmutableMap.of(
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.AtmosphericMixin", () -> ModList.get().isLoaded("atmospheric"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.AutumnityMixin", () -> ModList.get().isLoaded("autumnity"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.EndergeticMixin", () -> ModList.get().isLoaded("endergetic"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.EnvironmentalMixin", () -> ModList.get().isLoaded("environmental"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.GalosphereMixin", () -> ModList.get().isLoaded("galosphere"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.JNEMixin", () -> ModList.get().isLoaded("netherexp"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.NeapolitanMixin", () -> ModList.get().isLoaded("neapolitan"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.SpawnMixin", () -> ModList.get().isLoaded("spawn"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.WetlandWhimsyMixin", () -> ModList.get().isLoaded("wetland_whimsy"),
        "io.github.jedlimlx.supplemental_patches.mixins.biomes.YungsCaveBiomesMixin", () -> ModList.get().isLoaded("yungscavebiomes")
    );

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return CONDITIONS.getOrDefault(mixinClassName, TRUE).get();
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}

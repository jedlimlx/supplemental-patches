package io.github.jedlimlx.supplemental_patches.shaders

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome

val _biomeMap: Object2IntOpenHashMap<ResourceKey<Biome>> = Object2IntOpenHashMap()

// handling biomes
fun getBiomeMap(): Object2IntOpenHashMap<ResourceKey<Biome>> {
    _biomeMap.clear()

    val connection: ClientPacketListener? = Minecraft.getInstance()?.connection
    if (connection != null) {
        val biomes = connection.registryAccess().lookup(Registries.BIOME).orElseThrow()

        var currentId = 0
        biomes.entrySet().sortedWith(
            compareBy({ it.key.identifier().path }, { it.key.identifier().namespace })
        ).forEach { _biomeMap[it.key] = currentId++ }
    }

    return _biomeMap
}

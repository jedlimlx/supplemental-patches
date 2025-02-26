package io.github.jedlimlx.supplemental_patches.shaders

import io.github.jedlimlx.supplemental_patches.SupplementalPatches.LOGGER
import net.neoforged.fml.loading.FMLPaths
import java.nio.file.Path
import kotlin.io.path.*

val SHADERS_DIRECTORY: Path = FMLPaths.GAMEDIR.get().resolve("shaderpacks")

@OptIn(ExperimentalPathApi::class)
fun installShader(): String {
    val installation = detectInstallation()
    if (installation == null) {
        LOGGER.warn("No Complementary Shaders + Euphoria Patches installation detected.")
        return "No Complementary Shaders + Euphoria Patches installation detected."
    }

    LOGGER.info("Detected shader installation at $installation.")

    val newInstallation = Path(SHADERS_DIRECTORY.toString() + "/${installation.name} + Supplemental Patches")
    newInstallation.deleteRecursively()
    newInstallation.createDirectory()
    installation.copyToRecursively(newInstallation, overwrite = true, followLinks = false)

    LOGGER.info("Duplicated shader installation to $newInstallation.")

    // load the shaders
    MaterialShaders
    BlockEntityShaders
    TranslucentShaders
    EntityShaders
    ItemShaders
    ParticleShaders

    modifyBlockProperties(newInstallation)
    modifyEntityProperties(newInstallation)
    modifyItemProperties(newInstallation)
    generatedDeferredMaterials(newInstallation)
    generateSpecificMaterials(newInstallation)
    assignVoxelNumbers()
    generateTerrainMaterials(newInstallation)
    generateEntityMaterials(newInstallation)
    generateIrisMaterials(newInstallation)
    generateTranslucentMaterials(newInstallation)
    generateBlockEntityMaterials(newInstallation)
    generateVoxelsAndBlocklight(newInstallation)
    generateSettings(newInstallation)
    generateWavingCode(newInstallation)
    generateParticleCode(newInstallation)
    generateUniforms(newInstallation)
    generateFog(newInstallation)
    generateShaderMixins(newInstallation)
    modifyGBuffers(newInstallation)

    return "Shaders successfully installed at $newInstallation."
}

fun detectInstallation(): Path? =
    SHADERS_DIRECTORY.listDirectoryEntries().filter {
        it.isDirectory() && it.name.matches(
            Regex("Complementary(Unbound|Reimagined)_r(\\d+.?)+ \\+ EuphoriaPatches_(\\d+.?)+")
        )
    }.firstOrNull()

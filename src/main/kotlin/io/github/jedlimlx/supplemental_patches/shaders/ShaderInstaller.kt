package io.github.jedlimlx.supplemental_patches.shaders

import io.github.jedlimlx.supplemental_patches.LOGGER
import io.github.jedlimlx.supplemental_patches.PLATFORM
import java.nio.file.Path
import kotlin.io.path.*

@OptIn(ExperimentalPathApi::class)
fun installShader(): String = catchAndPrintError {
    val installation = detectInstallation()
    if (installation == null) {
        LOGGER.warn("No Complementary Shaders + Euphoria Patches installation detected.")
        return@catchAndPrintError "No Complementary Shaders + Euphoria Patches installation detected."
    }

    LOGGER.info("Detected shader installation at $installation.")

    val newInstallation = Path(PLATFORM.shaderDirectory.toString() + "/${installation.name} + Supplemental Patches")
    newInstallation.deleteRecursively()
    newInstallation.createDirectory()
    installation.copyToRecursively(newInstallation, overwrite = true, followLinks = false)

    LOGGER.info("Duplicated shader installation to $newInstallation.")

    // load the shaders
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
    generateWavingCode(newInstallation)
    generateReflectionHanders(newInstallation)
    generateParticleCode(newInstallation)
    injectBuffers(newInstallation)
    generateUniforms(newInstallation)
    generateFog(newInstallation)
    generateSkies(newInstallation)
    generateTextures(newInstallation)
    generateAtmospherics(newInstallation)
    generateSettingsFiles(newInstallation)
    generateSettings(newInstallation)
    refactorFunctions(newInstallation)
    generateShaderMixins(newInstallation)
    modifyLayers(newInstallation)
    modifyGBuffers(newInstallation)
    injectCommonFunctions(newInstallation)

    return@catchAndPrintError "Shaders successfully installed at $newInstallation."
}

fun detectInstallation(): Path? =
    PLATFORM.shaderDirectory.listDirectoryEntries().firstOrNull {
        it.isDirectory() && it.name.matches(
            Regex("Complementary(Unbound|Reimagined)_r(\\d+.?)+ \\+ EuphoriaPatches_(\\d+.?)+")
        )
    }

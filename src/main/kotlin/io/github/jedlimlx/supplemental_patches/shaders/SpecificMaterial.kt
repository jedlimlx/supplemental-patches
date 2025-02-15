package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class SpecificMaterial(val path: String, glsl: String) {
    private val _glsl: String = glsl
    val glsl: String
        get() {
            val lst = Regex("deferredMaterial\\(\"(.*?)\"\\)").findAll(_glsl).toList().map {
                Pair(
                    it.groupValues[1],
                    ShaderResourceLoader.DEFERRED_MAP[it.groupValues[1]]?.index ?: throw IllegalArgumentException("No deferred material ${it.groupValues[1]} found.")
                )
            }

            var output = _glsl
            lst.forEach { output = output.replace("deferredMaterial(\"${it.first}\")", it.second.toString()) }
            return output
        }

    init { MATERIALS.add(this) }
    companion object {
        val MATERIALS = mutableListOf<SpecificMaterial>()
    }
}

fun generateSpecificMaterials(directory: Path) {
    SpecificMaterial.MATERIALS.forEach {
        val file = File(directory.absolutePathString() + "/shaders/lib/materials/specificMaterials/${it.path}")
        file.writeText(it.glsl)
    }
}
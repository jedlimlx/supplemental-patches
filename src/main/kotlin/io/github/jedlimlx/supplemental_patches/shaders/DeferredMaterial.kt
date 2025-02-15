package io.github.jedlimlx.supplemental_patches.shaders

import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString

class DeferredMaterial(val name: String, val glsl: String) {
    var index: Int = 0

    init { MATERIALS.add(this) }
    companion object {
        val MATERIALS = mutableListOf<DeferredMaterial>()
    }
}


const val DEFERRED_INITIAL_ID = 16
const val DEFERRED_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/deferredMaterials.glsl"

fun generatedDeferredMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + DEFERRED_MATERIALS_PATH)

    var count = 0
    val code = generateCode("materialMaskInt", 64, DEFERRED_INITIAL_ID, 1) {
        if (count < DeferredMaterial.MATERIALS.size) {
            val shader = DeferredMaterial.MATERIALS[count]
            shader.index = it

            val code = "// ${shader.name}\n${shader.glsl}"
            count++

            code
        } else ""
    }.split("\n").joinToString("\n") { " ".repeat(20) + it }

    // injecting code into the old code
    file.writeText(
        file.readText().replace(
            "else /*if (materialMaskInt == \"15 to 255 except 254\")*/ { //",
            "else /*if (materialMaskInt == \"15 to 255 except 254\")*/ { //\n$code"
        )
    )
}
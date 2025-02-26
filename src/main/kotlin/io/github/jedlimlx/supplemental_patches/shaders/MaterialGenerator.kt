package io.github.jedlimlx.supplemental_patches.shaders

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.Resource
import java.io.File
import java.nio.file.Path
import kotlin.io.path.absolutePathString


const val BANNER = """
#   ___                _                   _        _   ___      _      _
#  / __|_  _ _ __ _ __| |___ _ __  ___ _ _| |_ __ _| | | _ \__ _| |_ __| |_  ___ ___
#  \__ \ || | '_ \ '_ \ / -_) '  \/ -_) ' \  _/ _` | | |  _/ _` |  _/ _| ' \/ -_|_-<
#  |___/\_,_| .__/ .__/_\___|_|_|_\___|_||_\__\__,_|_| |_| \__,_|\__\__|_||_\___/__/
#           |_|  |_|
"""

val BLOCK_ADDITIONAL_MAPPING = mutableMapOf<Int, List<String>>()
val BLOCK_REGEX_REPLACES = arrayListOf<Regex>()

val ITEM_ADDITIONAL_MAPPING = mutableMapOf<Int, List<String>>()
val ITEM_REGEX_REPLACES = arrayListOf<Regex>()

val ENTITY_ADDITIONAL_MAPPING = mutableMapOf<Int, List<String>>()
val ENTITY_REGEX_REPLACES = arrayListOf<Regex>()

fun modifyBlockProperties(directory: Path) {
    val file = File(directory.absolutePathString() + BLOCK_PROPERTIES)
    BLOCK_REGEX_REPLACES.map {
        file.writeText(it.replace(file.readText(), ""))
    }

    var code = file.readText()
    BLOCK_ADDITIONAL_MAPPING.forEach { (id, lst) ->
        code = code.replace("block.$id = ", "block.$id = ${lst.joinToString(" ")} \\\n\\\n")
    }

    file.writeText(code)
}

fun modifyItemProperties(directory: Path) {
    val file = File(directory.absolutePathString() + ITEM_PROPERTIES)
    ITEM_REGEX_REPLACES.map {
        file.writeText(it.replace(file.readText(), ""))
    }

    var code = file.readText()
    ITEM_ADDITIONAL_MAPPING.forEach { (id, lst) ->
        code = code.replace("item.$id = ", "item.$id = ${lst.joinToString(" ")} \\\n\\\n")
    }

    file.writeText(code)
}

fun modifyEntityProperties(directory: Path) {
    val file = File(directory.absolutePathString() + BLOCK_PROPERTIES)
    ENTITY_REGEX_REPLACES.map {
        file.writeText(it.replace(file.readText(), ""))
    }

    var code = file.readText()
    ENTITY_ADDITIONAL_MAPPING.forEach { (id, lst) ->
        code = code.replace("entity.$id = ", "entity.$id = ${lst.joinToString(" ")} \\\n\\\n")
    }

    file.writeText(code)
}

val MATERIALS = mutableListOf<ShaderBuilder>()
val MATERIALS_MAP = mutableMapOf<Int, ShaderBuilder>()

const val TERRAIN_INITIAL_ID = 12048
const val TERRAIN_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/terrainMaterials.glsl"

fun generateTerrainMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + TERRAIN_MATERIALS_PATH)

    var count = 0
    val code = generateCode("mat", 1024, TERRAIN_INITIAL_ID) {
        if (count < MATERIALS.size) {
            val material = MATERIALS[count]
            MATERIALS_MAP[it] = material

            val code = "// block.$it = ${material.name}\n${material.glsl}"
            count++

            code
        } else "// block.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    val lines = oldCode.split("\n")
    val newCode = lines.subList(0, lines.size - 3).joinToString("\n") + "} else $code}\n}"
    file.writeText(newCode)

    // writing the list of blocks to block.properties
    val blockPropertiesFile = File(directory.absolutePathString() + BLOCK_PROPERTIES)

    var text = blockPropertiesFile.readText()
    val builder = StringBuilder("\n$BANNER# Blocks added by Supplemental Patches\n\n")
    MATERIALS_MAP.forEach { (id, material) ->
        // Removing from their existing materials
        material.mat0.forEach { text = removeId(it, text) }
        material.mat1.forEach { text = removeId(it, text) }
        material.mat2.forEach { text = removeId(it, text) }
        material.mat3.forEach { text = removeId(it, text) }

        // Adding to new materials
        builder.apply {
            if (material.mat0.isNotEmpty())
                append("block.$id = ${material.mat0.joinToString(" ")}\n")
            if (material.mat1.isNotEmpty())
                append("block.${id + 1} = ${material.mat1.joinToString(" ")}\n")
            if (material.mat2.isNotEmpty())
                append("block.${id + 2} = ${material.mat2.joinToString(" ")}\n")
            if (material.mat3.isNotEmpty())
                append("block.${id + 3} = ${material.mat3.joinToString(" ")}\n")
        }
    }

    blockPropertiesFile.writeText(text + builder)
}

val ENTITIES = mutableListOf<ShaderBuilder>()
val ENTITY_MAP = mutableMapOf<Int, ShaderBuilder>()

const val ENTITY_INITIAL_ID = 51024
const val ENTITY_PATH = "/shaders/lib/materials/materialHandling/entityMaterials.glsl"

fun generateEntityMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + ENTITY_PATH)

    var count = 0
    val code = generateCode("entityId", 256, ENTITY_INITIAL_ID, 4) {
        if (count < ENTITIES.size) {
            val entity = ENTITIES[count]
            ENTITY_MAP[it] = entity

            val code = "// entity.$it = ${entity.name}\n${entity.glsl}"
            count++

            code
        } else "// entity.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    val lines = oldCode.replace("\n} else {", "\n} else if (entityId < 50128) {").split("\n")
    val newCode = lines.joinToString("\n") + " else $code"
    file.writeText(newCode)

    // writing the list of blocks to block.properties
    val entityPropertiesFile = File(directory.absolutePathString() + ENTITY_PROPERTIES)

    var text = entityPropertiesFile.readText()
    val builder = StringBuilder("\n$BANNER# Entities added by Supplemental Patches\n\n")
    ENTITY_MAP.forEach { (id, material) ->
        // Removing from their existing entity ids
        material.mat0.forEach { text = removeId(it, text) }
        material.mat1.forEach { text = removeId(it, text) }
        material.mat2.forEach { text = removeId(it, text) }
        material.mat3.forEach { text = removeId(it, text) }

        // Adding to new materials
        builder.apply {
            if (material.mat0.isNotEmpty()) append("entity.$id = ${material.mat0.joinToString(" ")}\n")
            if (material.mat1.isNotEmpty()) append("entity.${id + 1} = ${material.mat1.joinToString(" ")}\n")
            if (material.mat2.isNotEmpty()) append("entity.${id + 2} = ${material.mat2.joinToString(" ")}\n")
            if (material.mat3.isNotEmpty()) append("entity.${id + 3} = ${material.mat3.joinToString(" ")}\n")
        }
    }

    entityPropertiesFile.writeText(text + builder)
}

val ITEMS = mutableListOf<ShaderBuilder>()
val ITEM_MAP = mutableMapOf<Int, ShaderBuilder>()

const val IRIS_MATERIALS_INITIAL_ID = 45256
const val IRIS_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/irisMaterials.glsl"

fun generateIrisMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + IRIS_MATERIALS_PATH)

    var count = 0
    val code = generateCode("currentRenderedItemId", 256, IRIS_MATERIALS_INITIAL_ID, 4) {
        if (count < ITEMS.size) {
            val item = ITEMS[count]
            ITEM_MAP[it] = item

            val code = "// item.$it = ${item.name}\n${item.glsl}"
            count++

            code
        } else "// item.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    val lines = oldCode.replace("\n} else {", "\n} else if (currentRenderedItemId < 45128) {").split("\n")
    val newCode = lines.joinToString("\n") + " else $code"
    file.writeText(newCode)

    // writing the list of blocks to item.properties
    val itemPropertiesFile = File(directory.absolutePathString() + ITEM_PROPERTIES)

    var text = itemPropertiesFile.readText()
    val builder = StringBuilder("\n")
    ITEM_MAP.forEach { (id, material) ->
        // Removing from their existing item ids
        material.mat0.forEach { text = removeId(it, text) }
        material.mat1.forEach { text = removeId(it, text) }
        material.mat2.forEach { text = removeId(it, text) }
        material.mat3.forEach { text = removeId(it, text) }

        // Adding to new materials
        builder.apply {
            if (material.mat0.isNotEmpty()) append("item.$id = ${material.mat0.joinToString(" ")}\n")
            if (material.mat1.isNotEmpty()) append("item.${id + 1} = ${material.mat1.joinToString(" ")}\n")
            if (material.mat2.isNotEmpty()) append("item.${id + 2} = ${material.mat2.joinToString(" ")}\n")
            if (material.mat3.isNotEmpty()) append("item.${id + 3} = ${material.mat3.joinToString(" ")}\n")
        }
    }

    itemPropertiesFile.writeText(text + builder)
}

val TRANSLUCENTS = mutableListOf<ShaderBuilder>()
val TRANSLUCENTS_MAP = mutableMapOf<Int, ShaderBuilder>()

const val TRANSLUCENT_INITIAL_ID = 32128
const val TRANSLUCENT_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/translucentMaterials.glsl"

fun generateTranslucentMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + TRANSLUCENT_MATERIALS_PATH)

    var count = 0
    val code = generateCode("mat", 256, TRANSLUCENT_INITIAL_ID, 4) {
        if (count < TRANSLUCENTS.size) {
            val translucent = TRANSLUCENTS[count]
            TRANSLUCENTS_MAP[it] = translucent

            val code = "// block.$it = ${translucent.name}\n${translucent.glsl}"
            count++

            code
        } else "// block.$it"
    }
    val oldCode = file.readText().replace(Regex("uint\\((2\\d\\d)")) {
        "uint(${it.groupValues[1].toInt() - 200 + TRANSLUCENT_VOXEL_INITIAL_ID}"
    }

    // injecting code into the old code
    val lines = oldCode.replace("\n} else {", "\n} else if (mat < 32064) {").split("\n")
    val newCode = lines.joinToString("\n") + " else $code"
    file.writeText(newCode)

    // writing the list of blocks to block.properties
    val blockPropertiesFile = File(directory.absolutePathString() + BLOCK_PROPERTIES)

    var text = blockPropertiesFile.readText()
    val builder = StringBuilder("\n# Translucent materials added by Supplemental Patches\n\n")
    TRANSLUCENTS_MAP.forEach { (id, material) ->
        // Removing from their existing block ids
        material.mat0.forEach { text = removeId(it, text) }
        material.mat1.forEach { text = removeId(it, text) }
        material.mat2.forEach { text = removeId(it, text) }
        material.mat3.forEach { text = removeId(it, text) }

        // Adding to new materials
        builder.apply {
            if (material.mat0.isNotEmpty()) append("block.$id = ${material.mat0.joinToString(" ")}\n")
            if (material.mat1.isNotEmpty()) append("block.${id + 1} = ${material.mat1.joinToString(" ")}\n")
            if (material.mat2.isNotEmpty()) append("block.${id + 2} = ${material.mat2.joinToString(" ")}\n")
            if (material.mat3.isNotEmpty()) append("block.${id + 3} = ${material.mat3.joinToString(" ")}\n")
        }
    }

    blockPropertiesFile.writeText(text + builder)
}

val BLOCK_ENTITIES = mutableListOf<ShaderBuilder>()
val BLOCK_ENTITIES_MAP = mutableMapOf<Int, ShaderBuilder>()

const val BLOCK_ENTITY_INITIAL_ID = 60128
const val BLOCK_ENTITY_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/blockEntityMaterials.glsl"

fun generateBlockEntityMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + BLOCK_ENTITY_MATERIALS_PATH)

    var count = 0
    val code = generateCode("blockEntityId", 256, BLOCK_ENTITY_INITIAL_ID, 4) {
        if (count < BLOCK_ENTITIES.size) {
            val block_entity = BLOCK_ENTITIES[count]
            BLOCK_ENTITIES_MAP[it] = block_entity

            val code = "// block.$it = ${block_entity.name}\n${block_entity.glsl}"
            count++

            code
        } else "// block.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    val lines = oldCode.replace("\n} else {", "\n} else if (blockEntityId < 60064) {").split("\n")
    val newCode = lines.joinToString("\n") + " else $code"
    file.writeText(newCode)

    // writing the list of blocks to block.properties
    val blockPropertiesFile = File(directory.absolutePathString() + BLOCK_PROPERTIES)

    var text = blockPropertiesFile.readText()
    val builder = StringBuilder("\n# Block entities added by Supplemental Patches\n\n")
    BLOCK_ENTITIES_MAP.forEach { (id, material) ->
        // Removing from their existing block entity ids
        material.mat0.forEach { text = removeId(it, text) }
        material.mat1.forEach { text = removeId(it, text) }
        material.mat2.forEach { text = removeId(it, text) }
        material.mat3.forEach { text = removeId(it, text) }

        // Adding to new materials
        builder.apply {
            if (material.mat0.isNotEmpty()) append("block.$id = ${material.mat0.joinToString(" ")}\n")
            if (material.mat1.isNotEmpty()) append("block.${id + 1} = ${material.mat1.joinToString(" ")}\n")
            if (material.mat2.isNotEmpty()) append("block.${id + 2} = ${material.mat2.joinToString(" ")}\n")
            if (material.mat3.isNotEmpty()) append("block.${id + 3} = ${material.mat3.joinToString(" ")}\n")
        }
    }

    blockPropertiesFile.writeText(text + builder)
}

const val VOXELISATION_INITIAL_ID = 100
const val VOXELISATION_PATH = "/shaders/lib/misc/voxelization.glsl"

const val TOTAL_COLOURED_VOXELS = 256 + VOXELISATION_INITIAL_ID
const val TRANSLUCENT_VOXEL_INITIAL_ID = 60000
const val NEW_TINTS_INITIAL_ID = 60020

const val BLOCKLIGHT_PATH = "/shaders/lib/colors/blocklightColors.glsl"
const val MAIN_LIGHTING_PATH = "/shaders/lib/lighting/mainLighting.glsl"

const val GET_TINT_CODE = """

vec3 GetSpecialTintColor(uint mat) {
    if (mat < 60019u) return specialTintColor[mat - 60000u];
    else {
<insert>
    }

    return vec3(0.0);
}
"""

fun assignVoxelNumbers() {
    val colourIndex = (TINTS + COLOURS).filter { it.index == -1 }.mapIndexed { idx, colour ->
        colour to ((if (colour.tint) NEW_TINTS_INITIAL_ID else VOXELISATION_INITIAL_ID) + idx)
    }.toMap()

    var count = 0
    (MATERIALS + BLOCK_ENTITIES + TRANSLUCENTS).forEach { material ->
        if (material.lightColour.size == 1) {
            val colour = material.lightColour[0]!!
            (0..3).forEach {
                material.voxelNumber[it] =
                    colourIndex[colour] ?: (colour.index + (if (colour.tint) NEW_TINTS_INITIAL_ID else 0))
            }
        } else if (material.lightColour.isNotEmpty()) {
            material.lightColour.forEachIndexed { it, colour ->
                colour ?: return@forEachIndexed
                material.voxelNumber[it] =
                    colourIndex[colour] ?: (colour.index + (if (colour.tint) NEW_TINTS_INITIAL_ID else 0))
            }
        } else if (material.needsVoxelisation)
            (0..3).forEach { material.voxelNumber[it] = TOTAL_COLOURED_VOXELS + count++ }
    }
}

fun generateVoxelsAndBlocklight(directory: Path) {
    // Load file with item properties
    val itemPropertiesFile = File(directory.absolutePathString() + ITEM_PROPERTIES)

    var text = itemPropertiesFile.readText()
    val builder = StringBuilder("\n$BANNER# Items added by Supplemental Patches\n\n")
    val heldLightingMap: MutableMap<Int, String> = mutableMapOf()

    // Generating voxel ids
    val colourIndex = (TINTS + COLOURS).filter { it.index == -1 }.mapIndexed { idx, colour ->
        colour to ((if (colour.tint) NEW_TINTS_INITIAL_ID else VOXELISATION_INITIAL_ID) + idx)
    }.toMap()
    val colourIndexInverted = colourIndex.map { (k, v) -> v to k }.toMap()

    val voxelisationFile = File(directory.absolutePathString() + VOXELISATION_PATH)
    val voxelisationCode = StringBuilder().apply {
        val temp = MATERIALS_MAP + BLOCK_ENTITIES_MAP + TRANSLUCENTS_MAP
        val output = (temp.map { (id, material) ->
            if (material.lightColour.size == 1) id .. id + 3
            else if (material.lightColour.isNotEmpty())
                (0 .. 3).filter { material.lightColour[it] != null }.map { it + id }
            else if (material.needsVoxelisation) {
                id .. id + 3
            } else listOf()
        }.flatten()).sorted()

        append(
            computeAllPivots(output, 2, "mat", 1) { idx, depth ->
                StringBuilder().apply {
                    val material = temp[idx - idx % 4]!!
                    if (material.needsVoxelisation && material.lightColour.isEmpty()) {
                        append("    ".repeat(depth) + "if (mat == $idx) return ${material.voxelNumber[0]};\n")
                    } else {
                        val colour = material.lightColour[idx % material.lightColour.size]!!
                        val conditions = material.colourConditions.isNotEmpty()
                        if (conditions) append(
                            "    ".repeat(depth) +
                            "#if ${material.colourConditions.conditions()}\n"
                        )
                        append("    ".repeat(depth))

                        if (colour.index != -1) {
                            val index = colour.index + (if (colour.tint) NEW_TINTS_INITIAL_ID else 0)
                            append("if (mat == $idx) return $index;\n")

                            if (material.heldLighting && idx % 4 == 0) {
                                // Removing from their existing item ids
                                (material.mat0 + material.mat1 + material.mat2 + material.mat3).forEach {
                                    val tokens = it.split(":")
                                    text = text.replace("${tokens[0]}:${tokens[1]}", "")
                                }

                                // Adding to new materials
                                if (material.lightColour.size == 1) {
                                    val temp = (material.mat0 + material.mat1 + material.mat2 + material.mat3).joinToString(" ") {
                                        val tokens = it.split(":")
                                        "${tokens[0]}:${tokens[1]}"
                                    }
                                    text = text.replace(
                                        "item.${44000 + index} =",
                                        "item.${44000 + index} = " + temp
                                    )
                                } else {
                                    material.lightColour.forEachIndexed { idx, colour ->
                                        if (colour?.tint != false) return@forEachIndexed

                                        val mat = when (idx) {
                                            0 -> material.mat0
                                            1 -> material.mat1
                                            2 -> material.mat2
                                            else -> material.mat3
                                        }
                                        text = text.replace(
                                            "item.${44000 + index} =",
                                            "item.${44000 + index} = " + mat.joinToString(" ") {
                                                val tokens = it.split(":")
                                                "${tokens[0]}:${tokens[1]}"
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            append("if (mat == $idx) return ${colourIndex[colour]};\n")

                            if (material.heldLighting && idx % 4 == 0) {
                                // Removing from their existing item ids
                                (material.mat0 + material.mat1 + material.mat2 + material.mat3).forEach {
                                    val tokens = it.split(":")
                                    text = text.replace("${tokens[0]}:${tokens[1]}", "")
                                }

                                // Adding to new materials
                                if (material.lightColour.size == 1) {
                                    heldLightingMap[44000 + colourIndex[colour]!!] = (heldLightingMap[44000 + colourIndex[colour]!!] ?: "") + " " +
                                            (material.mat0 + material.mat1 + material.mat2 + material.mat3).joinToString(" ") {
                                        val tokens = it.split(":")
                                        "${tokens[0]}:${tokens[1]}"
                                    }
                                } else {
                                    material.lightColour.forEachIndexed { idx, colour ->
                                        if (colour?.tint != false) return@forEachIndexed

                                        val mat = when (idx) {
                                            0 -> material.mat0
                                            1 -> material.mat1
                                            2 -> material.mat2
                                            else -> material.mat3
                                        }
                                        heldLightingMap[44000 + colourIndex[colour]!!] = (heldLightingMap[44000 + colourIndex[colour]!!] ?: "") + " " +
                                                mat.joinToString(" ") {
                                            val tokens = it.split(":")
                                            "${tokens[0]}:${tokens[1]}"
                                        }
                                    }
                                }
                            }
                        }

                        if (conditions) append("    ".repeat(depth) + "#endif\n")
                    }
                }.toString()
            }
        )

        append("        if (mat < 10564) {")
    }.toString()

    heldLightingMap.forEach { (k, v) ->
        builder.append("item.$k = $v\n")
    }

    // Increasing voxel ids of translucent materials to TRANSLUCENT_VOXEL_INITIAL_ID
    voxelisationFile.writeText(
        voxelisationFile.readText().replace(Regex("return (2\\d\\d)")) {
            "return ${it.groupValues[1].toInt() - 200 + TRANSLUCENT_VOXEL_INITIAL_ID}"
        }.replace(">= 200", ">= $TRANSLUCENT_VOXEL_INITIAL_ID")
    )

    // Allowing block entities to have voxelization too
    voxelisationFile.writeText(
        voxelisationFile.readText().replace(
            "int voxelData = GetVoxelIDs(mat);",
            "int voxelData;\n" +
                    "                if (mat == 0 && blockEntityId != 0) voxelData = GetVoxelIDs(blockEntityId);\n" +
                    "                else voxelData = GetVoxelIDs(mat);"
        )
    )

    // Inserting code
    voxelisationFile.writeText(
        voxelisationFile.readText().replace("        if (mat < 10564) {", voxelisationCode)
    )
    itemPropertiesFile.writeText(text + builder)

    // Mapping voxel ids to their colours
    val blocklightFile = File(directory.absolutePathString() + BLOCKLIGHT_PATH)
    val blocklightCode = StringBuilder().apply {
        append("    if (mat >= $VOXELISATION_INITIAL_ID && mat < $TOTAL_COLOURED_VOXELS) {\n")
        append(
            generateCode(
                "mat",
                TOTAL_COLOURED_VOXELS - VOXELISATION_INITIAL_ID,
                VOXELISATION_INITIAL_ID,
                1
            ) {
                if (colourIndexInverted[it] != null) {
                    "return ${colourIndexInverted[it]};"
                } else ""
            }.split("\n").joinToString("\n") { "        $it" }
        )
        append("} else if (mat < 50) {")
    }.toString()

    blocklightFile.writeText(
        blocklightFile.readText().replace("\tif (mat < 50) {", blocklightCode)
    )

    // Increasing voxel ids of translucent materials to TRANSLUCENT_VOXEL_INITIAL_ID
    blocklightFile.writeText(
        blocklightFile.readText().replace(Regex("// (2\\d\\d)")) {
            "// ${it.groupValues[1].toInt() - 200 + TRANSLUCENT_VOXEL_INITIAL_ID}"
        }.replace(
            "void AddSpecialLightDetail",
            "\n\n${COLOUR_INJECTIONS.joinToString("\n\n")}\n\nvoid AddSpecialLightDetail"
        )
    )

    // Generating voxel IDs for translucent materials
    val tintingCode = generateCode(
        "mat",
        256,
        NEW_TINTS_INITIAL_ID,
        1,
        "u"  // unsigned integers
    ) {
        if (colourIndexInverted[it] != null) "return ${colourIndexInverted[it]};" else ""
    }.split("\n").joinToString("\n") { " ".repeat(4) + it }
    blocklightFile.appendText(GET_TINT_CODE.replace("<insert>", tintingCode))

    // Items that provide hand-held lighting
    val mainLightingFile = File(directory.absolutePathString() + MAIN_LIGHTING_PATH)

    val noColouredLightingOriginalCode = "if (heldItemId == 45032) heldLight = 15; if (heldItemId2 == 45032) heldLight2 = 15; // Lava Bucket"
    val noColouredLightingCode = StringBuilder(
        noColouredLightingOriginalCode
    ).apply {
        append("\n")
        ITEM_MAP.forEach { (id, shader) ->
            if (!shader.heldLighting) return@forEach
            append(" ".repeat(12))
            append("if (heldItemId == $id) heldLight = ${shader.lightLevel}; if (heldItemId == $id) heldLight2 = ${shader.lightLevel}; // ${shader.name}\n")
        }
    }.toString()

    val colouredLightingOriginalCode = "if (heldItemId2 == 45032) { heldLightCol2 = lavaSpecialLightColor.rgb; heldLight2 = 15; }"
    val colouredLightingCode = StringBuilder(colouredLightingOriginalCode).apply {
        append("\n")
        ITEM_MAP.forEach { (id, shader) ->
            if (!shader.heldLighting) return@forEach
            append(" ".repeat(12))
            append("if (heldItemId == $id) { heldLightCol = ${shader.lightColour[0]}.rgb; heldLight = ${shader.lightLevel}; };  // ${shader.name}\n")
            append(" ".repeat(12))
            append("if (heldItemId2 == $id) { heldLightCol2 = ${shader.lightColour[0]}.rgb; heldLight2 = ${shader.lightLevel}; };\n")
        }
    }.toString()

    mainLightingFile.writeText(
        mainLightingFile.readText()
            .replace(noColouredLightingOriginalCode, noColouredLightingCode)
            .replace(colouredLightingOriginalCode, colouredLightingCode)
    )
}

const val WAVING_CODE_DIRECTORY = "/shaders/lib/materials/materialMethods/wavingBlocks.glsl"

fun generateWavingCode(directory: Path) {
    val file = File(directory.absolutePathString() + WAVING_CODE_DIRECTORY)
    file.writeText(
        file.readText().replace(
            "void DoWave(inout vec3 playerPos, int mat) {",
            "void DoWave(inout vec3 playerPos, int mat) {\n    DoWave_Block(playerPos, mat);\n"
        )
    )

    // Adding in functions
    val builder = StringBuilder()
    val blockEntityIds = BLOCK_ENTITIES_MAP.filter { it.value.wavingObject != null }.map { it.key }
    builder.append("\n\n")

    with(builder) {
        append("void DoWave_BlockEntity(inout vec3 playerPos, int blockEntityId) {\n")
        append("    vec3 worldPos = playerPos.xyz + cameraPosition.xyz;\n")
        append("    #if defined GBUFFERS_BLOCK || defined SHADOW\n")
        append(
            computeAllPivots(blockEntityIds, 2, "blockEntityId", 4) { idx, depth ->
                StringBuilder().apply {
                    val entity = BLOCK_ENTITIES_MAP[idx]!!
                    val wavingObject = entity.wavingObject!!
                    val conditions = wavingObject.conditions.isNotEmpty()

                    val indent = "    ".repeat(depth)
                    if (conditions) append("$indent#if ${wavingObject.conditions.conditions()}\n")
                    append("${indent}if (blockEntityId >= $idx && blockEntityId < ${idx + 4}) {\n")
                    append("$indent    const int voxelNumber = ${entity.voxelNumber[0]};\n")
                    append(
                        wavingObject.code.split("\n").joinToString("\n") { "$indent    $it" }
                    )
                    append("\n$indent}\n")
                    if (conditions) append("$indent#endif\n")
                }.toString()
            }
        )

        append("    #endif\n")
        append("}\n")
    }

    file.appendText(builder.toString())

    val newBuilder = StringBuilder()

    val materialIds = MATERIALS_MAP.filter { it.value.wavingObject != null }.map { it.key }
    with(newBuilder) {
        append(WAVING_FUNCTIONS.joinToString("\n"))

        append("\n\nvoid DoWave_Block(inout vec3 playerPos, int mat) {\n")
        append("    vec3 worldPos = playerPos.xyz + cameraPosition.xyz;\n")
        append("    #if defined GBUFFERS_TERRAIN || defined SHADOW\n")
        append(
            computeAllPivots(materialIds, 2, "mat", 4) { idx, depth ->
                StringBuilder().apply {
                    val material = MATERIALS_MAP[idx]!!
                    val wavingObject = material.wavingObject!!
                    val conditions = wavingObject.conditions.isNotEmpty()

                    val indent = "    ".repeat(depth)
                    if (conditions) append("$indent#if defined ${wavingObject.conditions.conditions()}\n")
                    append("${indent}if (mat >= $idx && mat < ${idx + 4}) {\n")
                    append("$indent    const int voxelNumber = ${material.voxelNumber[0]};\n")
                    append(
                        wavingObject.code.split("\n").joinToString("\n") { "$indent    $it" }
                    )
                    append("\n$indent}\n")
                    if (conditions) append("$indent#endif\n")
                }.toString()
            }
        )

        append("    #endif\n")
        append("}\n\n")
        append("void DoWave(inout vec3 playerPos, int mat) {")
    }

    file.writeText(file.readText().replace("void DoWave(inout vec3 playerPos, int mat) {", newBuilder.toString()))
}

val PARTICLES = mutableListOf<ShaderBuilder>()

const val PARTICLES_PATH = "/shaders/program/gbuffers_textured.glsl"

fun generateParticleCode(directory: Path) {
    val textureAtlas = Minecraft.getInstance().particleEngine.textureAtlas

    val file = File(directory.absolutePathString() + PARTICLES_PATH)
    val code = file.readText()

    val tokens = code.split("bool noSmoothLighting = false;")
    val lines = tokens[0].split("\n")

    val indent = " ".repeat(4*2)
    val newCode = lines.subList(0, lines.size - 2).joinToString("\n") + StringBuilder().apply {
        var first = true
        PARTICLES.map {
            Pair(it, it.mat0.map { ResourceLocation.parse(it) }.filter { textureAtlas.textures[it] != null })
        }.filter { it.second.isNotEmpty() }.forEach { (it, lst) ->
            if (first) {
                append("\n${indent}if (")
                first = false
            } else {
                append(" else if (")
            }

            append(
                lst.joinToString(" || ") {
                    val sprite = textureAtlas.getSprite(it)
                    "(texCoord.x >= ${sprite.u0} && texCoord.x <= ${sprite.u1} && texCoord.y >= ${sprite.v0} && texCoord.y <= ${sprite.v1})"
                }
            )
            append(") {\n")
            it.glsl.split("\n").forEach { append("$indent    $it\n") }
            append("${indent}}")
        }
        append("\n    }\n    bool noSmoothLighting = false;\n")
    } + tokens[1]

    file.writeText(newCode)
}

val FOG_FUNCTIONS = arrayListOf<String>()
val FOGS = arrayListOf<String>()
const val MAIN_FOG_PATH = "/shaders/lib/atmospherics/fog/mainFog.glsl"

fun generateFog(directory: Path) {
    val file = File(directory.absolutePathString() + MAIN_FOG_PATH)

    val temp = "void DoFog(inout vec3 color"
    file.writeText(
        file.readText().replace(temp, FOG_FUNCTIONS.joinToString("\n\n") + "\n\n" + temp)
    )

    val temp2 = "if (darknessFactor > 0.00001) DoDarknessFog(color, lViewPos);"
    file.writeText(
        file.readText().replace(
            temp2, temp2 + "\n\n" + FOGS.joinToString("\n\n") {
                it.split("\n").joinToString("\n") { "    " + it }
            }
        )
    )
}

const val SHADOW_DIRECTORY = "/shaders/program/shadow.glsl"
const val GBUFFER_BLOCK_DIRECTORY = "/shaders/program/gbuffers_block.glsl"
const val SHADOW_COMP_DIRECTORY = "/shaders/program/shadowcomp.glsl"

fun modifyGBuffers(directory: Path) {
    var file = File(directory.absolutePathString() + SHADOW_DIRECTORY)
    file.writeText(
        file.readText().replace(
            "        DoWave(position.xyz, mat);",
            "        DoWave(position.xyz, mat);\n        DoWave_BlockEntity(position.xyz, blockEntityId);"
        )
    )

    file = File(directory.absolutePathString() + GBUFFER_BLOCK_DIRECTORY)
    file.writeText(
        file.readText()
            .replace("defined WAVE_EVERYTHING", "defined WAVE_EVERYTHING || defined WAVING_ANYTHING_TERRAIN")
            .replace(
            "\n#ifdef WAVE_EVERYTHING",
            "\n#if defined WAVE_EVERYTHING || defined WAVING_ANYTHING_TERRAIN",
            )
            .replace(
                "gl_Position = gl_ProjectionMatrix * gbufferModelView * position;",
                """#ifdef WAVING_ANYTHING_TERRAIN
            DoWave_BlockEntity(position.xyz, blockEntityId);
        #endif
        gl_Position = gl_ProjectionMatrix * gbufferModelView * position;"""
            )
    )

    file = File(directory.absolutePathString() + SHADOW_COMP_DIRECTORY)
    file.writeText(
        file.readText().replace(
            "specialTintColor[min(voxel - 200u, specialTintColor.length() - 1u)]", "GetSpecialTintColor(voxel)"
        ).replace("200u", "${TRANSLUCENT_VOXEL_INITIAL_ID}u")
    )
}
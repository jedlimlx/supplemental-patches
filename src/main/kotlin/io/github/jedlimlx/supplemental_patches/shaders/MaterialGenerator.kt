package io.github.jedlimlx.supplemental_patches.shaders

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import java.io.File
import java.nio.file.Path
import kotlin.collections.flatten
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

const val TERRAIN_INITIAL_ID = 12288
const val TERRAIN_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/terrainMaterials.glsl"

fun generateTerrainMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + TERRAIN_MATERIALS_PATH)
    MATERIALS.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("mat", 2048, TERRAIN_INITIAL_ID) { size, it ->
        if (count < MATERIALS.size) {
            if (size > MATERIALS[count].blockSize) return@generateCode null
            val material = MATERIALS[count]
            MATERIALS_MAP[it] = material

            val code = "// block.$it = ${material.name}\n${material.glsl}"
            count++

            code
        } else if (size > 4) null else "// block.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    file.writeText(Regex("#endif\\r?\\n}").replace(oldCode, "#endif\n} else $code"))

    // writing the list of blocks to block.properties
    val blockPropertiesFile = File(directory.absolutePathString() + BLOCK_PROPERTIES)

    var text = blockPropertiesFile.readText()
    val builder = StringBuilder("\n$BANNER# Blocks added by Supplemental Patches\n\n")
    MATERIALS_MAP.forEach { (id, material) ->
        // Removing from their existing materials
        material.mat.forEach { it.forEach { text = removeId(it, text) } }

        // Adding to new materials
        builder.apply {
            var count = 0
            material.mat.forEach {
                if (it.isNotEmpty())
                    append("block.${id + count} = ${it.joinToString(" ")}\n")
                count++
            }
        }
    }

    blockPropertiesFile.writeText(text + builder)
}

val ENTITIES = mutableListOf<ShaderBuilder>()
val ENTITY_MAP = mutableMapOf<Int, ShaderBuilder>()

const val ENTITY_INITIAL_ID = 51200
const val ENTITY_PATH = "/shaders/lib/materials/materialHandling/entityMaterials.glsl"

fun generateEntityMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + ENTITY_PATH)
    ENTITIES.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("entityId", 256, ENTITY_INITIAL_ID) { size, it ->
        if (count < ENTITIES.size) {
            if (size > ENTITIES[count].blockSize) return@generateCode null
            val entity = ENTITIES[count]
            ENTITY_MAP[it] = entity

            val code = "// entity.$it = ${entity.name}\n${entity.glsl}"
            count++

            code
        } else if (size > 4) null else "// entity.$it"
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
        material.mat.forEach { it.forEach { text = removeId(it, text) } }

        // Adding to new materials
        builder.apply {
            var count = 0
            material.mat.forEach {
                if (it.isNotEmpty())
                    append("entity.${id + count} = ${it.joinToString(" ")}\n")
                count++
            }
        }
    }

    entityPropertiesFile.writeText(text + builder)
}

val ITEMS = mutableListOf<ShaderBuilder>()
val ITEM_MAP = mutableMapOf<Int, ShaderBuilder>()

const val IRIS_MATERIALS_INITIAL_ID = 46080
const val IRIS_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/irisMaterials.glsl"

fun generateIrisMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + IRIS_MATERIALS_PATH)
    ITEMS.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("currentRenderedItemId", 256, IRIS_MATERIALS_INITIAL_ID) { size, it ->
        if (count < ITEMS.size) {
            if (size > ITEMS[count].blockSize) return@generateCode null
            val item = ITEMS[count]
            ITEM_MAP[it] = item

            val code = "// item.$it = ${item.name}\n${item.glsl}"
            count++

            code
        } else if (size > 4) null else "// item.$it"
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
        material.mat.forEach { it.forEach { text = removeId(it, text) } }

        // Adding to new materials
        builder.apply {
            var count = 0
            material.mat.forEach {
                if (it.isNotEmpty())
                    append("item.${id + count} = ${it.joinToString(" ")}\n")
                count++
            }
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
    TRANSLUCENTS.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("mat", 256, TRANSLUCENT_INITIAL_ID) { size, it ->
        if (count < TRANSLUCENTS.size) {
            if (size > TRANSLUCENTS[count].blockSize) return@generateCode null
            val translucent = TRANSLUCENTS[count]
            TRANSLUCENTS_MAP[it] = translucent

            val code = "// block.$it = ${translucent.name}\n${translucent.glsl}"
            count++

            code
        } else if (size > 4) null else "// block.$it"
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
        // Removing from their existing materials
        material.mat.forEach { it.forEach { text = removeId(it, text) } }

        // Adding to new materials
        builder.apply {
            var count = 0
            material.mat.forEach {
                if (it.isNotEmpty())
                    append("block.${id + count} = ${it.joinToString(" ")}\n")
                count++
            }
        }
    }

    blockPropertiesFile.writeText(text + builder)
}

val BLOCK_ENTITIES = mutableListOf<ShaderBuilder>()
val BLOCK_ENTITIES_MAP = mutableMapOf<Int, ShaderBuilder>()

const val BLOCK_ENTITY_INITIAL_ID = 5056
const val BLOCK_ENTITY_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/blockEntityMaterials.glsl"

fun generateBlockEntityMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + BLOCK_ENTITY_MATERIALS_PATH)
    BLOCK_ENTITIES.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("blockEntityId", 256, BLOCK_ENTITY_INITIAL_ID) { size, it ->
        if (count < BLOCK_ENTITIES.size) {
            if (size > BLOCK_ENTITIES[count].blockSize) return@generateCode null
            val blockEntity = BLOCK_ENTITIES[count]
            BLOCK_ENTITIES_MAP[it] = blockEntity

            val code = "// block.$it = ${blockEntity.name}\n${blockEntity.glsl}"
            count++

            code
        } else if (size > 4) null else "// block.$it"
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
        // Removing from their existing materials
        material.mat.forEach { it.forEach { text = removeId(it, text) } }

        // Adding to new materials
        builder.apply {
            var count = 0
            material.mat.forEach {
                if (it.isNotEmpty())
                    append("block.${id + count} = ${it.joinToString(" ")}\n")
                count++
            }
        }
    }

    blockPropertiesFile.writeText(text + builder)
}

const val VOXELISATION_INITIAL_ID = 100
const val VOXELISATION_PATH = "/shaders/lib/misc/voxelization.glsl"

const val TOTAL_COLOURED_VOXELS = 256 + VOXELISATION_INITIAL_ID
const val TRANSLUCENT_VOXEL_INITIAL_ID = 60000
const val NEW_TINTS_INITIAL_ID = 60020

const val BLOCKLIGHT_PATH = "/shaders/lib/colors/blocklightColorsACL.glsl"
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
            (0..< material.blockSize).forEach {
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
            (0..< material.blockSize).forEach { material.voxelNumber[it] = TOTAL_COLOURED_VOXELS + count++ }
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
            if (material.lightColour.size == 1) id ..< id + material.blockSize
            else if (material.lightColour.isNotEmpty())
                (0 ..< material.blockSize).filter { material.lightColour[it] != null }.map { it + id }
            else if (material.needsVoxelisation) {
                id ..< id + material.blockSize
            } else listOf()
        }.flatten()).sorted()

        append(
            computeAllPivots(output, 2, "mat", 1) { idx, depth ->
                StringBuilder().apply {
                    var count = 2
                    val material: ShaderBuilder
                    while (true) {
                        val output = temp[idx - idx % (1 shl count++)]
                        if (output != null) {
                            material = output
                            break
                        }
                    }

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

                            if (material.heldLighting && idx % material.blockSize == 0) {
                                // Removing from their existing item ids
                                material.allIds().forEach {
                                    val tokens = it.split(":")
                                    text = text.replace("${tokens[0]}:${tokens[1]}", "")
                                }

                                // Adding to new materials
                                if (material.lightColour.size == 1) {
                                    val temp = material.allIds().joinToString(" ") {
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

                                        val mat = material.mat[idx]
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

                            if (material.heldLighting && idx % material.blockSize == 0) {
                                // Removing from their existing item ids
                                material.allIds().forEach {
                                    val tokens = it.split(":")
                                    text = text.replace("${tokens[0]}:${tokens[1]}", "")
                                }

                                // Adding to new materials
                                if (material.lightColour.size == 1) {
                                    heldLightingMap[44000 + colourIndex[colour]!!] = (heldLightingMap[44000 + colourIndex[colour]!!] ?: "") + " " +
                                            material.allIds().joinToString(" ") {
                                        val tokens = it.split(":")
                                        "${tokens[0]}:${tokens[1]}"
                                    }
                                } else {
                                    material.lightColour.forEachIndexed { idx, colour ->
                                        if (colour?.tint != false) return@forEachIndexed

                                        val mat = material.mat[idx]
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
//    voxelisationFile.writeText(
//        voxelisationFile.readText().replace(
//            "int voxelData = GetVoxelIDs(mat);",
//            "int voxelData;\n" +
//                    "                voxelData = GetVoxelIDs(mat);"
//        )
//    )

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
                    append("${indent}if (blockEntityId >= $idx && blockEntityId < ${idx + entity.blockSize}) {\n")
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
                    if (conditions) append("$indent#if ${wavingObject.conditions.conditions()}\n")
                    append("${indent}if (mat >= $idx && mat < ${idx + material.blockSize}) {\n")
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

// TODO fix some particles not having shaders applied properly
fun generateParticleCode(directory: Path) {
    val textureAtlas = Minecraft.getInstance().particleEngine.textureAtlas

    val file = File(directory.absolutePathString() + PARTICLES_PATH)
    val code = file.readText()

    val newCode = "if (atlasSize.x < atlasCheck) {\n        vec2 texCoordScaled = $SCALE * texCoord;\n" + StringBuilder().apply {
        append(
            split(
                PARTICLES.map {
                    Pair(it, it.mat[0].map { ResourceLocation.parse(it) }.filter { it in textureAtlas.texturesByName.keys })
                }.filter { it.second.isNotEmpty() }.map { (particle, lst) ->
                    val rectangles = lst.map {
                        val sprite = textureAtlas.getSprite(it)
                        Rectangle(
                            (sprite.u0 * SCALE).toInt(),
                            (sprite.v0 * SCALE).toInt(),
                            (sprite.u1 * SCALE).toInt() - 1,
                            (sprite.v1 * SCALE).toInt() - 1,
                            particle.glsl
                        )
                    }

                    val sortedRectangles = rectangles.sortedWith { a, b ->
                        if (a.x1 == b.x1) a.y1.compareTo(b.y1) else a.x1.compareTo(b.x2)
                    }

                    var currRectangle: Rectangle? = null
                    val mergedRectangles = arrayListOf<Rectangle>()
                    for (rectangle in sortedRectangles) {
                        if (currRectangle == null) {
                            currRectangle = rectangle
                            continue
                        }

                        if (currRectangle.canMergeX(rectangle)) {
                            currRectangle.mergeX(rectangle)
                        } else {
                            mergedRectangles.add(currRectangle)
                            currRectangle = null
                        }
                    }

                    if (currRectangle != null)
                        mergedRectangles.add(currRectangle)

                    val sortedRectangles2 = mergedRectangles.sortedWith { a, b ->
                        if (a.y1 == b.y1) a.x1.compareTo(b.x2) else a.y1.compareTo(b.y2)
                    }

                    mergedRectangles.clear()
                    currRectangle = null
                    for (rectangle in sortedRectangles2) {
                        if (currRectangle == null) {
                            currRectangle = rectangle
                            continue
                        }

                        if (currRectangle.canMergeY(rectangle)) {
                            currRectangle.mergeY(rectangle)
                        } else {
                            mergedRectangles.add(currRectangle)
                            currRectangle = null
                        }
                    }

                    if (currRectangle != null)
                        mergedRectangles.add(currRectangle)

                    mergedRectangles
                }.flatten()
            )
        )
        append("    }\n    bool noSmoothLighting = false;\n")
    }.toString()

    file.writeText(
        Regex("if \\(atlasSize.x < atlasCheck\\) \\{[\\s\\S]*bool noSmoothLighting = false;", RegexOption.MULTILINE).replaceFirst(code, newCode)
    )
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
    var file = File(directory.absolutePathString() + SHADER_PROPERTIES_FILE)
    file.writeText(
        file.readText().replace(
            "voxel_sampler red_integer r8ui",
            "voxel_sampler red_integer r16ui"
        )
    )

    file = File(directory.absolutePathString() + SHADOW_DIRECTORY)
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
            .replace(
                "flat in vec2 absMidCoordPos;",
                "flat in vec2 absMidCoordPos;\n" +
                        "    flat in vec2 midCoord;"
            )
            .replace(
                "#include \"/lib/materials/materialHandling/blockEntityMaterials.glsl\"",
                "#include \"/lib/materials/materialHandling/blockEntityMaterials.glsl\"\n" +
                "\n" +
                "        #ifdef IS_IRIS\n" +
                "            #include \"/lib/materials/materialHandling/irisMaterials.glsl\"\n" +
                "        #endif"
            )
            .replace(
                "defined GENERATED_NORMALS || defined COATED_TEXTURES || defined POM || SHOCKWAVE > 0",
                "defined IS_IRIS || defined GENERATED_NORMALS || defined COATED_TEXTURES || defined POM || SHOCKWAVE > 0"
            )
            .replace(
                "color *= glColor;",
                "color *= glColor;\n" +
                "    \n" +
                "    float luminance = GetLuminance(color.rgb);"
            )
    )

    file = File(directory.absolutePathString() + SHADOW_COMP_DIRECTORY)
    file.writeText(
        file.readText().replace(
            "specialTintColor[min(voxel - 200u, specialTintColor.length() - 1u)]", "GetSpecialTintColor(voxel)"
        ).replace("200u", "${TRANSLUCENT_VOXEL_INITIAL_ID}u")
    )

    file = File(directory.absolutePathString() + IRIS_MATERIALS_PATH)
    file.writeText(
        file.readText().replace(
            "#include \"/lib/materials/materialHandling/terrainMaterials.glsl\"",
            "#ifdef DISTANT_LIGHT_BOKEH\n" +
            "        #undef DISTANT_LIGHT_BOKEH\n" +
            "        #include \"/lib/materials/materialHandling/terrainMaterials.glsl\"\n" +
            "        #define DISTANT_LIGHT_BOKEH\n" +
            "    #else\n" +
            "        #include \"/lib/materials/materialHandling/terrainMaterials.glsl\"\n" +
            "    #endif"
        )
        .replace(
            "int subsurfaceMode;",
            "#if defined GBUFFERS_ENTITIES || defined GBUFFERS_HAND\n" +
            "    int subsurfaceMode;\n" +
            "#endif\n" +
            "\n" +
            "#if defined GBUFFERS_BLOCK\n" +
            "    float skyLightCheck = 0.0;\n" +
            "    float overlayNoiseEmission;\n" +
            "    vec3 maRecolor;\n" +
            "    bool noGeneratedNormals;\n" +
            "    bool noVanillaAO;\n" +
            "#endif\n"
        )
    )
}
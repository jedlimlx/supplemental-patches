package io.github.jedlimlx.supplemental_patches.shaders

import io.github.jedlimlx.supplemental_patches.PLATFORM
import io.github.jedlimlx.supplemental_patches.LOGGER
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

var PACK_JSON = ""
val PACK_JSON_PATH = "/shaders/pack.json"
fun injectPackJson(directory: Path) {
	val file = File(directory.absolutePathString() + PACK_JSON_PATH)
	file.writeText(PACK_JSON)
}

val BLOCK_ADDITIONAL_MAPPING = mutableMapOf<Int, List<String>>()
val BLOCK_REGEX_REPLACES = arrayListOf<Regex>()

val ITEM_ADDITIONAL_MAPPING = mutableMapOf<Int, List<String>>()
val ITEM_REGEX_REPLACES = arrayListOf<Regex>()

val ENTITY_ADDITIONAL_MAPPING = mutableMapOf<Int, List<String>>()
val ENTITY_REGEX_REPLACES = arrayListOf<Regex>()

val LAYER_CHANGES = mutableMapOf<String, ArrayList<String>>()

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

fun modifyLayers(directory: Path) {
    val file = File(directory.absolutePathString() + BLOCK_PROPERTIES)

    var code = file.readText()
    LAYER_CHANGES.forEach { (layer, lst) ->
        code = code.replace("layer.$layer = ", "layer.$layer = ${lst.joinToString(" ")} \\\n\\\n")
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

fun createPropertiesMap(materialType: String, text: String): HashMap<Int, HashSet<String>> {
    val properties = HashMap<Int, HashSet<String>>()

    Regex("(?<!#)$materialType.(?<id>\\d+) *= *(?<entries>(?:[^\\n\\r\\\\]*(?:\\\\\\r?\\n?)?)+)")
        .findAll(text).forEach { result ->
            val id = result.groups["id"]?.value?.toInt() ?: return@forEach
            val entriesString = result.groups["entries"]?.value ?: return@forEach

            val entrySet = properties.getOrDefault(id, HashSet())
            entrySet.addAll(Regex("[^\\n\\r\\\\ ]+").findAll(entriesString).map { entry -> entry.value.replace("minecraft:", "") })
            properties[id] = entrySet
        }

    return properties
}

fun updateExistingMaterials(materialType: String, text: String, changedProperties: HashMap<Int, HashSet<String>>): String {
    var updatedText = text

    changedProperties.forEach { (id, entrySet) ->
        updatedText = Regex("(?<!#)$materialType.$id *= *(?<entries>(?:[^\\n\\r\\\\]*(?:\\\\\\r?\\n?)?)+)")
            .replace(updatedText) { result ->
                var replacement = result.value
                entrySet.forEach { entry -> replacement = removeId(entry, replacement) }
                return@replace replacement
            }
    }

    return updatedText
}

fun updatePropertiesFile(path: String, materials: Map<Int, ShaderBuilder>, materialType: String, banner: String) {
    val propertiesFile = File(path)
    val text = propertiesFile.readText()

    val properties = createPropertiesMap(materialType, text)
    val changedProperties = HashMap<Int, HashSet<String>>()

    val builder = StringBuilder(banner)
    materials.forEach { (id, material) ->
        builder.apply {
            var count = 0
            material.mat.forEach {
                if (it.isNotEmpty()) {
                    // Removing from their existing materials
                    it.forEach { entry ->
                        properties.replaceAll { id, entrySet ->
                            val entry = entry.replace("minecraft:", "")
                            if (entrySet.remove(entry)) {
                                val changedSet = changedProperties.getOrDefault(id, HashSet())
                                changedSet.add(entry)
                                changedProperties[id] = changedSet
                            }
                            return@replaceAll entrySet
                        }
                    }
                    // Adding to new materials
                    append("$materialType.${id + count} = ${it.joinToString(" ")}\n")
                }
                count++
            }
        }
    }

    propertiesFile.writeText(
        updateExistingMaterials(materialType, text, changedProperties) + builder
    )
}

val MATERIALS = mutableListOf<ShaderBuilder>()
var FILTERED_MATERIALS = mutableListOf<ShaderBuilder>()
val MATERIALS_MAP = mutableMapOf<Int, ShaderBuilder>()

const val TERRAIN_INITIAL_ID = 12288
const val TERRAIN_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/terrainIPBR.glsl"

fun generateTerrainMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + TERRAIN_MATERIALS_PATH)
    FILTERED_MATERIALS.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("mat", FILTERED_MATERIALS.sumOf { it.blockSize }, TERRAIN_INITIAL_ID) { size, it ->
        if (count < FILTERED_MATERIALS.size) {
            if (size > FILTERED_MATERIALS[count].blockSize) return@generateCode null
            val material = FILTERED_MATERIALS[count]
            MATERIALS_MAP[it] = material

            val code = "// block.$it = ${material.name}\n${material.glsl}"
            count++

            code
        } else if (size > 4) null else "// block.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    file.writeText(Regex("#endif\\r?\\n}").replace(oldCode, "#endif\n} else if (mat != 0 && mat != 65535) {\n${code.prependIndent("    ")}\n}\n"))

    // writing the list of blocks to block.properties
    updatePropertiesFile(
        directory.absolutePathString() + BLOCK_PROPERTIES,
        MATERIALS_MAP,
        "block",
        "\n$BANNER# Blocks added by Supplemental Patches\n\n"
    )
}

val ENTITIES = mutableListOf<ShaderBuilder>()
var FILTERED_ENTITIES = mutableListOf<ShaderBuilder>()
val ENTITY_MAP = mutableMapOf<Int, ShaderBuilder>()

const val ENTITY_INITIAL_ID = 51200
const val ENTITY_PATH = "/shaders/lib/materials/materialHandling/entityIPBR.glsl"

fun generateEntityMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + ENTITY_PATH)
    FILTERED_ENTITIES = ENTITIES.filter { it.required() }.toMutableList()
    FILTERED_ENTITIES.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("entityId", FILTERED_ENTITIES.sumOf { it.blockSize }, ENTITY_INITIAL_ID) { size, it ->
        if (count < FILTERED_ENTITIES.size) {
            if (size > FILTERED_ENTITIES[count].blockSize) return@generateCode null
            val entity = FILTERED_ENTITIES[count]
            ENTITY_MAP[it] = entity

            val code = "// entity.$it = ${entity.name}\n${entity.glsl}"
            count++

            code
        } else if (size > 4) null else "// entity.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    val lines = oldCode.replace("\n} else {", "\n} else if (entityId < 50256) {").split("\n")
    val newCode = lines.joinToString("\n") + " else if (entityId != 65535) {\n${code.prependIndent("    ")}\n}\n"
    file.writeText(newCode)

    // writing the list of entities to entity.properties
    updatePropertiesFile(
        directory.absolutePathString() + ENTITY_PROPERTIES,
        ENTITY_MAP,
        "entity",
        "\n$BANNER# Entities added by Supplemental Patches\n\n"
    )
}

val ITEMS = mutableListOf<ShaderBuilder>()
var FILTERED_ITEMS = mutableListOf<ShaderBuilder>()
val ITEM_MAP = mutableMapOf<Int, ShaderBuilder>()

const val IRIS_MATERIALS_INITIAL_ID = 46080
const val IRIS_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/irisIPBR.glsl"

fun generateIrisMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + IRIS_MATERIALS_PATH)
    FILTERED_ITEMS = ITEMS.filter { it.required() }.toMutableList()
    FILTERED_ITEMS.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("currentRenderedItemId", FILTERED_ITEMS.sumOf { it.blockSize }, IRIS_MATERIALS_INITIAL_ID) { size, it ->
        if (count < FILTERED_ITEMS.size) {
            if (size > FILTERED_ITEMS[count].blockSize) return@generateCode null
            val item = FILTERED_ITEMS[count]
            ITEM_MAP[it] = item

            val code = "// item.$it = ${item.name}\n${item.glsl}"
            count++

            code
        } else if (size > 4) null else "// item.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    val lines = oldCode.replace("\n} else {", "\n} else if (currentRenderedItemId < 45128) {").split("\n")
    val newCode = lines.joinToString("\n") + " else if (currentRenderedItemId != 65535) {\n${code.prependIndent("    ")}\n}\n"
    file.writeText(newCode)

    // writing the list of items to item.properties
    updatePropertiesFile(
        directory.absolutePathString() + ITEM_PROPERTIES,
        ITEM_MAP,
        "item",
        "\n"
    )
}

val TRANSLUCENTS = mutableListOf<ShaderBuilder>()
var FILTERED_TRANSLUCENTS = mutableListOf<ShaderBuilder>()
val TRANSLUCENTS_MAP = mutableMapOf<Int, ShaderBuilder>()

const val TRANSLUCENT_INITIAL_ID = 32128
const val TRANSLUCENT_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/translucentIPBR.glsl"

fun generateTranslucentMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + TRANSLUCENT_MATERIALS_PATH)
    FILTERED_TRANSLUCENTS.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("mat", FILTERED_TRANSLUCENTS.sumOf { it.blockSize }, TRANSLUCENT_INITIAL_ID) { size, it ->
        if (count < FILTERED_TRANSLUCENTS.size) {
            if (size > FILTERED_TRANSLUCENTS[count].blockSize) return@generateCode null
            val translucent = FILTERED_TRANSLUCENTS[count]
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
    val newCode = lines.joinToString("\n") + " else if (mat != 65535) {\n${code.prependIndent("    ")}\n}\n"
    file.writeText(newCode)

    // writing the list of blocks to block.properties
    updatePropertiesFile(
        directory.absolutePathString() + BLOCK_PROPERTIES,
        TRANSLUCENTS_MAP,
        "block",
        "\n# Translucent materials added by Supplemental Patches\n\n"
    )
}

val BLOCK_ENTITIES = mutableListOf<ShaderBuilder>()
var FILTERED_BLOCK_ENTITIES = mutableListOf<ShaderBuilder>()
val BLOCK_ENTITIES_MAP = mutableMapOf<Int, ShaderBuilder>()

const val BLOCK_ENTITY_INITIAL_ID = 5056
const val BLOCK_ENTITY_MATERIALS_PATH = "/shaders/lib/materials/materialHandling/blockEntityIPBR.glsl"

fun generateBlockEntityMaterials(directory: Path) {
    val file = File(directory.absolutePathString() + BLOCK_ENTITY_MATERIALS_PATH)
    FILTERED_BLOCK_ENTITIES.sortBy { -it.blockSize }

    var count = 0
    val code = generateCode("blockEntityId", FILTERED_BLOCK_ENTITIES.sumOf { it.blockSize }, BLOCK_ENTITY_INITIAL_ID) { size, it ->
        if (count < FILTERED_BLOCK_ENTITIES.size) {
            if (size > FILTERED_BLOCK_ENTITIES[count].blockSize) return@generateCode null
            val blockEntity = FILTERED_BLOCK_ENTITIES[count]
            BLOCK_ENTITIES_MAP[it] = blockEntity

            val code = "// block.$it = ${blockEntity.name}\n${blockEntity.glsl}"
            count++

            code
        } else if (size > 4) null else "// block.$it"
    }
    val oldCode = file.readText()

    // injecting code into the old code
    val lines = oldCode.replace("\n} else {", "\n} else if (blockEntityId < 5056 || blockEntityId == 10548) {").split("\n")
    val newCode = lines.joinToString("\n") + " else if (blockEntityId != 65535) {\n${code.prependIndent("    ")}\n}\n"
    file.writeText(newCode)

    // writing the list of blocks to block.properties
    updatePropertiesFile(
        directory.absolutePathString() + BLOCK_PROPERTIES,
        BLOCK_ENTITIES_MAP,
        "block",
        "\n# Block entities added by Supplemental Patches\n\n"
    )
}

const val VOXELISATION_INITIAL_ID = 100
const val VOXELISATION_PATH = "/shaders/lib/voxelization/lightVoxelization.glsl"

const val TOTAL_COLOURED_VOXELS = 256 + VOXELISATION_INITIAL_ID
const val TRANSLUCENT_VOXEL_INITIAL_ID = 30000
const val NEW_TINTS_INITIAL_ID = 30020

const val BLOCKLIGHT_PATH = "/shaders/lib/colors/blocklightColorsACT.glsl"
const val MAIN_LIGHTING_PATH = "/shaders/lib/lighting/mainLighting.glsl"

const val GET_TINT_CODE = """

vec3 GetSpecialTintColor(uint mat) {
    if (mat < 30019u) return specialTintColor[mat - 30000u];
    else {
<insert>
    }

    return vec3(0.0);
}
"""

fun assignVoxelNumbers() {
    FILTERED_MATERIALS = MATERIALS.filter { it.required() }.toMutableList()
    FILTERED_TRANSLUCENTS = TRANSLUCENTS.filter { it.required() }.toMutableList()
    FILTERED_BLOCK_ENTITIES = BLOCK_ENTITIES.filter { it.required() }.toMutableList()

    val colourIndex = (TINTS + COLOURS).filter { it.index == -1 }.mapIndexed { idx, colour ->
        colour to ((if (colour.tint) NEW_TINTS_INITIAL_ID else VOXELISATION_INITIAL_ID) + idx)
    }.toMap()

    var count = 0
    (FILTERED_MATERIALS + FILTERED_BLOCK_ENTITIES + FILTERED_TRANSLUCENTS).forEach { material ->
		if (material.blocklight.isNotEmpty()) {
			material.blocklight.forEach { (colours, _) ->
				val voxels = IntArray(material.blockSize) { -1 }
				if (colours.size == 1) {
					val colour = colours[0]!!
					(0..<material.blockSize).forEach {
						voxels[it] =
							colourIndex[colour] ?: (colour.index + (if (colour.tint) NEW_TINTS_INITIAL_ID else 0))
					}
				} else if (colours.isNotEmpty()) {
					colours.forEachIndexed { it, colour ->
						colour ?: return@forEachIndexed
						voxels[it] =
							colourIndex[colour] ?: (colour.index + (if (colour.tint) NEW_TINTS_INITIAL_ID else 0))
					}
				}

				material.voxelNumber.add(voxels)
			}
		} else if (material.needsVoxelisation)
            (0..< material.blockSize).forEach {
				val voxels = IntArray(material.blockSize) { TOTAL_COLOURED_VOXELS + count++ }
				material.voxelNumber.add(voxels)
			}
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
			if (material.blocklight.isNotEmpty()) {
				if (material.blocklight.all { it.first.size == 1 }) id ..< id + material.blockSize
				else  (0 ..< material.blockSize).filter {
					material.blocklight.all { (lst, _) -> lst[minOf(it, lst.size - 1)] != null }
				}.map { it + id }
			} else if (material.needsVoxelisation) {
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

                    if (material.needsVoxelisation && material.blocklight.isEmpty()) {
                        append("    ".repeat(depth) + "if (mat == $idx) return ${material.voxelNumber[0][0]};\n")
                    } else {
						val hasConditions = material.blocklight.first().second.isNotEmpty()
						material.blocklight.forEachIndexed { it, (colours, conditions) ->
							val colour = colours[idx % colours.size]!!
							if (hasConditions) {
								append(
									"    ".repeat(depth) + "#" + if (it == 0) "if" else {
										if (conditions.isEmpty()) "else"
										else "elif"
									} + " ${conditions.conditions()}\n"
								)
							}
							append("    ".repeat(depth))

							if (colour.index != -1) {
								val index = colour.index + (if (colour.tint) NEW_TINTS_INITIAL_ID else 0)
								append("if (mat == $idx) return $index;\n")

								if (material.heldLighting && idx % material.blockSize == 0) {
									// Removing from their existing item ids
									material.allIds().forEach {
										text = removeId(it, text)
									}

									// Adding to new materials
									if (colours.size == 1) {
										val temp = material.allIds().joinToString(" ") {
											val tokens = it.split(":")
											"${tokens[0]}:${tokens[1]}"
										}
										text = text.replace(
											"item.${44000 + index} =",
											"item.${44000 + index} = " + temp
										)
									} else {
										colours.forEachIndexed { idx, colour ->
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
										text = removeId(it, text)
									}

									// Adding to new materials
									if (colours.size == 1) {
										heldLightingMap[44000 + colourIndex[colour]!!] = (heldLightingMap[44000 + colourIndex[colour]!!] ?: "") + " " +
											material.allIds().joinToString(" ") {
												val tokens = it.split(":")
												"${tokens[0]}:${tokens[1]}"
											}
									} else {
										colours.forEachIndexed { idx, colour ->
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

							if (hasConditions && it == material.blocklight.size - 1) append("    ".repeat(depth) + "#endif\n")
						}
                    }
                }.toString()
            }
        )

        append("        if (mat < 10604) {")
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
        voxelisationFile.readText().replace("        if (mat < 10604) {", voxelisationCode)
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
            append("if (heldItemId == $id) { heldLightCol = ${shader.blocklight[0].first[0]}.rgb; heldLight = ${shader.lightLevel}; };  // ${shader.name}\n")
            append(" ".repeat(12))
            append("if (heldItemId2 == $id) { heldLightCol2 = ${shader.blocklight[0].first[0]}.rgb; heldLight2 = ${shader.lightLevel}; };\n")
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
                    append("$indent    const int voxelNumber = ${entity.voxelNumber[0][0]};\n")
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
                    append("$indent    const int voxelNumber = ${material.voxelNumber[0][0]};\n")
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

const val REFLECTION_VOXELISATION_DIRECTORY = "/shaders/lib/voxelization/reflectionVoxelization.glsl"
fun generateReflectionHanders(directory: Path) {
    val temp = "vec2 origin = mc_midTexCoord.xy - textureRad;"
    val file = File(directory.absolutePathString() + REFLECTION_VOXELISATION_DIRECTORY)

    val lst = arrayListOf<Int>()
    val map = hashMapOf<Int, String>()
    MATERIALS_MAP.forEach { (idx, it) ->
        var count = 0
        it.reflectionHandlers.forEach {
            if (it != null) {
                map[idx + count] = it
                lst.add(idx + count++)
            }
        }
    }

    file.writeText(
        file.readText().replace(temp,
            temp + "\n" + computeAllPivots(lst, 3, "mat", 1) { idx, depth ->
                val indent = "    ".repeat(depth)
                StringBuilder().apply {
                    append("${indent}if (mat == $idx) {\n")
                    append(map[idx]!!.prependIndent("$indent    "))
                    append("\n$indent}\n")
                }.toString()
            }
        )
    )
}

val PARTICLES = mutableListOf<ShaderBuilder>()

const val PARTICLES_PATH = "/shaders/program/gbuffers_textured.glsl"

const val SCALE = 16384
fun generateParticleCode(directory: Path) {
    val textureAtlas = PLATFORM.particleAtlas!!

    val file = File(directory.absolutePathString() + PARTICLES_PATH)
    var code = file.readText()
    code = code.replace("float atlasCheck = 1100.0; //", "float atlasCheck = 5000.0; //")

    try {
        val newCode = "if (tSize.x < atlasCheck) {\n" +
                "        vec2 texCoordScaled = texCoord * $SCALE;\n" + StringBuilder().apply {
            append(
                split(
                    PARTICLES.map {
                        Pair(it, it.mat[0].map { PLATFORM.getResourceLocation(it) }.filter { it in PLATFORM.particleAtlasTextures })
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
                            if (a.x1 == b.x1) a.y1.compareTo(b.y1) else a.x1.compareTo(b.x1)
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
                                currRectangle = rectangle
                            }
                        }

                        if (currRectangle != null) {
                            mergedRectangles.add(currRectangle)
                        }

                        val sortedRectangles2 = mergedRectangles.sortedWith { a, b ->
                            if (a.y1 == b.y1) a.x1.compareTo(b.x1) else a.y1.compareTo(b.y1)
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
                                currRectangle = rectangle
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
            Regex("if \\(tSize.x < atlasCheck\\) \\{[\\s\\S]*bool noSmoothLighting = false;", RegexOption.MULTILINE).replaceFirst(code, newCode)
        )
    } catch (e: StackOverflowError) {
        throw MinecraftError("Particle shaders could not be generated, due to stack overflow. Check if the same particle is listed by more than one *.json file.", null)
    }
}

val FOG_FUNCTIONS = arrayListOf<String>()
val FOGS = arrayListOf<String>()
val ACT_FOGS = arrayListOf<String>()
const val MAIN_FOG_PATH = "/shaders/lib/atmospherics/fog/mainFog.glsl"
const val ACT_FOG_PATH = "/shaders/lib/atmospherics/fog/coloredLightFog.glsl"

fun generateFog(directory: Path) {
    val file = File(directory.absolutePathString() + MAIN_FOG_PATH)

    val temp = "void DoFog(inout vec4 color"
    file.writeText(
        file.readText().replace(temp, FOG_FUNCTIONS.joinToString("\n\n") + "\n\n" + temp)
    )

    val temp2 = "if (darknessFactor > 0.00001) DoDarknessFog(color, lViewPos);"
    file.writeText(
        file.readText().replace(
            temp2, temp2 + "\n\n" + FOGS.joinToString("\n\n") {
                it.split("\n").joinToString("\n") { "    $it" }
            }
        )
    )

    val temp3 = "lightSample *= pow2(min1(lTracePos * 0.03125));"
    val actFogFile = File(directory.absolutePathString() + ACT_FOG_PATH)
    actFogFile.writeText(
        actFogFile.readText().replace(
            temp3, temp3 + "\n\n" + ACT_FOGS.joinToString("\n\n") {
                it.split("\n").joinToString("\n") { "        $it" }
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
            .replace(
                "flat in vec2 absMidCoordPos;",
                "flat in vec2 absMidCoordPos;\n" +
                        "    flat in vec2 midCoord;"
            )
            .replace(
                "flat out vec2 absMidCoordPos;",
                "flat out vec2 absMidCoordPos;\n" +
                        "    flat out vec2 midCoord;"
            )
            .replace(
                "#include \"/lib/materials/materialHandling/blockEntityIPBR.glsl\"",
                "#include \"/lib/materials/materialHandling/blockEntityIPBR.glsl\"\n" +
                "\n" +
                "        #ifdef IS_IRIS\n" +
                "            #include \"/lib/materials/materialHandling/irisIPBR.glsl\"\n" +
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
            "#include \"/lib/materials/materialHandling/terrainIPBR.glsl\"",
            "#ifdef DISTANT_LIGHT_BOKEH\n" +
            "        #undef DISTANT_LIGHT_BOKEH\n" +
            "        #include \"/lib/materials/materialHandling/terrainIPBR.glsl\"\n" +
            "        #define DISTANT_LIGHT_BOKEH\n" +
            "    #else\n" +
            "        #include \"/lib/materials/materialHandling/terrainIPBR.glsl\"\n" +
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

val COMMON_FUNCTIONS = arrayListOf<String>()
const val COMMON_FUNCTIONS_PATH = "/shaders/lib/util/commonFunctions.glsl"

fun injectCommonFunctions(directory: Path) {
    val file = File(directory.absolutePathString() + COMMON_FUNCTIONS_PATH)
    file.appendText(COMMON_FUNCTIONS.joinToString("\n"))
}

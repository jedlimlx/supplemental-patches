package io.github.jedlimlx.supplemental_patches.shaders

import com.google.gson.*
import io.github.jedlimlx.supplemental_patches.LOGGER
import io.github.jedlimlx.supplemental_patches.shaders.ShaderResourceLoader.getFileContents
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.GsonHelper
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.math.ceil
import kotlin.math.log10

//? <=1.21.8 {
/*import net.minecraft.client.Minecraft
import net.minecraft.server.packs.resources.ReloadableResourceManager
import net.minecraft.util.profiling.ProfilerFiller
*///?}

val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

fun loadJson(location: Identifier, manager: ResourceManager): JsonObject {
    try {
        return GsonHelper.fromJson(GSON, getFileContents(location, manager), JsonObject::class.java)
    } catch (e: JsonParseException) {
        throw MinecraftError(e.message ?: "Error parsing JSON file", location.toString())
    }
}

fun <T> readConditionalValue(json: JsonElement, valueString: String, error: MinecraftError, load: (JsonElement) -> T): List<Pair<List<String>, T>> {
	return if (json.isJsonObject) {
		val field = json.asJsonObject
		listOf(
			Pair(
				field["conditions"]?.asJsonArray?.map { it.asString }?.toList() ?: listOf(),
				load(field[valueString] ?: throw error)
			)
		)
	} else if (json.isJsonArray) {
		if (json.asJsonArray.any { !it.isJsonObject })
			return listOf(Pair(listOf(), load(json)))

		json.asJsonArray.map {
			val field = it.asJsonObject
			Pair(
				field["conditions"]?.asJsonArray?.map { it.asString }?.toList() ?: listOf(),
				load(field[valueString] ?: throw error)
			)
		}
	} else listOf(Pair(listOf(), load(json)))
}

object ShaderResourceLoader {
    val COLOURS_MAP: HashMap<String, Colour> = hashMapOf()
    val TINTS_MAP: HashMap<String, Colour> = hashMapOf()
    val WAVING_MAP: HashMap<String, WavingObject> = hashMapOf()
    val DEFERRED_MAP: HashMap<String, DeferredMaterial> = hashMapOf()
	val LIGHT_GROUP_MAP: HashMap<String, LightGroup> = hashMapOf()
	val LIGHT_MODIFIER_MAP: HashMap<String, String> = hashMapOf()

    val BLOCK_MAP: HashMap<String, ShaderBuilder> = hashMapOf()
    val ITEM_MAP: HashMap<String, ShaderBuilder> = hashMapOf()
    val ENTITY_MAP: HashMap<String, ShaderBuilder> = hashMapOf()

    val REFLECTION_HANDLERS: HashMap<String, String> = hashMapOf()

	//? <=1.21.8 {
    /*fun registerListener() {
        val mc = Minecraft.getInstance()

        if (mc != null && mc.resourceManager is ReloadableResourceManager) {
            val resourceManager = (mc.resourceManager as ReloadableResourceManager)
            resourceManager.registerReloadListener(ShaderResourceLoader::reload)

            LOGGER.info("Registered listener for shader patches into Euphoria Patches.")
        }
    }
	*///?}

    fun reload(
		stage: PreparationBarrier,
		resourceManager: ResourceManager,
		//? <=1.21.4 {
		/*preparationsProfiler: ProfilerFiller,
		reloadProfiler: ProfilerFiller,
		*///?}
		backgroundExecutor: Executor,
		gameExecutor: Executor
    ): CompletableFuture<Void> = catchAndPrintError {
        // Clear all lists
        COLOURS.clear()
        TINTS.clear()
        COLOUR_INJECTIONS.clear()
        COLOURS_MAP.clear()
        TINTS_MAP.clear()

        WAVING_FUNCTIONS.clear()
        WAVING_MAP.clear()

        DEFERRED_MAP.clear()

        BLOCK_MAP.clear()
        BLOCK_ADDITIONAL_MAPPING.clear()
        BLOCK_REGEX_REPLACES.clear()

        ITEM_MAP.clear()
        ITEM_ADDITIONAL_MAPPING.clear()
        ITEM_REGEX_REPLACES.clear()

        ENTITY_MAP.clear()
        ENTITY_ADDITIONAL_MAPPING.clear()
        ENTITY_REGEX_REPLACES.clear()

        SpecificMaterial.MATERIALS.clear()

        MATERIALS.clear()
        MATERIALS_MAP.clear()

        ENTITIES.clear()
        ENTITY_MAP.clear()

        ITEMS.clear()
        ITEM_MAP.clear()

        TRANSLUCENTS.clear()
        TRANSLUCENTS_MAP.clear()

        BLOCK_ENTITIES.clear()
        BLOCK_ENTITIES_MAP.clear()

        PARTICLES.clear()

        FOGS.clear()
        ACT_FOGS.clear()
        FOG_FUNCTIONS.clear()

        MIXINS.clear()

        UNIFORMS.clear()

        SETTINGS.clear()

        SETTINGS_FILES.clear()

        TEXTURES.clear()

        SKIES.clear()

        LAYER_CHANGES.clear()

        REFLECTION_HANDLERS.clear()

        COMMON_FUNCTIONS.clear()

        BUFFERS.clear()

        REFACTORS.clear()

		LIGHT_GROUP_MAP.clear()

		DEFINES.clear()

		LIGHT_GROUPS.clear()

        // Loading various colours
        val lst = resourceManager.listResources("euphoria/colors") { it.path.endsWith(".json") }

        LOGGER.info("Loading ${lst.entries.size} colors...")
        lst.forEachWithErrorHandling { (loc, _) ->
            val json = loadJson(loc, resourceManager)
            val colour = if ("index" in json.keySet()) Colour(index = json["index"].asInt, code = json["code"]?.asString ?: "")
            else Colour(code = json["code"].asString)

            COLOURS.add(colour)
            COLOURS_MAP[loc.toString().replace("euphoria/colors/", "").replace(".json", "")] = colour
        }

        // Loading various tints
        val tintLst = resourceManager.listResources("euphoria/tints") { it.path.endsWith(".json") }

        LOGGER.info("Loading ${tintLst.entries.size} tints...")
        tintLst.forEachWithErrorHandling { (loc, _) ->
            val json = loadJson(loc, resourceManager)
            val colour = if ("index" in json.keySet()) Colour(index = json["index"].asInt, tint = true)
            else Colour(code = json["code"].asString, tint = true)

            TINTS.add(colour)
            TINTS_MAP[loc.toString().replace("euphoria/tints/", "").replace(".json", "")] = colour
        }

        // Loading waving objects
        val map = resourceManager.listResources("euphoria/waving/objects") { it.path.endsWith(".glsl") }.map { (loc, _) ->
            loc.path.split("/").last() to getFileContents(loc, resourceManager)
        }.toMap()
        val lst2 = resourceManager.listResources("euphoria/waving/objects") { it.path.endsWith(".json") }

        LOGGER.info("Loading ${lst2.entries.size} waving objects...")
        lst2.forEachWithErrorHandling { (loc, _) ->
            val json = loadJson(loc, resourceManager)
            val key = loc.toString().replace("euphoria/waving/objects/", "").replace(".json", "")
            WAVING_MAP[key] = WavingObject(
                code = map[
                    json["glsl"].asString ?: throw MinecraftError(".glsl file not specified.", loc.toString())
                ] ?: throw MinecraftError("${json["glsl"]} not found!", loc.toString()),
                conditions = json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
            )
        }

        // Loading deferred materials
        val map2 = resourceManager.listResources("euphoria/deferred") { it.path.endsWith(".glsl") }.map { (loc, _) ->
            loc.path.split("/").last() to getFileContents(loc, resourceManager)
        }.toMap()
        val lst3 = resourceManager.listResources("euphoria/deferred") { it.path.endsWith(".json") }

        LOGGER.info("Loading ${lst3.entries.size} deferred materials...")
        lst3.forEachWithErrorHandling { (loc, _) ->
            val json = loadJson(loc, resourceManager)
            val key = loc.toString().replace("euphoria/deferred/", "").replace(".json", "")
            DEFERRED_MAP[key] = DeferredMaterial(
                name = json["name"].asString,
                glsl = map2[
                    json["glsl"].asString ?: throw MinecraftError(".glsl file not specified.", loc.toString())
                ] ?: throw MinecraftError("${json["glsl"]} not found!", loc.toString())
            )
        }

        // Loading reflection handlers
        resourceManager.listResources("euphoria/reflection_handlers") { it.path.endsWith(".glsl") }.map { (loc, _) ->
            val key = loc.toString().replace("euphoria/reflection_handlers/", "")
                .replace(".glsl", "")
            REFLECTION_HANDLERS[key] = getFileContents(loc, resourceManager)
        }

		// Loading light modifiers
		resourceManager.listResources("euphoria/photonics/light_modifiers") { it.path.endsWith(".glsl") }.forEachWithErrorHandling { (loc, _) ->
			val key = loc.toString().replace("euphoria/photonics/light_modifiers/", "").replace(".glsl", "")
			LIGHT_MODIFIER_MAP[key] = getFileContents(loc, resourceManager)
		}

		// Loading light groups
		loadPhotonicsLightGroups(resourceManager, "euphoria/photonics")

        return@catchAndPrintError CompletableFuture.allOf(
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/terrain", BLOCK_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/translucents", BLOCK_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/block_entities", BLOCK_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/items", ITEM_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/entities", ENTITY_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/particles"),
            loadSpecificMaterials(backgroundExecutor, resourceManager, "euphoria/specific_materials"),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/waving/functions", WAVING_FUNCTIONS),
            loadSettings(backgroundExecutor, resourceManager, "euphoria/settings"),
            loadSettingsFiles(backgroundExecutor, resourceManager, "euphoria/settings_files"),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/colors/injects", COLOUR_INJECTIONS),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/atmospherics/fog/fogs", FOGS),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/atmospherics/fog/act_fogs", ACT_FOGS),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/atmospherics/fog/functions", FOG_FUNCTIONS),
            loadUniforms(backgroundExecutor, resourceManager, "euphoria/uniforms"),
            loadMixins(backgroundExecutor, resourceManager, "euphoria/mixins"),
            loadSkies(backgroundExecutor, resourceManager, "euphoria/atmospherics/sky"),
            loadTextures(backgroundExecutor, resourceManager, "euphoria/textures"),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/common", COMMON_FUNCTIONS),
            loadBuffers(backgroundExecutor, resourceManager, "euphoria/buffers"),
            loadRefactors(backgroundExecutor, resourceManager, "euphoria/refactors"),
			loadPackJson(backgroundExecutor, resourceManager, "euphoria"),
			loadPhotonicsDefines(backgroundExecutor, resourceManager, "euphoria/photonics/defines")
        ).thenAcceptAsync {
            fun process(json: JsonObject, string: String, map: HashMap<String, ShaderBuilder>, regexReplaces: MutableList<Regex>, additionaMapping: MutableMap<Int, List<String>>) {
                json.keySet().forEach {
                    when {
                        it == "remove" -> {
                            json["remove"].asJsonArray.forEach {
                                regexReplaces.add(Regex(it.asString))
                            }
                        }

                        it.matches(Regex("$string.\\d+")) -> {
                            val num = it.replace("$string.", "").toInt()
                            additionaMapping[num] = json[it].asJsonArray.map { it.asString }
                        }

                        it.matches(Regex("layer.(.*?)")) -> {
                            val layer = it.replace("layer.", "")
                            if (layer !in LAYER_CHANGES) LAYER_CHANGES[layer] = arrayListOf()
                            LAYER_CHANGES[layer]!!.addAll(json[it].asJsonArray.map { it.asString })
                        }

                        else -> {
                            // Regex("([a-zA-Z\\d_-]+:[a-zA-Z\\d_/-]+).mat\\d+")
                            val tokens = it.split(".")
                            val shader = map[tokens[0]] ?: return@forEach
                            shader.mat[tokens[1].replace("mat", "").toInt()].addAll(
                                json[it]?.asJsonArray?.map { it.asString } ?: listOf()
                            )
                        }
                    }
                }
            }

            resourceManager.listResources("euphoria") { it.path.endsWith(".properties.json") }.forEachWithErrorHandling { (loc, _) ->
                if (loc.path.startsWith("euphoria/block")) {
                    val blockProperties = GsonHelper.fromJson(
                        GSON,
                        getFileContents(loc, resourceManager),
                        JsonObject::class.java
                    )
                    process(blockProperties, "block", BLOCK_MAP, BLOCK_REGEX_REPLACES, BLOCK_ADDITIONAL_MAPPING)
                } else if (loc.path.startsWith("euphoria/entity")) {
                    val entityProperties = GsonHelper.fromJson(
                        GSON,
                        getFileContents(loc, resourceManager),
                        JsonObject::class.java
                    )
                    process(entityProperties, "entity", ENTITY_MAP, ENTITY_REGEX_REPLACES, ENTITY_ADDITIONAL_MAPPING)
                } else if (loc.path.startsWith("euphoria/item")) {
                    val itemProperties = GsonHelper.fromJson(
                        GSON,
                        getFileContents(loc, resourceManager),
                        JsonObject::class.java
                    )
                    process(itemProperties, "item", ITEM_MAP, ITEM_REGEX_REPLACES, ITEM_ADDITIONAL_MAPPING)
                }
            }
        }.thenCompose(stage::wait).thenAcceptAsync({}, gameExecutor)
    }

	fun getColourOrTint(id: String, loc: String) = COLOURS_MAP[id] ?: TINTS_MAP[id] ?: throw MinecraftError("Color / tint $id does not exist!", loc)

    fun loadMaterialShaders(
        executor: Executor, resourceManager: ResourceManager, type: String, map: MutableMap<String, ShaderBuilder>? = null
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                resourceManager.listResources(type) { it.path.endsWith(".glsl") }.map { (loc, _) ->
                    loc.path.replace("$type/", "") to getFileContents(loc, resourceManager)
                }.toMap()
            }, executor
        ).thenAcceptAsync(
            {
                val lst = resourceManager.listResources(type) { it.path.endsWith(".json") }

                LOGGER.info("Loading ${lst.entries.size} unique $type materials...")
                lst.forEachWithErrorHandling { (loc, _) ->
                    val tokens = loc.path.replace("$type/", "").split("/")
                    val path = tokens.subList(0, tokens.size - 1).joinToString("/")
                    val json = loadJson(loc, resourceManager)
                    val builder = ShaderBuilder(
                        name = json["name"].asString ?: throw MinecraftError("Name of material not specified", loc.toString()),
                        glsl = it[
                            "$path${if (path.isEmpty()) "" else "/"}" +
                                    (json["glsl"].asString ?: throw MinecraftError(".glsl file not specified.", loc.toString()))
                        ] ?: throw MinecraftError("$path/${json["glsl"].asString} not found!", loc.toString()),
                        blockSize = json["block_size"]?.asInt ?: 4
                    )

                    for (i in builder.mat.indices)
                        builder.mat[i] = json["mat$i"]?.asJsonArray?.map { it.asString }?.toMutableList() ?: mutableListOf()

                    if (json["blocklight"] != null) {
						builder.blocklight(
							readConditionalValue(
								json["blocklight"],
								"color",
								MinecraftError("Invalid specification of blocklight color!", loc.toString())
							) {
								if (it.isJsonArray) {
									it.asJsonArray.map {
										if (it != JsonNull.INSTANCE) {
											getColourOrTint(it.asString, loc.toString())
										} else null
									}.toList()
								} else listOf(getColourOrTint(it.asString, loc.toString()))
							}
						)
                    }
                    if (json["held_lighting"]?.asBoolean == true) builder.heldLighting()
                    if (json["translucent"]?.asBoolean == true) builder.translucent()
                    if (json["needs_voxelization"]?.asBoolean == true) builder.needsVoxelisation()

                    if (json["waving"] != null) {
                        builder.wavingObject(
                            WAVING_MAP[json["waving"].asString] ?:
                            throw MinecraftError("Waving object ${json["waving"]} does not exist!", loc.toString())
                        )
                    }

                    if (json["light_level"] != null)
                        builder.lightLevel(json["light_level"].asInt)

                    if (json["reflection_handlers"] != null) {
                        if (json["reflection_handlers"].isJsonArray) {
                            builder.reflectionHandlers(
                                json["reflection_handlers"].asJsonArray.map {
                                    if (it != JsonNull.INSTANCE) {
                                        REFLECTION_HANDLERS[it.asString] ?:
                                        throw MinecraftError("Reflection handler $it does not exist!", loc.toString())
                                    } else null
                                }
                            )
                        } else {
                            builder.reflectionHandler(
                                REFLECTION_HANDLERS[json["reflection_handlers"].asString] ?:
                                throw MinecraftError("Reflection handler ${json["reflection_handlers"]} does not exist!", loc.toString())
                            )
                        }
                    }

					if (json["light_modifiers"] != null) {
						if (json["light_modifiers"].isJsonArray) {
							builder.lightModifiers(
								json["light_modifiers"].asJsonArray.map {
									if (it != JsonNull.INSTANCE) {
										LIGHT_MODIFIER_MAP[it.asString] ?:
										throw MinecraftError("Light modifier $it does not exist!", loc.toString())
									} else null
								}
							)
						} else {
							builder.lightModifier(
								LIGHT_MODIFIER_MAP[json["light_modifiers"].asString] ?:
								throw MinecraftError("Light modifier ${json["light_modifiers"]} does not exist!", loc.toString())
							)
						}
					}

					if (json["light_groups"] != null) {
						builder.mat.indices.forEach { i ->
							val lightGroups = readConditionalValue(
								json["light_groups"],
								"group",
								MinecraftError("Did not specify light group", loc.toString())
							) {
								if (it.isJsonArray) {
									val lightGroup = it.asJsonArray[i]
									if (!lightGroup.isJsonNull) {
										LIGHT_GROUP_MAP[lightGroup.asString]
											?: throw MinecraftError(
												"Light group ${json["group"]} does not exist",
												loc.toString()
											)
									} else null
								} else {
									LIGHT_GROUP_MAP[it.asString]
										?: throw MinecraftError(
											"Light group ${json["group"]} does not exist",
											loc.toString()
										)
								}
							}

							val blocks = builder.mat[i].map {
								val tokens = it.split(":")
								if (tokens.size > 2) {
									val properties = tokens.subList(2, tokens.size)
									"${tokens[0]}:${tokens[1]}[${properties.joinToString(",")}]"
								} else it
							}.toList()
							lightGroups.forEach { (conditions, lightGroup) ->
								if (lightGroup != null) {
									if (conditions.isNotEmpty()) {
										lightGroup.blocks.add(Pair(conditions, blocks))
									} else {
										lightGroup.blocks.add(
											Pair(
												lightGroups.filter { it.first.isNotEmpty() }.map { "!(${it.first.conditions()})" },
												blocks
											)
										)
									}
								}
							}
						}
					}

                    builder.register(
                        when (type) {
                            "euphoria/translucents" -> TRANSLUCENTS
                            "euphoria/block_entities" -> BLOCK_ENTITIES
                            "euphoria/items" -> ITEMS
                            "euphoria/entities" -> ENTITIES
                            "euphoria/particles" -> PARTICLES
                            else -> MATERIALS
                        }
                    )

                    if (map != null)
                        map[loc.toString().replace("euphoria/", "").replace(".json", "")] = builder
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadSpecificMaterials(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                resourceManager.listResources(type) { it.path.endsWith(".glsl") }.map { (loc, _) ->
                    loc.path.split("/").last() to getFileContents(loc, resourceManager)
                }.toMap()
            }, executor
        ).thenAcceptAsync(
            {
                val lst = resourceManager.listResources(type) { it.path.endsWith(".json") }

                LOGGER.info("Loading ${lst.entries.size} specific materials...")
                lst.forEachWithErrorHandling { (loc, _) ->
                    val json = loadJson(loc, resourceManager)
                    SpecificMaterial(
                        path = json["path"].asString,
                        glsl = it[
                            json["glsl"].asString ?: throw MinecraftError(".glsl file not specified.", loc.toString())
                        ] ?: throw MinecraftError("${json["glsl"]} not found!", loc.toString())
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadSettings(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                fun recurse(namespace: String, path: String = ""): List<Settings> {
                    LOGGER.info("Loading settings from $type$path...")
                    return resourceManager.listResources("$type$path") {
						it.namespace == namespace &&
                        it.path.endsWith(".json") &&
						!it.path.endsWith("entrypoint.json") &&
						it.path.split("$type$path/").last().count { it == '/' } == 0
                    }.mapWithErrorHandling { (loc, _) ->
                        val json = loadJson(loc, resourceManager)
                        val settingType = SettingType.fromString(
                            json["type"].asString, loc.toString()
                        )

                        val languages = json.keySet().filter { it.matches(Regex("[a-z][a-z]_[A-Z][A-Z]")) }.associateWith {
                            val temp = json[it].asJsonObject
                            temp.keySet().associateWith { temp[it].asString }
                        }
                        when (settingType) {
                            SettingType.DIVIDER -> Settings(
                                SettingType.DIVIDER, "", json["priority"]?.asInt ?: 0,
                                mapOf(), listOf(), listOf(),
                                dividers = json["dividers"]?.asInt ?: 2
                            )
                            SettingType.INFORMATION -> Settings(
                                SettingType.INFORMATION,
                                json["name"].asString ?: throw MinecraftError("Setting name is not specified.", loc.toString()),
                                json["priority"]?.asInt ?: 0,
                                languages, listOf(), listOf()
                            )
                            SettingType.DIRECTORY -> {
                                val output = Settings(
                                    SettingType.DIRECTORY,
                                    json["name"].asString ?: throw MinecraftError("Setting name is not specified.", loc.toString()),
                                    json["priority"]?.asInt ?: 0,
                                    languages, listOf(), listOf()
                                )
                                output.children.addAll(recurse(namespace, path = "$path/${json["folder"].asString}"))

                                output
                            }
                            SettingType.SETTING -> {
                                val conditions = json["conditions"]?.asJsonArray
                                val conditionsLst = conditions?.map {
                                    val temp = it.asJsonObject
                                    Pair(temp["if"].asString, temp["then"].asString)
                                } ?: listOf(Pair("else", json["default"].asString))

                                val values = (json["values"]?.asJsonArray?.toList() ?: listOf<JsonObject>()).map {
                                    if (it.isJsonObject) {
                                        val output = it.asJsonObject
                                        if (output["type"].asString == "range") {
                                            val lst = mutableListOf<String>()

                                            var curr = output["start"].asString.toDouble()
                                            val step = output["step"].asString.toDouble()
                                            val stop = output["stop"].asString.toDouble()

                                            val dp = ceil(-log10(step)).toInt()
                                            while (curr < stop) {
                                                if (dp > 0) lst.add(String.format("%.${dp}f", curr))
                                                else lst.add(curr.toInt().toString())
                                                curr += step
                                            }

                                            lst
                                        } else throw MinecraftError("${output["type"]} is not a valid type.", loc.toString())
                                    } else mutableListOf(it.asString)
                                }.flatten()

                                Settings(
                                    SettingType.SETTING,
                                    json["name"].asString ?: throw MinecraftError("Setting name is not specified.", loc.toString()),
                                    json["priority"]?.asInt ?: 0,
                                    languages,
                                    conditionsLst,
                                    values,
                                    json["slider"]?.asBoolean ?: false,
                                    json["file"]?.asString ?: "common.glsl",
                                    json["activation"]?.asBoolean ?: false
                                )
                            }
							SettingType.LINK -> {
								Settings(
									SettingType.LINK,
									json["name"].asString ?: throw MinecraftError("Setting name is not specified.", loc.toString()),
									json["priority"]?.asInt ?: 0,
									mapOf(), listOf(), listOf()
								)
							}
							SettingType.DIRECTORY_LINK -> {
								Settings(
									SettingType.DIRECTORY_LINK,
									json["name"].asString ?: throw MinecraftError("Setting name is not specified.", loc.toString()),
									json["priority"]?.asInt ?: 0,
									mapOf(), listOf(), listOf()
								)
							}
                        }
                    }
                }

				var count = 0
				val entrypoints = arrayListOf<Settings>()
				resourceManager.listResources(type) {
					it.path.endsWith("entrypoint.json") && it.namespace != "minecraft"
				}.forEach { (loc, _) ->
					val json = loadJson(loc, resourceManager)
					val settingsJson = json["settings"]?.asJsonObject ?: throw MinecraftError(
						"Entrypoint for settings not specified!",
						loc.toString()
					)
					val languages = settingsJson.keySet().filter { it.matches(Regex("[a-z][a-z]_[A-Z][A-Z]")) }.associateWith {
						val temp = settingsJson[it].asJsonObject
						temp.keySet().associateWith { temp[it].asString }
					}

					val settings = Settings(
						SettingType.DIRECTORY,
						settingsJson["name"].asString ?: throw MinecraftError("Setting name is not specified.", loc.toString()),
						count++,
						languages, listOf(), listOf()
					)
					settings.children.addAll(recurse(loc.namespace))
					entrypoints.add(settings)

					entrypoints.add(
						Settings(
							SettingType.DIRECTORY_LINK,
							json["about"].asString ?: throw MinecraftError("Setting name is not specified.", loc.toString()),
							count++,
							mapOf(), listOf(), listOf()
						)
					)
				}

				SETTINGS.addAll(recurse("minecraft"))
				SETTINGS.find { it.name == "SHADER_PATCHES" }!!.children.addAll(entrypoints)
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadFiles(
        executor: Executor, resourceManager: ResourceManager, type: String, lst: ArrayList<String>
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                resourceManager.listResources(type) { it.path.endsWith(".glsl") }
            }, executor
        ).thenAcceptAsync(
            {
                it.forEach { (loc, _) ->
                    lst.add(getFileContents(loc, resourceManager))
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadUniforms(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                resourceManager.listResources(type) { it.path.endsWith(".json") }
            }, executor
        ).thenAcceptAsync(
            {
                LOGGER.info("Loading ${it.size} uniforms...")
                it.forEachWithErrorHandling { (loc, _) ->
                    val json = loadJson(loc, resourceManager)
                    UNIFORMS.add(
                        Uniform(
                            type = json["type"].asString,
                            name = json["name"].asString,
                            defaultValue = json["default"]?.asString,
                            code = json["code"]?.asString ?: "",
                            conditions = json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadMixins(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                resourceManager.listResources(type) { it.path.endsWith(".glsl") }.map { (loc, _) ->
                    loc.path.replace("$type/", "") to getFileContents(loc, resourceManager)
                }.toMap()
            }, executor
        ).thenAcceptAsync(
            {
                val lst = resourceManager.listResources(type) { it.path.endsWith(".json") }

                LOGGER.info("Loading ${lst.entries.size} shader mixins...")
                lst.forEachWithErrorHandling { (loc, _) ->
                    val tokens = loc.path.replace("$type/", "").split("/")
                    val path = tokens.subList(0, tokens.size - 1).joinToString("/")
                    val json = loadJson(loc, resourceManager)

                    MIXINS.add(
                        ShaderMixin(
                            path = json["file"].asString ?: throw MinecraftError("Path of file to modify is not specified", loc.toString()),
                            type = ShaderMixinType.fromString(json["type"].asString ?: throw MinecraftError("Injection type is not specified", loc.toString()), loc.toString()),
                            key = (if (json["regex"]?.asBoolean ?: false) "regex~" else "") + (json["key"].asString ?: throw MinecraftError("Key to identify modification location is not specified", loc.toString())),
                            code = it[
                                "$path${if (path.isEmpty()) "" else "/"}" +
                                    (json["code"].asString ?: throw MinecraftError(".glsl file not specified.", loc.toString()))
                            ] ?: throw MinecraftError("$path/${json["code"].asString} not found!", loc.toString()),
                            mixinFile = loc.toString()
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadSkies(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync(
            {
                resourceManager.listResources(type) { it.path.endsWith(".glsl") }.map { (loc, _) ->
                    loc.path.replace("$type/", "") to getFileContents(loc, resourceManager)
                }.toMap()
            }, executor
        ).thenAcceptAsync(
            {
                val lst = resourceManager.listResources(type) { it.path.endsWith(".json") }

                LOGGER.info("Loading ${lst.entries.size} skies...")
                lst.forEachWithErrorHandling { (loc, _) ->
                    val tokens = loc.path.replace("$type/", "").split("/")
                    val path = tokens.subList(0, tokens.size - 1).joinToString("/")
                    val json = loadJson(loc, resourceManager)

                    SKIES.add(
                        Sky(
                            json["name"].asString ?: throw MinecraftError("Name of main GLSL file is not specified", loc.toString()),
                            it[
                                "$path${if (path.isEmpty()) "" else "/"}" +
                                        (json["code"].asString ?: throw MinecraftError(".glsl file not specified.", loc.toString()))
                            ] ?: throw MinecraftError("$path/${json["code"].asString} not found!", loc.toString()),
                            json["dimension"].asString ?: throw MinecraftError("Dimension in which sky should be rendered is not specified.", loc.toString()),
							it[
								"$path${if (path.isEmpty()) "" else "/"}" +
									(json["deferred"].asString ?: throw MinecraftError("Code to be inserted into deferred1.glsl not specified.", loc.toString()))
							] ?: throw MinecraftError("$path/${json["code"].asString} not found!", loc.toString()),
							it[
								"$path${if (path.isEmpty()) "" else "/"}" +
									(json["reflection"].asString ?: throw MinecraftError("Code to be inserted into reflectionImpl.glsl is not specified.", loc.toString()))
							] ?: throw MinecraftError("$path/${json["code"].asString} not found!", loc.toString()),
                            json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadSettingsFiles(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                resourceManager.listResources(type) { it.path.endsWith(".json") }.forEachWithErrorHandling { (loc, _) ->
                    val json = loadJson(loc, resourceManager)
                    SETTINGS_FILES.add(
                        SettingsFile(
                            json["name"]?.asString ?: throw MinecraftError("Name of setting file not specified.", loc.toString()),
                            json["files"]?.asJsonArray?.map { it.asString } ?:
                            throw MinecraftError("No shader files for settings to be placed in specified.", loc.toString())
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadTextures(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                val lst = resourceManager.listResources(type) { it.path.endsWith(".json") }
                LOGGER.info("Loading ${lst.size} custom textures...")

                lst.forEachWithErrorHandling { (loc, _) ->
                    val json = loadJson(loc, resourceManager)
                    TEXTURES.add(
                        Texture(
                            json["texture"]?.asString ?: throw MinecraftError("Path to texture file not specified.", loc.toString()),
                            json["name"]?.asString ?: throw MinecraftError("Name of texture is not specified.", loc.toString()),
                            json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadBuffers(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                val lst = resourceManager.listResources(type) { it.path.endsWith(".json") }
                LOGGER.info("Loading ${lst.size} custom buffers...")

                lst.forEachWithErrorHandling { (loc, _) ->
                    val json = loadJson(loc, resourceManager)
                    BUFFERS.add(
                        Buffer(
                            json["name"]?.asString ?: throw MinecraftError("Buffer name is not specified.", loc.toString()),
                            json["image_format"]?.asString ?: throw MinecraftError("Image format is not specified", loc.toString()),
                            (json["conditions"]?.asJsonArray ?: listOf()).map { it.asString }.toList(),
                            (json["read"]?.asJsonArray ?: listOf()).map { it.asString }.toList(),
                            (json["write"]?.asJsonArray ?: listOf()).map { it.asString }.toList()
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadRefactors(
        executor: Executor, resourceManager: ResourceManager, type: String
    ): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync (
            {
                resourceManager.listResources(type) { it.path.endsWith(".json") }.forEachWithErrorHandling { (loc, _) ->
                    val json = loadJson(loc, resourceManager)
                    REFACTORS.add(
                        Refactor(
                            json["function"]?.asString ?: throw MinecraftError("Function header is not specified.", loc.toString()),
                            json["original_file"]?.asString ?: throw MinecraftError("File containing the original function is not specified.", loc.toString()),
                            (json["changes"]?.asJsonArray ?: listOf()).map {
                                val obj = it.asJsonObject
                                when (it.asJsonObject["type"].asString) {
                                    "new_param" -> {
                                        NewParameter(
                                            obj["name"]?.asString ?: throw MinecraftError("New parameter name / data type is not specified.", loc.toString()),
                                            obj["default"]?.asString ?: throw MinecraftError("Default value for new parameter is not specified.", loc.toString())
                                        )
                                    }
                                    else -> throw MinecraftError("Unknown type specified for refactoring changes.", loc.toString())
                                }
                            }.toList(),
                            (json["files"]?.asJsonArray ?: listOf()).map { it.asString }.toList()
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

	fun loadPackJson(
		executor: Executor, resourceManager: ResourceManager, type: String
	): CompletableFuture<Void> {
		return CompletableFuture.supplyAsync (
			{
				resourceManager.listResources(type) { it.path.endsWith("pack.json") }.forEach { (loc, _) ->
					PACK_JSON = getFileContents(loc, resourceManager)

					val json = loadJson(loc, resourceManager)
					SHADERPACK_NAME = json["name"]!!.asString
				}
			}, executor
		).thenAcceptAsync {}
	}

	fun loadPhotonicsLightGroups(
		resourceManager: ResourceManager, type: String
	) {
		fun recurse(path: String): MutableList<LightGroup> {
			LOGGER.info("Loading light groups from $type$path...")
			return resourceManager.listResources("$type$path") {
				it.path.endsWith(".json") &&
				it.path.split("$type$path/").last().count { it == '/' } == 0
			}.mapWithErrorHandling { (loc, _) ->
				val json = loadJson(loc, resourceManager)

				val properties = json.keySet().filter { it != "group" && it != "overrides" && it != "blocks" }.map {
					LightGroupProperty(
						it,
						readConditionalValue<String>(
							json[it],
							"value",
							MinecraftError(
								"Did not specify value for light group property $it",
								loc.toString()
							)
						) { it.asString }
					)
				}.toList()

				val groupName = json["group"]?.asString ?: throw MinecraftError("Did not specify light group name", loc.toString())
				val lightGroup = LightGroup(
					groupName,
					if (json["blocks"] != null) mutableListOf(Pair(listOf(), json["blocks"].asJsonArray.map { it.asString }.toList())) else mutableListOf(),
					properties,
					if (json["overrides"] != null) recurse("$path/${json["overrides"].asString}") else mutableListOf()
				)

				LIGHT_GROUP_MAP[groupName] = lightGroup
				lightGroup
			}.toMutableList()
		}

		LIGHT_GROUPS.addAll(recurse(""))
	}

	fun loadPhotonicsDefines(
		executor: Executor, resourceManager: ResourceManager, type: String
	): CompletableFuture<Void> {
		return CompletableFuture.supplyAsync (
			{
				resourceManager.listResources(type) { it.path.endsWith(".json") }.forEachWithErrorHandling { (loc, _) ->
					val json = loadJson(loc, resourceManager)

					DEFINES.colours.addAll(
						json["colors"]?.asJsonObject?.keySet()?.map {
							Pair(it, json["colors"].asJsonObject[it].asString)
						} ?: listOf()
					)
					DEFINES.intensities.addAll(
						json["intensities"]?.asJsonObject?.keySet()?.map {
							Pair(it, json["intensities"].asJsonObject[it].asDouble)
						} ?: listOf()
					)
					DEFINES.radii.addAll(
						json["radii"]?.asJsonObject?.keySet()?.map {
							Pair(it, json["radii"].asJsonObject[it].asDouble)
						} ?: listOf()
					)
					DEFINES.falloffs.addAll(
						json["falloffs"]?.asJsonObject?.keySet()?.map {
							Pair(it, json["falloffs"].asJsonObject[it].asDouble)
						} ?: listOf()
					)
				}
			}, executor
		).thenAcceptAsync {}
	}

    fun getFileContents(location: Identifier, manager: ResourceManager): String {
        return manager.getResourceOrThrow(location).open().use { it.bufferedReader().readText() }
    }
}

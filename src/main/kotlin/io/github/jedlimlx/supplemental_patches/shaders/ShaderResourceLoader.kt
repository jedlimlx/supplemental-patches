package io.github.jedlimlx.supplemental_patches.shaders

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import io.github.jedlimlx.supplemental_patches.SupplementalPatches.LOGGER
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.resources.PreparableReloadListener.PreparationBarrier
import net.minecraft.server.packs.resources.ReloadableResourceManager
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.util.GsonHelper
import net.minecraft.util.profiling.ProfilerFiller
import java.io.FileNotFoundException
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.collections.flatten
import kotlin.collections.forEach
import kotlin.math.ceil
import kotlin.math.log10


val GSON: Gson = GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create()

object ShaderResourceLoader {
    val COLOURS_MAP: HashMap<String, Colour> = hashMapOf()
    val TINTS_MAP: HashMap<String, Colour> = hashMapOf()
    val WAVING_MAP: HashMap<String, WavingObject> = hashMapOf()
    val DEFERRED_MAP: HashMap<String, DeferredMaterial> = hashMapOf()

    val BLOCK_MAP: HashMap<String, ShaderBuilder> = hashMapOf()
    val ITEM_MAP: HashMap<String, ShaderBuilder> = hashMapOf()
    val ENTITY_MAP: HashMap<String, ShaderBuilder> = hashMapOf()

    fun registerListener() {
        val mc = Minecraft.getInstance()

        if (mc != null && mc.resourceManager is ReloadableResourceManager) {
            val resourceManager = (mc.resourceManager as ReloadableResourceManager)
            resourceManager.registerReloadListener(ShaderResourceLoader::reload)

            LOGGER.info("Registered listener for shader patches into Euphoria Patches.")
        }
    }

    fun reload(
        stage: PreparationBarrier,
        resourceManager: ResourceManager,
        backgroundExecutor: Executor,
        gameExecutor: Executor
    ): CompletableFuture<Void> {
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
        FOG_FUNCTIONS.clear()

        MIXINS.clear()

        UNIFORMS.clear()

        ATMOSPHERICS.clear()

        SETTINGS.clear()

        SETTINGS_FILES.clear()

        TEXTURES.clear()

        SKIES.clear()

        // Loading various colours
        val lst = resourceManager.listResources("euphoria/colors") { it.path.endsWith(".json") }

        LOGGER.info("Loading ${lst.entries.size} colors...")
        lst.forEach { (loc, _) ->
            val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
            val colour = if ("index" in json.keySet()) Colour(index = json["index"].asInt, code = json["code"]?.asString ?: "")
            else Colour(code = json["code"].asString)

            COLOURS.add(colour)
            COLOURS_MAP[loc.toString().replace("euphoria/colors/", "").replace(".json", "")] = colour
        }

        // Loading various tints
        val tintLst = resourceManager.listResources("euphoria/tints") { it.path.endsWith(".json") }

        LOGGER.info("Loading ${tintLst.entries.size} tints...")
        tintLst.forEach { (loc, _) ->
            val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
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
        lst2.forEach { (loc, _) ->
            val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
            val key = loc.toString().replace("euphoria/waving/objects/", "").replace(".json", "")
            WAVING_MAP[key] = WavingObject(
                code = map[
                    json["glsl"].asString ?: throw IllegalArgumentException(".glsl file not specified.")
                ] ?: throw FileNotFoundException("${json["glsl"]} not found!"),
                conditions = json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
            )
        }

        // Loading deferred materials
        val map2 = resourceManager.listResources("euphoria/deferred") { it.path.endsWith(".glsl") }.map { (loc, _) ->
            loc.path.split("/").last() to getFileContents(loc, resourceManager)
        }.toMap()
        val lst3 = resourceManager.listResources("euphoria/deferred") { it.path.endsWith(".json") }

        LOGGER.info("Loading ${lst3.entries.size} deferred materials...")
        lst3.forEach { (loc, _) ->
            val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
            val key = loc.toString().replace("euphoria/deferred/", "").replace(".json", "")
            DEFERRED_MAP[key] = DeferredMaterial(
                name = json["name"].asString,
                glsl = map2[
                    json["glsl"].asString ?: throw IllegalArgumentException(".glsl file not specified.")
                ] ?: throw FileNotFoundException("${json["glsl"]} not found!")
            )
        }

        return CompletableFuture.allOf(
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/terrain", BLOCK_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/translucent", BLOCK_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/block_entity", BLOCK_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/items", ITEM_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/entity", ENTITY_MAP),
            loadMaterialShaders(backgroundExecutor, resourceManager, "euphoria/particles"),
            loadSpecificMaterials(backgroundExecutor, resourceManager, "euphoria/specific_materials"),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/waving/functions", WAVING_FUNCTIONS),
            loadSettings(backgroundExecutor, resourceManager, "euphoria/settings"),
            loadSettingsFiles(backgroundExecutor, resourceManager, "euphoria/settings_files"),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/colors/injects", COLOUR_INJECTIONS),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/atmospherics/fog/fogs", FOGS),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/atmospherics/fog/functions", FOG_FUNCTIONS),
            loadUniforms(backgroundExecutor, resourceManager, "euphoria/uniforms"),
            loadMixins(backgroundExecutor, resourceManager, "euphoria/mixins"),
            loadVolumetricAtmospherics(backgroundExecutor, resourceManager, "euphoria/atmospherics/volumetric"),
            loadSkies(backgroundExecutor, resourceManager, "euphoria/atmospherics/sky"),
            loadTextures(backgroundExecutor, resourceManager, "euphoria/textures")
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

            resourceManager.listResources("euphoria") { it.path.endsWith(".properties.json") }.forEach { (loc, _) ->
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
                lst.forEach { (loc, _) ->
                    val tokens = loc.path.replace("$type/", "").split("/")
                    val path = tokens.subList(0, tokens.size - 1).joinToString("/")
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
                    val builder = ShaderBuilder(
                        name = json["name"].asString ?: throw IllegalArgumentException("Name of material not specified"),
                        glsl = it[
                            "$path${if (path.isEmpty()) "" else "/"}" +
                                    (json["glsl"].asString ?: throw IllegalArgumentException(".glsl file not specified."))
                        ] ?: throw FileNotFoundException("$path/${json["glsl"].asString} not found!"),
                        blockSize = json["block_size"]?.asInt ?: 4
                    )

                    for (i in builder.mat.indices)
                        builder.mat[i] = json["mat$i"]?.asJsonArray?.map { it.asString }?.toMutableList() ?: mutableListOf()

                    if (json["color"] != null) {
                        if (json["color"].isJsonArray) {
                            builder.lightColour(
                                json["color"].asJsonArray.map {
                                    if (it != JsonNull.INSTANCE) {
                                        COLOURS_MAP[it.asString] ?:
                                        TINTS_MAP[it.asString] ?:
                                        throw IllegalArgumentException("Color / tint $it does not exist!")
                                    } else null
                                },
                                conditions = json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
                            )
                        } else {
                            builder.lightColour(
                                COLOURS_MAP[json["color"].asString] ?:
                                TINTS_MAP[json["color"].asString] ?:
                                throw IllegalArgumentException("Color / tint ${json["color"]} does not exist!"),
                                conditions = json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
                            )
                        }
                    }
                    if (json["held_lighting"]?.asBoolean == true) builder.heldLighting()
                    if (json["translucent"]?.asBoolean == true) builder.translucent()
                    if (json["needs_voxelization"]?.asBoolean == true) builder.needsVoxelisation()

                    if (json["waving"] != null) {
                        builder.wavingObject(
                            WAVING_MAP[json["waving"].asString] ?:
                            throw IllegalArgumentException("Waving object ${json["waving"]} does not exist!")
                        )
                    }

                    if (json["light_level"] != null)
                        builder.lightLevel(json["light_level"].asInt)

                    builder.register(
                        when (type) {
                            "euphoria/translucent" -> TRANSLUCENTS
                            "euphoria/block_entity" -> BLOCK_ENTITIES
                            "euphoria/items" -> ITEMS
                            "euphoria/entity" -> ENTITIES
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
                lst.forEach { (loc, _) ->
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
                    SpecificMaterial(
                        path = json["path"].asString,
                        glsl = it[
                            json["glsl"].asString ?: throw IllegalArgumentException(".glsl file not specified.")
                        ] ?: throw FileNotFoundException("${json["glsl"]} not found!")
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
                fun recurse(path: String = ""): List<Settings> {
                    LOGGER.info("Loading settings from $type$path...")
                    return resourceManager.listResources("$type$path") {
                        it.path.endsWith(".json") &&
                                it.path.split("$type$path/").last().count { it == '/' } == 0
                    }.map { (loc, _) ->
                        val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
                        val settingType = SettingType.fromString(json["type"].asString)

                        val languages = json.keySet().filter { it.matches(Regex("[a-z][a-z]_[A-Z][A-Z]")) }.associate {
                            val temp = json[it].asJsonObject
                            it to temp.keySet().associateWith { temp[it].asString }
                        }
                        when (settingType) {
                            SettingType.DIVIDER -> Settings(
                                SettingType.DIVIDER, "", json["priority"]?.asInt ?: 0,
                                mapOf(), listOf(), listOf(),
                                dividers = json["dividers"]?.asInt ?: 2
                            )
                            SettingType.INFORMATION -> Settings(
                                SettingType.INFORMATION, json["name"].asString, json["priority"]?.asInt ?: 0,
                                languages, listOf(), listOf()
                            )
                            SettingType.DIRECTORY -> {
                                val output = Settings(
                                    SettingType.DIRECTORY, json["name"].asString, json["priority"]?.asInt ?: 0,
                                    languages, listOf(), listOf()
                                )
                                output.children.addAll(recurse("$path/${json["folder"].asString}"))

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
                                        } else throw IllegalArgumentException("type is not known")
                                    } else mutableListOf(it.asString)
                                }.flatten()

                                Settings(
                                    SettingType.SETTING,
                                    json["name"].asString,
                                    json["priority"]?.asInt ?: 0,
                                    languages,
                                    conditionsLst,
                                    values,
                                    json["slider"]?.asBoolean ?: false,
                                    json["file"]?.asString ?: "common.glsl",
                                    json["activation"]?.asBoolean ?: false
                                )
                            }
                        }
                    }
                }

                SETTINGS.addAll(recurse())
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
                it.forEach { (loc, _) ->
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
                    UNIFORMS.add(
                        Uniform(
                            type = json["type"].asString,
                            name = json["name"].asString,
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
                lst.forEach { (loc, _) ->
                    val tokens = loc.path.replace("$type/", "").split("/")
                    val path = tokens.subList(0, tokens.size - 1).joinToString("/")
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)

                    MIXINS.add(
                        ShaderMixin(
                            path = json["file"].asString ?: throw IllegalArgumentException("Path of file to modify is not specified"),
                            type = ShaderMixinType.fromString(json["type"].asString ?: throw IllegalArgumentException("Injection type is not specified")),
                            key = json["key"].asString ?: throw IllegalArgumentException("Key to identify modification location is not specified"),
                            code = it[
                                "$path${if (path.isEmpty()) "" else "/"}" +
                                    (json["code"].asString ?: throw IllegalArgumentException(".glsl file not specified."))
                            ] ?: throw FileNotFoundException("$path/${json["code"].asString} not found!")
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun loadVolumetricAtmospherics(
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

                LOGGER.info("Loading ${lst.entries.size} volumetric atmospherics...")
                lst.forEach { (loc, _) ->
                    val tokens = loc.path.replace("$type/", "").split("/")
                    val path = tokens.subList(0, tokens.size - 1).joinToString("/")
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)

                    ATMOSPHERICS.add(
                        Atmospherics(
                            libPath = json["lib"].asJsonObject["name"].asString ?: throw IllegalArgumentException("Path to place library file is not specified"),
                            libCode = it[
                                "$path${if (path.isEmpty()) "" else "/"}" +
                                        (json["lib"].asJsonObject["glsl"].asString ?: throw IllegalArgumentException(".glsl file not specified."))
                            ] ?: throw FileNotFoundException("$path/${json["lib"].asJsonObject["glsl"].asString} not found!"),
                            mainCode = it[
                                "$path${if (path.isEmpty()) "" else "/"}" +
                                        (json["main"].asString ?: throw IllegalArgumentException(".glsl file not specified."))
                            ] ?: throw FileNotFoundException("$path/${json["main"].asString} not found!"),
                            conditions = json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
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
                lst.forEach { (loc, _) ->
                    val tokens = loc.path.replace("$type/", "").split("/")
                    val path = tokens.subList(0, tokens.size - 1).joinToString("/")
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)

                    SKIES.add(
                        Sky(
                            json["name"].asString ?: throw IllegalArgumentException("Name of main GLSL file is not specified"),
                            it[
                                "$path${if (path.isEmpty()) "" else "/"}" +
                                        (json["code"].asString ?: throw IllegalArgumentException(".glsl file not specified."))
                            ] ?: throw FileNotFoundException("$path/${json["code"].asString} not found!"),
                            json["dimension"].asString ?: throw IllegalArgumentException("Dimension in which sky should be rendered is not specified."),
                            json["deferred"].asString ?: throw IllegalArgumentException("Code to be inserted into deferred1.glsl not specified."),
                            json["reflection"].asString ?: throw IllegalArgumentException("Code to be inserted into reflectionImpl.glsl is not specified."),
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
                resourceManager.listResources(type) { it.path.endsWith(".json") }.forEach { (loc, _) ->
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
                    SETTINGS_FILES.add(
                        SettingsFile(
                            json["name"]?.asString ?: throw IllegalArgumentException("Name not specified."),
                            json["files"]?.asJsonArray?.map { it.asString } ?:
                            throw IllegalArgumentException("No shader files for settings to be placed in specified.")
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
                resourceManager.listResources(type) { it.path.endsWith(".json") }.forEach { (loc, _) ->
                    val json = GsonHelper.fromJson(GSON, getFileContents(loc, resourceManager), JsonObject::class.java)
                    TEXTURES.add(
                        Texture(
                            json["texture"]?.asString ?: throw IllegalArgumentException("Path to texture file not specified."),
                            json["name"]?.asString ?: throw IllegalArgumentException("Name of texture is not specified."),
                            json["conditions"]?.asJsonArray?.map { it.asString } ?: listOf()
                        )
                    )
                }
            }, executor
        ).thenAcceptAsync {}
    }

    fun getFileContents(location: ResourceLocation, manager: ResourceManager): String {
        return manager.getResourceOrThrow(location).open().use { it.bufferedReader().readText() }
    }
}
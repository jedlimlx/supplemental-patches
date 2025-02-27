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
        preparationsProfiler: ProfilerFiller,
        reloadProfiler: ProfilerFiller,
        backgroundExecutor: Executor,
        gameExecutor: Executor
    ): CompletableFuture<Void> {
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

        // Loading various tint
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
            loadFiles(backgroundExecutor, resourceManager, "euphoria/colors/injects", COLOUR_INJECTIONS),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/atmospherics/fog/fogs", FOGS),
            loadFiles(backgroundExecutor, resourceManager, "euphoria/atmospherics/fog/functions", FOG_FUNCTIONS),
            loadUniforms(backgroundExecutor, resourceManager, "euphoria/uniforms"),
            loadMixins(backgroundExecutor, resourceManager, "euphoria/mixins")
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
                            val shader = map[it] ?: return@forEach
                            shader.mat0.addAll(json["mat0"]?.asJsonArray?.map { it.asString } ?: listOf())
                            shader.mat1.addAll(json["mat1"]?.asJsonArray?.map { it.asString } ?: listOf())
                            shader.mat2.addAll(json["mat2"]?.asJsonArray?.map { it.asString } ?: listOf())
                            shader.mat3.addAll(json["mat3"]?.asJsonArray?.map { it.asString } ?: listOf())
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
                        mat0 = json["mat0"]?.asJsonArray?.map { it.asString } ?: listOf(),
                        mat1 = json["mat1"]?.asJsonArray?.map { it.asString } ?: listOf(),
                        mat2 = json["mat2"]?.asJsonArray?.map { it.asString } ?: listOf(),
                        mat3 = json["mat3"]?.asJsonArray?.map { it.asString } ?: listOf()
                    )

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

                        val languages = json.keySet().filter { it.matches(Regex("[a-z][a-z]_[A-Z][A-Z]")) }.map {
                            val temp = json[it].asJsonObject
                            it to temp.keySet().associateWith { temp[it].asString }
                        }.toMap()
                        when (settingType) {
                            SettingType.DIVIDER -> Settings(
                                SettingType.DIVIDER, "", json["priority"]?.asInt ?: 0,
                                mapOf(), listOf(), listOf()
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
                                Settings(
                                    SettingType.SETTING,
                                    json["name"].asString,
                                    json["priority"]?.asInt ?: 0,
                                    languages,
                                    conditionsLst,
                                    json["values"]?.asJsonArray?.map { it.asString } ?: listOf(),
                                    json["slider"]?.asBoolean ?: false
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

    fun getFileContents(location: ResourceLocation, manager: ResourceManager): String {
        return manager.getResourceOrThrow(location).open().use { it.bufferedReader().readText() }
    }
}
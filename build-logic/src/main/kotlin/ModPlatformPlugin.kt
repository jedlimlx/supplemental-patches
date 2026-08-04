@file:Suppress("unused", "DuplicatedCode")

import de.undercouch.gradle.tasks.download.Download
import dev.kikugie.fletching_table.extension.FletchingTableExtension
import dev.kikugie.stonecutter.build.StonecutterBuildExtension
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import me.modmuss50.mpp.ModPublishExtension
import me.modmuss50.mpp.ReleaseType
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Delete
import org.gradle.internal.extensions.stdlib.toDefaultLowerCase
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.plugins.ide.idea.model.IdeaModel
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Path
import java.util.*
import javax.inject.Inject
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

fun Project.getLatestVersionModrinth(
	id: String,
	minecraftVersion: String,
	modLoader: String
): String {
	val cacheFileName = "${id}_${minecraftVersion}_${modLoader}.txt"
	val cacheFile = layout.buildDirectory.dir("modrinth-cache").get().asFile.toPath().resolve("modrinth_cache/$cacheFileName")
	if (cacheFile.exists()) return cacheFile.readText().trim()

	val client = HttpClient.newBuilder().build()
	val request = HttpRequest.newBuilder()
		.uri(URI.create("https://api.modrinth.com/v2/project/$id/version?loaders=[%22$modLoader%22]&game_versions=[%22$minecraftVersion%22]"))
		.build()

	val response = client.send(request, HttpResponse.BodyHandlers.ofString())
	if (response.statusCode() != 200) {
		throw RuntimeException("Failed to fetch Modrinth version for $id: HTTP ${response.statusCode()}")
	}

	val jsonArray = Json.decodeFromString<JsonArray>(response.body())
	if (jsonArray.isEmpty()) {
		throw NoSuchElementException("No version found for project $id with loader $modLoader on MC $minecraftVersion")
	}

	val versionId = jsonArray[0].jsonObject["id"].toString().drop(1).dropLast(1)

	cacheFile.parent.createDirectories()
	cacheFile.writeText(versionId)
	return versionId
}

fun Project.prop(name: String): String = (findProperty(name) ?: "") as String

fun Project.env(variable: String): String? = providers.environmentVariable(variable).orNull

fun Project.envTrue(variable: String): Boolean = env(variable)?.toDefaultLowerCase() == "true"

fun Project.inferredLoader() = project.buildFile.name.substringAfter('.').replace(".gradle.kts", "")

fun DependencyHandlerScope.modrinthImplementation(project: Project, modName: String) {
	val propName = project.prop("deps.${modName}")
	if (propName != "") {
		this.dependencies.add(
			if (project.inferredLoader() == "neoforge") "implementation" else "modImplementation",
			"maven.modrinth:${modName}:${propName}"
		)
	}
}

fun DependencyHandlerScope.modrinthCompileOnly(project: Project, modName: String) {
	val propName = project.prop("deps.${modName}")
	if (propName != "") {
		this.dependencies.add(
			if (project.inferredLoader() == "neoforge") "compileOnly" else "modCompileOnly",
			"maven.modrinth:${modName}:${propName}"
		)
	}
}

fun RepositoryHandler.strictMaven(
	url: String, vararg groups: String, configure: MavenArtifactRepository.() -> Unit = {}
) = exclusiveContent {
	forRepository { maven(url) { configure() } }
	filter { groups.forEach(::includeGroup) }
}

abstract class ModPlatformPlugin @Inject constructor() : Plugin<Project> {
	var project: Project? = null
	var euphoriaDev: Boolean = false
	var photonicsJar: Boolean = false

	override fun apply(project: Project) = with(project) {
		val project = this

		val secretPropertiesFile = project.rootProject.file("secrets.properties")
		if (secretPropertiesFile.exists()) {
			val properties = Properties()
			properties.load(secretPropertiesFile.inputStream())
			properties.forEach { (key, value) ->
				project.extensions.extraProperties.set(key.toString(), value)
			}
		}

		val inferredLoader = project.inferredLoader()
		val extension = extensions.create("platform", ModPlatformExtension::class.java).apply {
			loader.convention(inferredLoader)
			jarTask.convention("jar")
			sourcesJarTask.convention("sourcesJar")
		}

		listOf(
			"org.jetbrains.kotlin.jvm",
			"com.google.devtools.ksp",
			"dev.kikugie.fletching-table"
		).forEach { apply(plugin = it) }

		afterEvaluate {
			configureProject(extension)
		}
	}

	private fun Project.configureProject(extension: ModPlatformExtension) {
		val loader = extension.loader.get()
		val isFabric = loader == "fabric"
		val isNeoForge = loader == "neoforge"
		val isForge = loader == "forge"

		val modId = prop("mod.id")
		val modVersion = prop("mod.version")
		val channelTag = prop("mod.channel_tag")
		val mcVersion = prop("deps.minecraft")

		val stonecutter = extensions.getByType<StonecutterBuildExtension>()

		listOf(
			"java",
			"me.modmuss50.mod-publish-plugin",
			"idea",
		).forEach { apply(plugin = it) }

		version = "$modVersion$channelTag+$mcVersion-$loader"

		extension.requiredJava.set(
			when {
				stonecutter.eval(stonecutter.current.version, ">=26.1") -> JavaVersion.VERSION_25
				stonecutter.eval(stonecutter.current.version, ">=1.20.6") -> JavaVersion.VERSION_21
				stonecutter.eval(stonecutter.current.version, ">=1.18") -> JavaVersion.VERSION_17
				stonecutter.eval(stonecutter.current.version, ">=1.17") -> JavaVersion.VERSION_16
				else -> JavaVersion.VERSION_1_8
			}
		)

		if (isFabric) {
			extension.dependencies { required("java") { versionRange = ">=${extension.requiredJava.get().majorVersion}" } }
		}

		configureFletchingTable()
		configureJarTask(modId, loader)
		configureIdea()
		configureShaderpackDownloads()
		configurePhotonicsJarDownloads()
		configureCopyEuphoriaPatchesSubmodule()
		configureProcessResources(isFabric, isNeoForge, isForge, modId, "$modVersion$channelTag", mcVersion, extension, extension.requiredJava.get())
		configureJava(stonecutter, extension.requiredJava.get())
		configureKotlin(stonecutter, extension.requiredJava.get())
		registerBuildAndCollectTask(extension, "$modVersion$channelTag")
		configurePublishing(extension, loader, stonecutter, "$modVersion$channelTag", channelTag, version.toString())
	}

	private fun Project.configureJarTask(modId: String, loader: String) {
		val isForge = loader == "forge"

		tasks.withType<Jar>().configureEach {
			archiveBaseName.set(modId)
			if (isForge) {
				manifest.attributes(
					"MixinConfigs" to "${modId}.mixins.json"
				)
			}
		}
	}

	private fun Project.configureShaderpackDownloads() {
		val shaderDirectoryPath = "run/shaderpacks"
		val shaderDirectory = File(shaderDirectoryPath)
		shaderDirectory.mkdir()

		val complementaryShadersLink = prop("deps.complementary")
		if (complementaryShadersLink.isNotEmpty()) {
			tasks.register<Download>("downloadComplementaryShaders") {
				src(complementaryShadersLink)
				overwrite(false)
				dest("${shaderDirectoryPath}/${complementaryShadersLink.split("/").last()}")
			}
		} else {
			logger.warn("No link to Complementary development version specified!")
		}

		val euphoriaDevLink = prop("deps.euphoria-dev")
		if (euphoriaDevLink.isNotEmpty()) {
			val euphoriaDevName = euphoriaDevLink.split("/").last().split("?").first().split(".zip").first()
			val target = File("${shaderDirectoryPath}/${euphoriaDevName}")
			if (!target.exists()) {
				euphoriaDev = true
				tasks.register<Download>("downloadEuphoriaDev") {
					src(prop("deps.euphoria-dev"))
					overwrite(true)
					dest("${shaderDirectoryPath}/${euphoriaDevName}.zip")

					finalizedBy("unzipEuphoriaDev")
				}

				tasks.register<Copy>("unzipEuphoriaDev") {
					from(zipTree("${shaderDirectoryPath}/${euphoriaDevName}.zip"))
					into("${shaderDirectoryPath}/${euphoriaDevName}")

					finalizedBy("deleteEuphoriaDevZip")
				}

				tasks.register<Delete>("deleteEuphoriaDevZip") {
					delete("${shaderDirectoryPath}/${euphoriaDevName}.zip")
				}
			}
		} else {
			logger.warn("No link to Euphoria Patches development version specified!")
		}
	}

	private fun Project.configurePhotonicsJarDownloads() {
		val libsPath = "run/libs"
		val libsDirectory = File(libsPath)
		libsDirectory.mkdir()

		val photonicsLink = prop("deps.photonics-link")
		if (photonicsLink.isNotEmpty()) {
			val jarName = photonicsLink.split(".jar").first().split("?").last()
			val target = File(jarName)
			if (!target.exists()) {
				photonicsJar = true
				tasks.register<Download>("downloadPhotonicsJar") {
					src(photonicsLink)
					overwrite(true)
					dest("${libsDirectory}/${jarName}.jar")
				}
			}
		} else {
			logger.warn("No link to Photonics alpha version specified!")
		}
	}

	private fun Project.configureCopyEuphoriaPatchesSubmodule() {
		val shaderDirectoryPath = "run/shaderpacks"
		val sourceDir = rootProject.file("euphoria-patches")

		if (sourceDir.exists() && sourceDir.isDirectory) {
			tasks.register<Copy>("copyEuphoriaPatches") {
				group = "setup"
				description = "Copies the euphoria-patches submodule into the run shaderpacks directory."
				from(sourceDir)
				into("${shaderDirectoryPath}/EuphoriaPatches_GitHub")
				eachFile {
					duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.INCLUDE
				}
			}
		} else {
			logger.warn("Root directory 'euphoria-patches' does not exist or is not a directory.")
		}
	}

	private fun Project.configureProcessResources(
		isFabric: Boolean,
		isNeoForge: Boolean,
		isForge: Boolean,
		modId: String,
		modVersion: String,
		mcVersion: String,
		extension: ModPlatformExtension,
		requiredJava: JavaVersion
	) {
		tasks.named<ProcessResources>("processResources") {
			dependsOn(tasks.named("stonecutterGenerate"))
			dependsOn(tasks.named("downloadComplementaryShaders"))
			if (euphoriaDev) dependsOn(tasks.named("downloadEuphoriaDev"))
			if (photonicsJar) dependsOn(tasks.named("downloadPhotonicsJar"))
			if (rootProject.file("euphoria-patches").exists()) {
				dependsOn(tasks.named("copyEuphoriaPatches"))
			}

			dependsOn("kspKotlin")

			filesMatching("*.mixins.json") { expand("java" to "JAVA_${requiredJava.majorVersion}") }

			var contributors = prop("mod.contributors")
			var authors = prop("mod.authors")
			var issuesUrl = prop("mod.issues_url")
			if (issuesUrl == "") issuesUrl = prop("mod.sources_url") + "/issues"

			if (isFabric) {
				contributors = contributors.replace(", ", "\", \"")
				authors = authors.replace(", ", "\", \"")
			}

			val dependencies = buildDependenciesBlock(isFabric, modId, extension.dependencies)

			val props = mapOf(
				"version" to modVersion,
				"minecraft" to mcVersion,
				"id" to modId,
				"name" to prop("mod.name"),
				"group" to prop("mod.group"),
				"authors" to authors,
				"contributors" to contributors,
				"license" to prop("mod.license"),
				"description" to prop("mod.description"),
				"issues_url" to issuesUrl,
				"homepage_url" to prop("mod.homepage_url"),
				"sources_url" to prop("mod.sources_url"),
				"discord_url" to prop("mod.discord_url"),
				"dependencies" to dependencies,
				"euphoria_version" to prop("deps.euphoria_version"),
			)

			filesMatching("**/euphoria/pack.json") { expand(props) }
			filesMatching("**/euphoria/settings/shader_version.json") { expand(props) }
			filesMatching("**/euphoria/settings/mod_version.json") { expand(props) }
			when {
				isFabric -> {
					filesMatching("fabric.mod.json") { expand(props) }
					exclude("META-INF/mods.toml", "META-INF/neoforge.mods.toml", "aw/*.cfg", ".cache", "pack.mcmeta")
				}

				isNeoForge -> {
					filesMatching("META-INF/neoforge.mods.toml") { expand(props) }
					exclude("META-INF/mods.toml", "fabric.mod.json", "aw/*.accesswidener", ".cache", "pack.mcmeta")
				}

				isForge -> {
					filesMatching("META-INF/mods.toml") { expand(props) }
					exclude("META-INF/neoforge.mods.toml", "fabric.mod.json", "aw/*.accesswidener", ".cache")
				}
			}
		}
	}

	private fun buildDependenciesBlock(
		isFabric: Boolean, modId: String, deps: DependenciesConfig
	): String = if (isFabric) {
		buildString {
			fun joinGroup(
				name: String, container: NamedDomainObjectContainer<Dependency>
			): String? {
				if (container.isEmpty()) return null
				val entries = container.joinToString(",\n    ") {
					"\"${it.modid.get()}\": \"${it.versionRange.get()}\""
				}
				return "\n  \"$name\": {\n    $entries\n  }"
			}

			val groups = listOfNotNull(
				joinGroup("depends", deps.required),
				joinGroup("recommends", deps.optional),
				joinGroup("breaks", deps.incompatible)
			)

			append(groups.joinToString(","))
		}
	} else {
		buildString {
			fun appendBlock(container: NamedDomainObjectContainer<Dependency>, type: String) {
				container.forEach {
					appendLine(
						"""

						[[dependencies.$modId]]
						modId = "${it.modid.get()}"
						side = "${it.environment.get().uppercase(Locale.getDefault())}"
                        versionRange = "${it.forgeVersionRange.get()}"
						mandatory = ${if (type == "required") "true" else "false"}
                        type = "$type"
						""".replace("                  ", "").trimIndent()
					)
				}
			}

			appendBlock(deps.required, "required")
			appendBlock(deps.optional, "optional")
			appendBlock(deps.incompatible, "incompatible")
		}
	}

	private fun Project.configureJava(stonecutter: StonecutterBuildExtension, requiredJava: JavaVersion) {
		extensions.configure<JavaPluginExtension>("java") {
			withSourcesJar()
			withJavadocJar()
			sourceCompatibility = requiredJava
			targetCompatibility = requiredJava
		}
	}

	private fun Project.configureKotlin(stonecutter: StonecutterBuildExtension, requiredJava: JavaVersion) {
		tasks.withType(KotlinJvmCompile::class).configureEach {
			compilerOptions.jvmTarget.set(JvmTarget.fromTarget(requiredJava.toString()))
		}
	}

	private fun Project.configureIdea() {
		extensions.configure<IdeaModel>("idea") {
			module {
				isDownloadJavadoc = true
				isDownloadSources = true
			}
		}
	}

	private fun Project.configureFletchingTable() {
		extensions.configure<FletchingTableExtension> {
			mixins.create("main").apply {
				mixin("default", "${prop("mod.id")}.mixins.json") {
					env("CLIENT")
				}

				automatic = true
			}

			j52j.register("main") {
				extension("json", "**/*.json5")
			}
		}
	}

	private fun Project.registerBuildAndCollectTask(extension: ModPlatformExtension, modVersion: String) {
		tasks.register<Copy>("buildAndCollect") {
			group = "build"
			from(
				tasks.named(extension.jarTask.get())
			)
			into(rootProject.layout.buildDirectory.file("libs/$modVersion"))
			dependsOn("build")
		}
	}

	private fun Project.configurePublishing(
		ext: ModPlatformExtension,
		loader: String,
		stonecutter: StonecutterBuildExtension,
		modVersion: String,
		channelTag: String,
		fullVersion: String,
	) {
		val additionalVersions = (findProperty("publish.additionalVersions") as String?)?.split(',')?.map(String::trim)
			?.filter(String::isNotEmpty).orEmpty()

		val releaseType = ReleaseType.of(
			channelTag.substringAfter('-').substringBefore('.').ifEmpty { "stable" })

		extensions.configure<ModPublishExtension>("publishMods") {
			val mrStaging = envTrue("TEST_PUBLISHING_WITH_MR_STAGING")

			val modrinthAccessToken = env("MODRINTH_API_TOKEN")
			val curseforgeAccessToken = env("CURSEFORGE_API_TOKEN")
			if (!envTrue("ENABLE_PUBLISHING")) {
				dryRun = true
			}

			val isForge = loader == "forge"
			val targetName = if(isForge) {
				"reobfJar"
			} else {
				ext.jarTask.get()
			}

			val jarTask = tasks.named(targetName).map { it as Jar }
			val srcJarTask = tasks.named(ext.sourcesJarTask.get()).map { it as Jar }
			val currentVersion = stonecutter.current.version
			val deps = ext.dependencies

			file.set(jarTask.flatMap(Jar::getArchiveFile))
			additionalFiles.from(srcJarTask.flatMap(Jar::getArchiveFile))
			type = releaseType
			version = fullVersion
			//changelog.set(rootProject.file("CHANGELOG.md").readText())
			modLoaders.add(loader)

			displayName = "${prop("mod.name")} $modVersion ${loader.replaceFirstChar(Char::titlecase)} $currentVersion"

			modrinth(deps, currentVersion, additionalVersions, mrStaging, modrinthAccessToken)
			if (!mrStaging) curseforge(deps, currentVersion, additionalVersions, false, curseforgeAccessToken)
		}
	}

	fun whenNotNull(stringProp: Property<String>, action: (String) -> Unit) {
		if (!stringProp.orNull.isNullOrBlank()) action(stringProp.get())
	}

	private fun ModPublishExtension.modrinth(
		deps: DependenciesConfig,
		currentVersion: String,
		additionalVersions: List<String>,
		staging: Boolean,
		acesssToken: String?
	) = modrinth {
		if (staging) apiEndpoint = "https://staging-api.modrinth.com/v2"
		projectId = project.prop("publish.modrinth")
		accessToken = acesssToken
		minecraftVersions.addAll(listOf(currentVersion) + additionalVersions)

		if (!staging) {
			deps.required.forEach { dep -> whenNotNull(dep.modrinth) { requires(it) } }
			deps.optional.forEach { dep -> whenNotNull(dep.modrinth) { optional(it) } }
			deps.incompatible.forEach { dep -> whenNotNull(dep.modrinth) { incompatible(it) } }
			deps.embeds.forEach { dep -> whenNotNull(dep.modrinth) { embeds(it) } }
		}
	}

	private fun ModPublishExtension.curseforge(
		deps: DependenciesConfig,
		currentVersion: String,
		additionalVersions: List<String>,
		staging: Boolean,
		acesssToken: String?
	) = curseforge {
		projectId = project.prop("publish.curseforge")
		accessToken = acesssToken
		minecraftVersions.addAll(listOf(currentVersion) + additionalVersions)

		deps.required.forEach { dep -> whenNotNull(dep.curseforge) { requires(it) } }
		deps.optional.forEach { dep -> whenNotNull(dep.curseforge) { optional(it) } }
		deps.incompatible.forEach { dep -> whenNotNull(dep.curseforge) { incompatible(it) } }
		deps.embeds.forEach { dep -> whenNotNull(dep.curseforge) { embeds(it) } }
	}
}

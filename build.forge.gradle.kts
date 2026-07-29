import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.toString

val MODS = listOf(
	// general library mods
	"architectury-api",
	"blueprint",
	"cloth-config",
	"corgilib*",
	"data-anchor",
	"geckolib",
	"glitchcore",
	"terrablender",
	"trimmed",
	"moonlight",
	"oh-the-trees-youll-grow*",
	"puzzles-lib",
	"resourceful-config",
	"resourceful-lib",
	"runiclib",

	// extra optimisation
	"ferrite-core#",
	"immediately-fast#",

	// abnormals mods
	"abnormals-delight",
	"atmospheric",
	"autumnity",
	"berry-good",
	"buzzier-bees",
	"caverns-and-chasms",
	"clayworks",
	"endergetic",
	"environmental",
	"neapolitan",
	"savage-and-ravage",
	"upgrade-aquatic",
	"woodworks",

	// supplementaries
	"supplementaries",
	"amendments",
	"supplementaries-squared",
	"snowy-spirit!",

	// galena
	"oreganized",
	"doom-gloom",
	"windswept",

	// farmers delight
	"farmers-delight!",
	"rustic-delight!",
	"my-nethers-delight!",
	"ends-delight!",
	"dungeons-delight!",

	// biome mods
	"biomes-o-plenty*",
	"oh-the-biomes-weve-gone*",

	// orcinus
	"galosphere",

	// peculiar room
	"spawn-mod*",
	"whaleborne",
	"twigs",
	"the-between",
	"dye-depot!",
	"dye-the-world!",

	// jne
	"elysium-api!",
	"jadens-nether-expansion",
	"soulfulnether",
	"rubinated-nether",

	// yungs
	"yungs-api!",
	"yungs-cave-biomes*",

	// mob mods
	"cryptic-foes!",
	"inhabitants!",
	"mowzies-mobs!",
	"hominid!",

	// misc
	"cobblemon*",
	"enhanced-celestials*",
	"friends-and-foes!",
	"illager-invasion!"
)

plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
	id("dev.vfyjxf.gradle.production")
}

platform {
	loader = "forge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[1.20,1.21)"
		}
		required("forge") {
			forgeVersionRange = "[46,)"
		}
		required("kotlinforforge") {
			forgeVersionRange = "[4.0,)"
		}
		required("oculus") {
			forgeVersionRange = "[1.7,)"
		}
	}
}

legacyForge {
	version = "${property("deps.minecraft")}-${property("deps.forge")}"

	validateAccessTransformers = true

	accessTransformers.from(
		rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg")
	)

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "Forge Client (${stonecutter.active?.version})"
			programArgument("--username=Dev")
		}
	}


	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

production {
	val minecraftVersion = prop("deps.minecraft")

	idea {
		enabled = false
	}

	runs.configureEach {
		javaVersion = 17
		userName = "Dev"
	}

	runs.named("client") {
		type = "client"
		instanceDir = file("run")
		jvmArgs("-Xmx6G")

		mods {
			includeProject = true
			includeRequiredDependencies = false
			fun addMods(mods: List<String>) {
				mods.forEach {
					try {
						val tokens = it.split(":")
						val id = tokens[0].replace(Regex("[#!*]"), "")
						if (tokens[0].first() != '#') {
							if (tokens.size == 1) {
								val version = project.getLatestVersionModrinth(id, minecraftVersion, "forge")
								modrinthVersion(version)
							} else modrinth(id) { version = prop(tokens[1]) }
						}
					} catch (e: Exception) {
						logger.warn(e.toString())
					}
				}
			}

			// important dependencies
			addMods(listOf("jei", "jade"))

			modrinth("kotlin-for-forge") { version = prop("deps.kotlin-for-forge") }
			modrinth("better-mod-list") { version = prop("deps.better-mod-list") }

			modrinth("xenon-forge") { version = prop("deps.sodium") }
			modrinth("oculus") { version = prop("deps.iris") }
			modrinth("euphoria-patches") { version = "${prop("deps.euphoria-patches")}-forge" }
			addMods(listOf("radium", "iris-shader-folder", "irissearch"))

			addMods(MODS)
		}
	}
}

mixin {
	add(sourceSets.main.get(), "${prop("mod.id")}.mixins.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }

	maven("https://maven.bawnorton.com/releases")
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "Kotlin for Forge" }
	maven("https://maven.isxander.dev/releases") { name = "Xander Maven" }
}

dependencies {
	fun addMods(mods: List<String>) {
		mods.forEach {
			try {
				val tokens = it.split(":")
				val id = tokens[0].replace(Regex("[#!*]"), "")
				val modString = if (tokens.size == 1) {
					val mod = fletchingTable.modrinth(id, prop("deps.minecraft"), "forge")
					"${mod.group}:${id}:${mod.version}"
				} else "maven.modrinth:$id:${tokens[1]}"

				when (tokens[0].last()) {
					'*' -> modCompileOnly(modString)
					'!' -> modRuntimeOnly(modString)
					'#' -> {}
					else -> modImplementation(modString)
				}
			} catch (e: Exception) {
				logger.warn(e.toString())
			}
		}
	}

	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")

	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-common:${libs.versions.mixinsquared.get()}")
	compileOnly("com.github.bawnorton.mixinsquared:mixinsquared-common:${libs.versions.mixinsquared.get()}")

	implementation(libs.mixinsquared.forge)
	jarJar(libs.mixinsquared.forge)

	implementation("thedarkcolour:kotlinforforge:${prop("deps.kotlinforforge")}")

	modImplementation("maven.modrinth:better-modlist:${prop("deps.modmenu")}")
	modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")

	// jei / jade
	addMods(listOf("jei!", "jade!"))

	// rendering / optimisation mods
	addMods(listOf("radium!", "iris-shader-folder!"))
	modImplementation("maven.modrinth:xenon-forge:${prop("deps.sodium")}")
	modImplementation("maven.modrinth:oculus:${prop("deps.iris")}")
	runtimeOnly("maven.modrinth:euphoria-patches:${prop("deps.euphoria-patches")}-forge")

	addMods(MODS)
}

sourceSets {
	main {
		resources.srcDir(
			"${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated"
		)
	}
}

tasks.named<ProcessResources>("processResources") {
	from(rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg")) {
		rename { "accesstransformer.cfg" }
		into("META-INF")
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

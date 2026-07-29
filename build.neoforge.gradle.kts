import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlinx.serialization.json.*

val MODS = listOf(
	// general library mods
	"architectury-api",
	"blueprint",
	"cloth-config",
	"corgilib",
	"data-anchor",
	"geckolib",
	"glitchcore",
	"terrablender",
	"trimmed",
	"moonlight",
	"oh-the-trees-youll-grow",
	"puzzles-lib",
	"resourceful-config",
	"resourceful-lib",
	"runiclib*",
	"teallib",
	"yacl",
	"zeta#",

	// extra optimisation
	"ferrite-core#",
	"immediately-fast#",

	// abnormals mods
	"abnormals-delight*",
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
	"supplementaries-squared*",
	"snowy-spirit!",

	// galena
	"oreganized",
	"doom-gloom",
	"windswept*",

	// farmers delight
	"farmers-delight!",
	"rustic-delight!",
	"my-nethers-delight!",
	"ends-delight!",
	"dungeons-delight!",

	// biome mods
	"#biomes-o-plenty*",
	"#oh-the-biomes-weve-gone*",
	"no-mans-land",

	// orcinus
	"galosphere",

	// peculiar room
	"spawn-mod",
	"whaleborne",
	"twigs*",
	"the-between*",
	"the-beyond*",
	"dye-depot!",
	"dye-the-world!",

	// jne
	"elysium-api!",
	"jadens-nether-expansion",
	"soulfulnether*",
	"rubinated-nether",

	// yungs
	"yungs-api!",
	"yungs-cave-biomes",

	// mob mods
	"cryptic-foes*",
	"inhabitants*",
	"mowzies-mobs!",
	"hominid!",

	// misc
	"quark#",
	"quark-oddities#",
	"#cobblemon*",
	"enhanced-celestials",
	"friends-and-foes!",
	"illager-invasion!",
	"curiosities-syndicate",
	"#wetland-whimsy"
)

plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
	id("dev.vfyjxf.gradle.production")
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("\"particles\" in it.location()", "\"particles\" in it.location()")
		replace("resourceIdentifier", "resourceIdentifier")
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeVersionRange = "[1.21,1.21.3)"
		}
		required("neoforge") {
			forgeVersionRange = "[1,)"
		}
		required("kotlinforforge") {
			forgeVersionRange = "[5.3,)"
		}
		required("iris") {
			forgeVersionRange = "[1.8,)"
		}
	}
}

production {
	val minecraftVersion = prop("deps.minecraft")

	idea {
		enabled = false
	}

	runs.configureEach {
		javaVersion = 21
		userName = "Dev"
	}

	runs.named("client") {
		type = "client"
		instanceDir = file("run")
		jvmArgs("-Xmx6G")

		mods {
			includeProject = true
			includeRequiredDependencies = true
			fun addMods(mods: List<String>) {
				mods.forEach {
					try {
						val tokens = it.split(":")
						val id = tokens[0].replace(Regex("[#!*]"), "")
						if (tokens[0].first() != '#') {
							if (tokens.size == 1) {
								val version = project.getLatestVersionModrinth(id, minecraftVersion, "fabric")
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

			modrinth("kotlin-for-forge") { version = prop("deps.kotlinforforge") }
			modrinth("better-modlist") { version = prop("deps.modmenu") }

			modrinth("sodium") { version = prop("deps.sodium") }
			modrinth("iris") { version = prop("deps.iris") }
			modrinth("euphoria-patches") { version = "${prop("deps.euphoria-patches")}-neoforge" }
			addMods(listOf("lithium", "iris-shader-folder", "irissearch"))

			addMods(MODS)

			if (prop("deps.enderscape_2_1_0") == "true")
				add(files("libs/enderscape-neoforge-2.1.0+mc1.21.1.jar"))
			else addMods(listOf("enderscape"))
		}
	}
}


neoForge {
	version = property("deps.neoforge") as String
	accessTransformers.from(rootProject.file("src/main/resources/aw/${stonecutter.current.version}.cfg"))
	validateAccessTransformers = true

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = (property("deps.parchment") as String).split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${stonecutter.active?.version})"
			programArgument("--username=Dev")
		}
	}

	mods {
		register(property("mod.id") as String) {
			sourceSet(sourceSets["main"])
		}
	}
	sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated")
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }

	maven("https://maven.bawnorton.com/releases")
	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "Kotlin for Forge" }
}

dependencies {
	fun addMods(mods: List<String>) {
		mods.forEach {
			try {
				val tokens = it.split(":")
				val id = tokens[0].replace(Regex("[#!*]"), "")
				val modString = if (tokens.size == 1) {
					val mod = fletchingTable.modrinth(id, prop("deps.minecraft"), "neoforge")
					"${mod.group}:${id}:${mod.version}"
				} else "maven.modrinth:$id:${tokens[1]}"

				when (tokens[0].last()) {
					'*' -> compileOnly(modString)
					'!' -> runtimeOnly(modString)
					'#' -> {}
					else -> implementation(modString)
				}
			} catch (e: Exception) {
				logger.warn(e.toString())
			}
		}
	}

	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-common:${libs.versions.mixinsquared.get()}")
	compileOnly("com.github.bawnorton.mixinsquared:mixinsquared-common:${libs.versions.mixinsquared.get()}")

	implementation(libs.mixinsquared.neoforge)
	jarJar(libs.mixinsquared.neoforge)

	implementation("thedarkcolour:kotlinforforge-neoforge:${prop("deps.kotlinforforge")}")

	implementation("maven.modrinth:better-modlist:${prop("deps.modmenu")}")
	implementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")

	// jei / jade
	addMods(listOf("jei!", "jade!"))

	// rendering / optimisation mods
	addMods(listOf("lithium!", "iris-shader-folder!"))
	implementation("maven.modrinth:sodium:${prop("deps.sodium")}")
	implementation("maven.modrinth:iris:${prop("deps.iris")}")
	runtimeOnly("maven.modrinth:euphoria-patches:${prop("deps.euphoria-patches")}-neoforge")

	addMods(MODS)

	if (prop("deps.enderscape_2_1_0") == "true")
		implementation(files("libs/enderscape-neoforge-2.1.0+mc1.21.1.jar"))
	else addMods(listOf("enderscape"))
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

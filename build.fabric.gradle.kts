plugins {
	id("mod-platform")
	id("fabric-loom")
}

platform {
	loader = "fabric"
	dependencies {
		required("minecraft") {
			versionRange = "~${prop("deps.minecraft")}"
		}
		required("fabric-api") {
			slug("fabric-api")
			versionRange = ">=${prop("deps.fabric-api")}"
		}
		required("fabricloader") {
			versionRange = ">=${libs.fabric.loader.get().version}"
		}
		required("iris") {
			versionRange = ">=1.7"
		}
		required("fabric-language-kotlin") {
			versionRange = ">=${prop("deps.fabric-lang-kotlin")}"
		}
		optional("modmenu") {}
	}
}

loom {
	accessWidenerPath = rootProject.file("src/main/resources/aw/${stonecutter.current.version}.accesswidener")
	runs.named("client") {
		client()
		ideConfigGenerated(true)
		runDir = "run/"
		environment = "client"
		programArgs("--username=Dev")
		configName = "Fabric Client"
	}
}

configurations.all {
	if (prop("deps.minecraft") == "1.20.1") {
		resolutionStrategy {
			force("org.lwjgl:lwjgl:3.3.1")
		}
	}
}

repositories {
	mavenCentral()
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }

	maven("https://maven.bawnorton.com/releases")

	maven("https://maven.ladysnake.org/releases") { name = "Ladysnake Mods" }
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") { name = "Fuzs Mod Resources" }
	maven("https://maven.isxander.dev/releases") { name = "Xander Maven" }
	maven("https://maven.terraformersmc.com/") { name = "TerraformersMC" }
	maven("https://maven.jamieswhiteshirt.com/libs-release") {
		content { includeGroup("com.jamieswhiteshirt") }
	}
	maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
		name = "GeckoLib"
		content { includeGroup("com.eliotlash.mclib") }
	}
}

dependencies {
	fun addMods(mods: List<String>) {
		mods.forEach {
			try {
				val id = it.replace("*", "").replace("!", "")
				val mod = fletchingTable.modrinth(id, prop("deps.minecraft"), "fabric")

				val modString = "${mod.group}:${id}:${mod.version}"
				when (it.last()) {
					'*' -> modCompileOnly(modString)
					'!' -> modRuntimeOnly(modString)
					else -> modImplementation(modString)
				}
			} catch (e: Exception) {
				logger.warn(e.toString())
			}
		}
	}

	val minecraftVersion = prop("deps.minecraft")
	minecraft("com.mojang:minecraft:${minecraftVersion}")
	mappings(
		loom.layered {
			officialMojangMappings()
			if (hasProperty("deps.parchment")) parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
		}
	)
	modImplementation(libs.fabric.loader)

	implementation(libs.moulberry.mixinconstraints)
	include(libs.moulberry.mixinconstraints)

	implementation(libs.mixinsquared.fabric)
	include(libs.mixinsquared.fabric)

	modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")
	modImplementation("net.fabricmc:fabric-language-kotlin:${prop("deps.fabric-lang-kotlin")}")

	modLocalRuntime("com.terraformersmc:modmenu:${prop("deps.modmenu")}")
	modImplementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")

	// libraries
	localRuntime("org.anarres:jcpp:1.4.14")
	localRuntime("io.github.douira:glsl-transformer:${prop("deps.glsl-transformer")}")

	implementation("com.eliotlash.mclib:mclib:20")
	implementation("com.electronwill.night-config:toml:3.8.1")
	implementation("org.reflections:reflections:0.10.2")
	implementation("net.jodah:typetools:0.6.3")

	if (minecraftVersion == "1.20.1") {
		modApi("fuzs.puzzlesaccessapi:puzzlesaccessapi-fabric:20.1.1")
		modImplementation("com.jamieswhiteshirt:reach-entity-attributes:2.4.0")
		modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-base:${prop("deps.cardinal")}")
		modImplementation("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:${prop("deps.cardinal")}")
	} else {
		if (minecraftVersion in listOf("1.21.1", "1.21.10", "1.21.11")) addMods(listOf("lithostitched"))

		modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-base:${prop("deps.cardinal")}")
		modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-entity:${prop("deps.cardinal")}")
	}

	modImplementation("com.github.Chocohead:Fabric-ASM:2.3")
	if (prop("deps.extensibleenums").isNotEmpty())
		modApi("fuzs.extensibleenums:extensibleenums-fabric:${prop("deps.extensibleenums")}")
	if (prop("deps.neoforgedatapackextensions").isNotEmpty())
		modApi("fuzs.neoforgedatapackextensions:neoforgedatapackextensions-fabric:${prop("deps.neoforgedatapackextensions")}")
	if (prop("deps.wood-api").isNotEmpty())
		modImplementation("com.terraformersmc.terraform-api:terraform-wood-api-v1:${prop("deps.wood-api")}")
	if (prop("deps.mixson").isNotEmpty())
		modImplementation("maven.modrinth:mixson:${prop("deps.mixson")}")
	if (prop("deps.satin").isNotEmpty())
		modImplementation("org.ladysnake:satin:${prop("deps.satin")}")

	// jei / jade
	addMods(listOf("jade!"))
	if (prop("deps.minecraft") != "1.20.1")
		addMods(listOf("jei!"))

	// rendering / optimisation mods
	addMods(listOf("iris", "sodium!", "lithium!", "iris-shader-folder!"))
	runtimeOnly("maven.modrinth:euphoria-patches:${prop("deps.euphoria-patches")}-fabric")

	// general library mods
	addMods(
		listOf(
			"architectury-api",
			"cloth-config",
			"corgilib",
			"data-anchor",
			"forge-config-api-port",
			"geckolib",
			"glitchcore",
			"mixson",
			"moonlight",
			"oh-the-trees-youll-grow",
			"puzzles-lib",
			"resourceful-config",
			"resourceful-lib",
			"runiclib*",
			"terrablender",
			"trimmed"
		)
	)

	// supplementaries
	addMods(
		listOf(
			"supplementaries",
			"amendments",
			"supplementaries-squared",
			"snowy-spirit!"
		)
	)

	// farmers delight
	addMods(
		listOf(
			//"farmers-delight-refabricated!",
			//"rustic-delight!",
			//"my-nethers-delight-refabricated!",
			//"ends-delight!"
		)
	)

	// biome mods
	addMods(
		listOf(
			"biomes-o-plenty*",
			"oh-the-biomes-weve-gone*"
		)
	)

	// orcinus
	addMods(
		listOf(
			"galosphere"
		)
	)

	// peculiar room
	addMods(
		listOf(
			"twigs*",
			"dye-depot!"
		)
	)

	// yungs
	addMods(
		listOf(
			"yungs-api!",
			"yungs-cave-biomes"
		)
	)

	// frozen block
	addMods(
		listOf(
			"frozenlib",
			"wilder-wild*",
			"trailier-tales",
			"the-copperier-age!"
		)
	)

	// thermoo
	addMods(
		listOf(
			"thermoo",
			"immersive-storms",
			"scorchful",
			"frostiful"
		)
	)

	// misc fabric-exclusive
	addMods(
		listOf(
			"cinderscapes*",
			"nears!",
			"gipples-galore!",
			"pearfection!",
			"beeten!"
		)
	)

	// misc
	addMods(
		listOf(
			"enderscape",
			"cobblemon*",
			"enhanced-celestials",
			"friends-and-foes!",
			"illager-invasion!"
		)
	)
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("resourceIdentifier", "resourceIdentifier")
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

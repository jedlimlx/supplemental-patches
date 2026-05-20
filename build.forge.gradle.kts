plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
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
				val id = tokens[0].replace("*", "").replace("!", "")
				val modString = if (tokens.size == 1) {
					val mod = fletchingTable.modrinth(id, prop("deps.minecraft"), "forge")
					"${mod.group}:${id}:${mod.version}"
				} else "maven.modrinth:$id:${tokens[1]}"

				when (tokens[0].last()) {
					'*' -> modCompileOnly(modString)
					'!' -> modRuntimeOnly(modString)
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
	addMods(listOf("oculus", "xenon-forge!", "radium!", "iris-shader-folder!"))
	runtimeOnly("maven.modrinth:euphoria-patches:${prop("deps.euphoria-patches")}-forge")

	// general library mods
	addMods(
		listOf(
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
			"runiclib"
		)
	)

	// abnormals mods
	addMods(
		listOf(
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
			"woodworks"
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

	// galena
	addMods(
		listOf(
			"oreganized",
			"doom-gloom",
			"windswept"
		)
	)

	// farmers delight
	addMods(
		listOf(
			"farmers-delight!",
			"rustic-delight!",
			"my-nethers-delight!",
			"ends-delight!",
			"dungeons-delight!"
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
			"spawn-mod*",
			"whaleborne",
			"twigs",
			"the-between",
			"dye-depot!",
			"dye-the-world!"
		)
	)

	// jne
	addMods(
		listOf(
			"elysium-api!",
			"jadens-nether-expansion",
			"soulfulnether",
			"rubinated-nether"
		)
	)

	// yungs
	addMods(
		listOf(
			"yungs-api!",
			"yungs-cave-biomes*"
		)
	)

	// mob mods
	addMods(
		listOf(
			"cryptic-foes!",
			"inhabitants!",
			"mowzies-mobs!",
			"hominid!"
		)
	)

	// misc
	addMods(
		listOf(
			"cobblemon*",
			"enhanced-celestials*",
			"friends-and-foes!",
			"illager-invasion!"
		)
	)
}

sourceSets {
	main {
		resources.srcDir(
			"${rootDir}/versions/datagen/${stonecutter.current.version.split("-")[0]}/src/main/generated"
		)
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

stonecutter {

}

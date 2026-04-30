plugins {
	id("mod-platform")
	id("net.neoforged.moddev")
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

	maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "Kotlin for Forge" }
}

dependencies {
	fun addMods(mods: List<String>) {
		mods.forEach {
			try {
				val id = it.replace("*", "").replace("!", "")
				val mod = fletchingTable.modrinth(id, prop("deps.minecraft"), "neoforge")

				val modString = "${mod.group}:${id}:${mod.version}"
				when (it.last()) {
					'*' -> compileOnly(modString)
					'!' -> runtimeOnly(modString)
					else -> implementation(modString)
				}
			} catch (e: Exception) {
				logger.warn(e.toString())
			}
		}
	}

	implementation(libs.moulberry.mixinconstraints)
	jarJar(libs.moulberry.mixinconstraints)

	implementation("thedarkcolour:kotlinforforge-neoforge:${prop("deps.kotlinforforge")}")

	implementation("maven.modrinth:better-modlist:${prop("deps.modmenu")}")
	implementation("dev.isxander:yet-another-config-lib:${prop("deps.yacl")}")

	// jei / jade
	addMods(listOf("jei!", "jade!"))

	// rendering / optimisation mods
	addMods(listOf("iris", "sodium!", "lithium!", "iris-shader-folder!"))
	runtimeOnly("maven.modrinth:euphoria-patches:${prop("deps.euphoria-patches")}-neoforge")

	// general library mods
	addMods(
		listOf(
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
			"yacl"
		)
	)

	// abnormals mods
	addMods(
		listOf(
			"abnormals-delight*",
			"atmospheric",
			"autumnity",
			"berry-good",
			"buzzier-bees",
			"caverns-and-chasms*",
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
			"doom-gloom*",
			"windswept*"
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
			"spawn-mod",
			"whaleborne",
			"twigs*",
			"the-between*",
			"the-beyond*",
			"dye-depot!",
			"dye-the-world!"
		)
	)

	// jne
	addMods(
		listOf(
			"elysium-api!",
			"jadens-nether-expansion",
			"soulfulnether*",
			"rubinated-nether"
		)
	)

	// yungs
	addMods(
		listOf(
			"yungs-api!",
			"yungs-cave-biomes"
		)
	)

	// mob mods
	addMods(
		listOf(
			"cryptic-foes*",
			"inhabitants*",
			"mowzies-mobs!",
			"hominid!"
		)
	)

	// misc
	addMods(
		listOf(
			"enderscape",
			"cobblemon*",
			"enhanced-celestials",
			"friends-and-foes!",
			"illager-invasion!",
			"curiosities-syndicate",
			"wetland-whimsy"
		)
	)
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("resourceIdentifier", "resourceIdentifier")
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

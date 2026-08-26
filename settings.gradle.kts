pluginManagement {
	repositories {
		mavenLocal()
		mavenCentral()
		gradlePluginPortal()
		maven("https://maven.fabricmc.net/") { name = "Fabric" }
		maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
		maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
		maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
		maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
		maven("https://maven.terraformersmc.com/") { name = "TerraformersMC" }
		maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "Kotlin for Forge" }
		maven("https://maven.bawnorton.com/releases")
		maven("https://maven.covers1624.net/")

		exclusiveContent {
			forRepository { maven("https://api.modrinth.com/maven") { name = "Modrinth" } }
			filter { includeGroup("maven.modrinth") }
		}
	}
	includeBuild("build-logic")
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
	id("dev.kikugie.stonecutter") version "0.9.7"
	id("dev.kikugie.loom-back-compat") version "0.4.1"
}

stonecutter {
	create(rootProject) {
		fun match(version: String, vararg loaders: String) =
			loaders.forEach { version("$version-$it", version).buildscript = "build.$it.gradle.kts" }

		match("1.20.1", "fabric", "forge")
		match("1.21.1", "fabric", "neoforge")
		match("1.21.8", "fabric")
		match("1.21.10", "fabric")
		match("1.21.11", "fabric")
		match("26.1.2", "fabric")
		match("26.2", "fabric")

		vcsVersion = "1.21.1-neoforge"
	}
}

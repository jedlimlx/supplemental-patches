import org.spongepowered.asm.gradle.plugins.MixinExtension
import org.spongepowered.asm.gradle.plugins.struct.DynamicProperties
import java.text.SimpleDateFormat
import java.util.*

buildscript {
    repositories {
        mavenCentral()
        maven("https://maven.fabricmc.net/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0-Beta")
        classpath("org.spongepowered:mixingradle:0.7.+")
    }
}

apply(plugin = "kotlin")
apply(plugin = "org.spongepowered.mixin")
plugins {
    eclipse
    idea
    `maven-publish`
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

group = "io.github.jedlimlx.supplemental_patches"
version = "0.1.0-beta"

val modid = "supplemental_patches"
val vendor = "jedlimlx"

val minecraftVersion = "1.20.1"
val forgeVersion = "47.3.0"
val jeiVersion = "15.19.5.99"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

println(
    "Java: ${System.getProperty("java.version")} JVM: ${System.getProperty("java.vm.version")}(${
        System.getProperty(
            "java.vendor"
        )
    }) Arch: ${System.getProperty("os.arch")}"
)

minecraft {
    mappings("official", minecraftVersion)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs.all {
        mods {
            workingDirectory(project.file("run"))
            // property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")
            property("forge.enabledGameTestNamespaces", modid)
            property("terminal.jline", "true")
            mods {
                create(modid) {
                    source(sourceSets.main.get())
                }
            }
        }
    }

    runs.run {
        create("client") {
            property("log4j.configurationFile", "log4j2.xml")
            args("--username", "Player")
            jvmArg("-Xms8G")
        }

        create("server") {}
        create("gameTestServer") {}
        create("data") {
            workingDirectory(project.file("run"))
            args(
                "--mod",
                modid,
                "--all",
                "--output",
                file("src/generated/resources/"),
                "--existing",
                file("src/main/resources")
            )
        }
    }
}

sourceSets.main.configure { resources.srcDirs("src/generated/resources/") }

repositories {
    mavenCentral()
    maven {
        name = "Kotlin for Forge"
        url = uri("https://thedarkcolour.github.io/KotlinForForge/")
    }
    maven {
        name = "Progwml6's maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }
    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev")
    }
    maven {
        name = "CurseForge"
        url = uri("https://cursemaven.com")
    }
    maven {
        url = uri("https://maven.jaackson.me")
    }
    maven { url = uri("https://jitpack.io") }
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        forRepositories(fg.repository) // Only add this if you're using ForgeGradle, otherwise remove this line
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

fun getProperty(name: String): String {
    return project.findProperty(name)?.toString() ?: System.getProperty(name)
}

dependencies {
    minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")
    annotationProcessor("org.spongepowered:mixin:0.8.7:processor")
    implementation("thedarkcolour:kotlinforforge:4.10.0")

    compileOnly("io.github.llamalad7:mixinextras-common:0.4.1")
    implementation("io.github.llamalad7:mixinextras-forge:0.4.1")

    implementation(fg.deobf("maven.modrinth:twkfQtEc:EAVq8Fld"))  // moonlight
    implementation(fg.deobf("maven.modrinth:architectury-api:9.2.14+forge"))
    implementation(fg.deobf("maven.modrinth:polymorph:0.49.5+1.20.1"))
    implementation(fg.deobf("maven.modrinth:blueprint:7.1.0-forge"))
    implementation(fg.deobf("maven.modrinth:geckolib:4.4.9-forge"))
    implementation(fg.deobf("maven.modrinth:resourceful-config:2.1.0-forge"))
    implementation(fg.deobf("maven.modrinth:resourceful-lib:2.1.20-forge"))
    implementation(fg.deobf("maven.modrinth:lithostitched:1.4-forge"))
    implementation(fg.deobf("maven.modrinth:structure-gel-api:2.16.2-forge"))
    implementation(fg.deobf("maven.modrinth:cloth-config:11.1.136+forge"))
    implementation(fg.deobf("maven.modrinth:yungs-api:1.20-Forge-4.0.6"))

    implementation(fg.deobf("maven.modrinth:ct-overhaul-village:3.4.10-forge"))

    implementation(fg.deobf("maven.modrinth:fusion-connected-textures:1.1.1-forge-mc1.20.1"))
    implementation(fg.deobf("maven.modrinth:xenon-forge:0.3.31"))
    implementation(fg.deobf("maven.modrinth:oculus:1.20.1-1.7.0-forge"))
    implementation(fg.deobf("maven.modrinth:euphoria-patches:1.5.2-r5.4-forge"))
//    implementation(fg.deobf("maven.modrinth:distanthorizons:2.1.0-a-1.20.1-forge"))

    implementation(fg.deobf("maven.modrinth:jade:11.11.1+forge"))

    implementation(fg.deobf("maven.modrinth:twigs:1.20.1-3.1.1-forge"))
    implementation(fg.deobf( "maven.modrinth:etcetera:1.1.3-forge"))
    implementation(fg.deobf("maven.modrinth:artsandcrafts:1.3.2-forge"))

    implementation(fg.deobf("maven.modrinth:galosphere:1.20.1-1.4.1-forge"))

    implementation(fg.deobf("maven.modrinth:supplementaries:1.20-3.1.6-forge"))
    implementation(fg.deobf("maven.modrinth:amendments:1.20-1.2.12-forge"))
    implementation(fg.deobf("maven.modrinth:supplementaries-squared:1.20-1.1.16-forge"))

    implementation(fg.deobf("maven.modrinth:villagersplus:3.1-forge"))
    compileOnly(fg.deobf("maven.modrinth:immersive-weathering:1.20.1-2.0.2-forge"))

    implementation(fg.deobf("maven.modrinth:farmers-delight:1.20.1-1.2.5"))
    implementation(fg.deobf("maven.modrinth:rustic-delight:1.3.0-forge"))
    implementation(fg.deobf("maven.modrinth:crate-delight:24.11.06-1.20-forge"))

    implementation(fg.deobf("maven.modrinth:enderman-overhaul:1.0.4-forge"))
    implementation(fg.deobf("maven.modrinth:creeper-overhaul:3.0.2-forge"))
    compileOnly(fg.deobf("maven.modrinth:friends-and-foes-forge:forge-mc1.20.1-3.0.6"))

    implementation(fg.deobf("maven.modrinth:dye-depot:1.0.0-forge"))
    compileOnly(fg.deobf("maven.modrinth:dye-the-world:1.1.2-forge"))

    implementation(fg.deobf("maven.modrinth:woodworks:3.0.1-forge"))
    implementation(fg.deobf("maven.modrinth:upgrade-aquatic:6.0.1-forge"))
    implementation(fg.deobf("maven.modrinth:buzzier-bees:6.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:autumnity:5.0.1-forge"))
    implementation(fg.deobf("maven.modrinth:clayworks:3.0.1-forge"))
    implementation(fg.deobf("maven.modrinth:caverns-and-chasms:2.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:savage-and-ravage:6.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:atmospheric:6.1.0-forge"))
    implementation(fg.deobf("maven.modrinth:environmental:4.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:endergetic:5.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:abnormals-delight:5.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:berry-good:7.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:neapolitan:5.1.0-forge"))

    implementation(fg.deobf("maven.modrinth:sullysmod:3.2.1-beta"))

    implementation(fg.deobf("maven.modrinth:wetland-whimsy:1.1.7-1.20.1"))

    implementation(fg.deobf("maven.modrinth:oreganized:3.1.2"))
    implementation(fg.deobf("maven.modrinth:doom-gloom:1.0.2"))
    compileOnly(fg.deobf("maven.modrinth:glowroot:1.0.8.4"))

    implementation(fg.deobf("maven.modrinth:when-dungeons-arise:2.1.57-1.20.1-forge"))
    implementation(fg.deobf("maven.modrinth:lukis-grand-capitals:1.1.1+mod"))

    compileOnly(fg.deobf("maven.modrinth:the-bumblezone:7.5.13+1.20.1-forge"))

    implementation(fg.deobf("maven.modrinth:yungs-cave-biomes:1.20.1-Forge-2.0.2"))

//    implementation(fg.deobf("maven.modrinth:subtle-effects:1.7.1-forge"))
    implementation(fg.deobf("maven.modrinth:elysium-api:1.20.1-1.0.2"))
    implementation(fg.deobf("maven.modrinth:jadens-nether-expansion:2.2.1"))
    implementation(fg.deobf("maven.modrinth:terrablender:3.0.1.7-forge"))

    implementation(fg.deobf("maven.modrinth:zeta:1.20.1-1.0-24-forge"))
    compileOnly(fg.deobf("maven.modrinth:quark:1.20.1-4.0-460-forge"))
    compileOnly(fg.deobf("maven.modrinth:quark-oddities:1.20.1-forge"))
    compileOnly(fg.deobf("maven.modrinth:biome-makeover:forge-1.20.1-1.11.0"))

    implementation(fg.deobf("maven.modrinth:spawn-mod:1.0.2-forge"))

    compileOnly(fg.deobf("mezz.jei:jei-$minecraftVersion-forge-api:$jeiVersion"))
    runtimeOnly(fg.deobf("mezz.jei:jei-$minecraftVersion-forge:$jeiVersion"))
}

val Project.mixin: MixinExtension
    get() = extensions.getByType()

mixin.run {
    add(sourceSets.main.get(), "supplemental_patches.refmap.json")
    config("supplemental_patches.mixins.json")

    val debug = this.debug as DynamicProperties
    debug.setProperty("verbose", true)
    debug.setProperty("export", true)
    setDebug(debug)
}

tasks.withType<Jar> {
    archiveBaseName.set(modid)
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to modid,
                "Specification-Vendor" to vendor,
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version.toString(),
                "Implementation-Vendor" to vendor,
                "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date())
            )
        )
    }
    finalizedBy("reobfJar")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/mcmodsrepo")
        }
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}
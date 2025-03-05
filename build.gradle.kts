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
    id("org.jetbrains.kotlin.jvm") version "2.0.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.0"
}

val modid = "supplemental_patches"
val vendor = "jedlimlx"

group = property("maven_group")!!
version = property("mod_version")!!

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

minecraft {
    mappings("official", "${property("minecraft_version")}")
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
    maven { url = uri("https://maven.jaackson.me") }
    maven { url = uri("https://jitpack.io") }

    maven {
        name = "GeckoLib"
        url = uri("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        content {
            includeGroupByRegex("software\\.bernie.*")
            includeGroup("com.eliotlash.mclib")
        }
    }

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

jarJar.enable()

dependencies {
    minecraft("net.minecraftforge:forge:${property("minecraft_version")}-${property("forge_version")}")
    annotationProcessor("org.spongepowered:mixin:0.8.7:processor")
    implementation("thedarkcolour:kotlinforforge:${property("forge_kotlin_version")}")

    implementation("com.github.Fallen-Breath.conditional-mixin:conditional-mixin-forge:0.6.4")
    jarJar(group = "com.github.Fallen-Breath.conditional-mixin", name = "conditional-mixin-forge", version = "[0.6.0,)")

    // rendering / optimisation mods
    implementation(fg.deobf("maven.modrinth:xenon-forge:0.3.31"))
    implementation(fg.deobf("maven.modrinth:oculus:1.20.1-1.8.0-forge"))
    implementation(fg.deobf("maven.modrinth:euphoria-patches:1.5.2-r5.4-forge"))

    // general library mods
    implementation(fg.deobf("maven.modrinth:architectury-api:9.2.14+forge"))
    implementation(fg.deobf("software.bernie.geckolib:geckolib-forge-${property("minecraft_version")}:4.4.9"))
    implementation(fg.deobf("maven.modrinth:cloth-config:11.1.136+forge"))
    implementation(fg.deobf("maven.modrinth:terrablender:3.0.1.7-forge"))
    implementation(fg.deobf("maven.modrinth:moonlight:forge_1.20-2.13.71"))
    implementation(fg.deobf("maven.modrinth:resourceful-config:2.1.3-forge"))
    implementation(fg.deobf("maven.modrinth:resourceful-lib:2.1.29-forge"))

    implementation(fg.deobf("maven.modrinth:blueprint:7.1.1-forge"))

    // abnormals mods
    implementation(fg.deobf("maven.modrinth:abnormals-delight:5.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:atmospheric:6.1.0-forge"))
    implementation(fg.deobf("maven.modrinth:autumnity:5.0.1-forge"))
    implementation(fg.deobf("maven.modrinth:berry-good:7.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:buzzier-bees:6.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:caverns-and-chasms:2.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:clayworks:3.0.1-forge"))
    implementation(fg.deobf("maven.modrinth:endergetic:5.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:environmental:4.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:neapolitan:5.1.0-forge"))
    implementation(fg.deobf("maven.modrinth:savage-and-ravage:6.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:woodworks:3.0.1-forge"))
    implementation(fg.deobf("maven.modrinth:upgrade-aquatic:6.0.1-forge"))

    // supplementaries
    compileOnly(fg.deobf("maven.modrinth:supplementaries:1.20-3.1.20-forge"))
    compileOnly(fg.deobf("maven.modrinth:amendments:1.20-1.2.19-forge"))
    compileOnly(fg.deobf("maven.modrinth:supplementaries-squared:1.20-1.1.18-forge"))

    // oreganized
    implementation(fg.deobf("maven.modrinth:oreganized:3.1.2"))
    implementation(fg.deobf("maven.modrinth:doom-gloom:1.0.2"))

    // farmers delight
    implementation(fg.deobf("maven.modrinth:farmers-delight:1.20.1-1.2.7"))
    implementation(fg.deobf("maven.modrinth:rustic-delight:1.4.0-forge"))
    implementation(fg.deobf("maven.modrinth:crate-delight:24.11.22-1.20-forge"))

    // mob overhauls
    implementation(fg.deobf("maven.modrinth:enderman-overhaul:1.0.4-forge"))
    implementation(fg.deobf("maven.modrinth:creeper-overhaul:3.0.2-forge"))

    // fabric-exclusive
    compileOnly(fg.deobf("maven.modrinth:hybrid-aquatic:1.20.1-1.4.0"))

    // misc
    implementation(fg.deobf("maven.modrinth:galosphere:1.20.1-1.4.1-forge"))
    implementation(fg.deobf("maven.modrinth:spawn-mod:1.0.2-forge"))
    implementation(fg.deobf("maven.modrinth:twigs:1.20.1-3.1.1-forge"))

    implementation(fg.deobf("maven.modrinth:elysium-api:1.20.1-1.0.2"))
    implementation(fg.deobf("maven.modrinth:jadens-nether-expansion:2.2.1"))

    implementation(fg.deobf("maven.modrinth:sullysmod:3.2.1-beta"))

    implementation(fg.deobf("maven.modrinth:wetland-whimsy:1.1.7-1.20.1"))

    compileOnly(fg.deobf("maven.modrinth:friends-and-foes-forge:forge-mc1.20.1-3.0.6"))

    implementation(fg.deobf("maven.modrinth:yungs-api:1.20-Forge-4.0.6"))
    implementation(fg.deobf("maven.modrinth:yungs-cave-biomes:1.20.1-Forge-2.0.1"))

    implementation(fg.deobf("maven.modrinth:villagersplus:3.1-forge"))

    compileOnly(fg.deobf("maven.modrinth:immersive-weathering:1.20.1-2.0.2-forge"))

    implementation(fg.deobf("maven.modrinth:zeta:1.20.1-1.0-24-forge"))
    implementation(fg.deobf("maven.modrinth:quark:1.20.1-4.0-460-forge"))
    implementation(fg.deobf("maven.modrinth:quark-oddities:1.20.1-forge"))
    compileOnly(fg.deobf("maven.modrinth:biome-makeover:forge-1.20.1-1.11.0"))

    implementation(fg.deobf("maven.modrinth:dye-depot:1.0.0-forge"))
    implementation(fg.deobf("maven.modrinth:dye-the-world:1.1.2-forge"))

    // jei & jade
    implementation(fg.deobf("maven.modrinth:jade:11.12.3+forge"))
    implementation(fg.deobf("maven.modrinth:jei:15.20.0.106-forge"))
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
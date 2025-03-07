import java.text.SimpleDateFormat
import java.util.*

plugins {
    eclipse
    idea
    `java-library`
    `maven-publish`
    id("net.neoforged.gradle.userdev") version "7.0.173"
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0"
}

val modid = "supplemental_patches"
val vendor = "jedlimlx"

group = property("maven_group")!!
version = property("mod_version")!!

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

minecraft {
    accessTransformers {
        file("src/main/resources/META-INF/accesstransformer.cfg")
    }
}

runs {
    configureEach {
        workingDirectory(project.file("run"))
        systemProperty("forge.logging.markers", "REGISTRIES")
        systemProperty("forge.logging.console.level", "debug")

        modSource(sourceSets.main.get())
    }

    create("client") {
        systemProperty("log4j.configurationFile", "log4j2.xml")
        arguments("--username", "Player")
        jvmArguments("-Xms8G")
    }

    create("server") {}
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
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

jarJar.enable()

dependencies {
    implementation("net.neoforged:neoforge:${property("neoforge_version")}")
    annotationProcessor("org.spongepowered:mixin:0.8.7:processor")
    implementation("thedarkcolour:kotlinforforge-neoforge:${property("neoforge_kotlin_version")}")

    implementation("com.github.Fallen-Breath.conditional-mixin:conditional-mixin-neoforge:0.6.4")
    jarJar(group = "com.github.Fallen-Breath.conditional-mixin", name = "conditional-mixin-neoforge", version = "[0.6.0,)")

    // rendering / optimisation mods
    implementation("maven.modrinth:sodium:mc1.21.1-0.6.9-neoforge")
    implementation("maven.modrinth:iris:1.8.8+1.21.1-neoforge")
    implementation("maven.modrinth:euphoria-patches:1.5.2-r5.4-neoforge")

    // general library mods
    implementation("maven.modrinth:architectury-api:13.0.8+neoforge")
    implementation("software.bernie.geckolib:geckolib-neoforge-${property("minecraft_version")}:4.7.3")
    implementation("maven.modrinth:cloth-config:15.0.140+neoforge")
    implementation("maven.modrinth:terrablender:4.1.0.8-neoforge")
    implementation("maven.modrinth:moonlight:neoforge_1.21-2.17.32")
    implementation("maven.modrinth:resourceful-config:3.0.10-neoforge")
    implementation("maven.modrinth:resourceful-lib:3.0.12-neoforge")

    compileOnly("maven.modrinth:blueprint:7.1.1-forge")

    // abnormals mods
    compileOnly("maven.modrinth:abnormals-delight:5.0.0-forge")
    compileOnly("maven.modrinth:atmospheric:6.1.0-forge")
    compileOnly("maven.modrinth:autumnity:5.0.1-forge")
    compileOnly("maven.modrinth:berry-good:7.0.0-forge")
    compileOnly("maven.modrinth:buzzier-bees:6.0.0-forge")
    compileOnly("maven.modrinth:caverns-and-chasms:2.0.0-forge")
    compileOnly("maven.modrinth:clayworks:3.0.1-forge")
    compileOnly("maven.modrinth:endergetic:5.0.0-forge")
    compileOnly("maven.modrinth:environmental:4.0.0-forge")
    compileOnly("maven.modrinth:neapolitan:5.1.0-forge")
    compileOnly("maven.modrinth:savage-and-ravage:6.0.0-forge")
    compileOnly("maven.modrinth:woodworks:3.0.1-forge")
    compileOnly("maven.modrinth:upgrade-aquatic:6.0.1-forge")

    // supplementaries
    implementation("maven.modrinth:supplementaries:neoforge_1.21-3.0.39-beta")
    implementation("maven.modrinth:amendments:neoforge_1.21-1.2.24")
    implementation("maven.modrinth:supplementaries-squared:neoforge_1.21-1.2.3")

    // oreganized
    compileOnly("maven.modrinth:oreganized:3.1.2")
    compileOnly("maven.modrinth:doom-gloom:1.0.2")

    // farmers delight
    implementation("maven.modrinth:farmers-delight:1.21.1-1.2.7")
    implementation("maven.modrinth:rustic-delight:1.4.1-neoforge")
    implementation("maven.modrinth:crate-delight:24.11.22-1.21-neoforge")

    // mob overhauls
    implementation("maven.modrinth:enderman-overhaul:2.0.2-neoforge")
    implementation("maven.modrinth:creeper-overhaul:4.0.6-neoforge")

    // fabric-exclusive
    // compileOnly("maven.modrinth:hybrid-aquatic:1.20.1-1.4.0")

    // misc
    compileOnly("maven.modrinth:galosphere:1.20.1-1.4.1-forge")
    compileOnly("maven.modrinth:spawn-mod:1.0.2-forge")
    compileOnly("maven.modrinth:twigs:1.20.1-3.1.1-forge")

    compileOnly("maven.modrinth:elysium-api:1.20.1-1.0.2")
    compileOnly("maven.modrinth:jadens-nether-expansion:2.2.1")

    compileOnly("maven.modrinth:sullysmod:3.2.1-beta")

    implementation("maven.modrinth:wetland-whimsy:0.6")

    implementation("maven.modrinth:friends-and-foes-forge:neoforge-mc1.21.1-3.0.7")

    compileOnly("maven.modrinth:yungs-api:1.20-Forge-4.0.6")
    compileOnly("maven.modrinth:yungs-cave-biomes:1.20.1-Forge-2.0.1")

    compileOnly("maven.modrinth:villagersplus:3.1-forge")

    compileOnly("maven.modrinth:immersive-weathering:1.20.1-2.0.2-forge")

    compileOnly("maven.modrinth:zeta:1.20.1-1.0-24-forge")
    compileOnly("maven.modrinth:quark:1.20.1-4.0-460-forge")
    compileOnly("maven.modrinth:quark-oddities:1.20.1-forge")
    compileOnly("maven.modrinth:biome-makeover:forge-1.20.1-1.11.0")

    compileOnly("maven.modrinth:dye-depot:1.0.0-forge")
    compileOnly("maven.modrinth:dye-the-world:1.1.2-forge")

    // jei & jade
    implementation("maven.modrinth:jade:15.9.4+neoforge")
    implementation("maven.modrinth:jei:19.21.0.247-neoforge")
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
        jvmTarget = "21"
    }
}
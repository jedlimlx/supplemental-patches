import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.text.SimpleDateFormat
import java.util.*

plugins {
    eclipse
    idea
    `java-library`
    `maven-publish`
    id("net.neoforged.gradle.userdev") version "7.0.180"
    id("org.jetbrains.kotlin.jvm") version "2.1.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
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
    implementation("maven.modrinth:sodium:mc1.21.1-0.6.13-neoforge")
    implementation("maven.modrinth:iris:1.8.12+1.21.1-neoforge")
    implementation("maven.modrinth:lithium:mc1.21.1-0.15.0-neoforge")
    runtimeOnly("maven.modrinth:euphoria-patches:1.7.2-r5.6.1-neoforge")

    // general library mods
    implementation("maven.modrinth:architectury-api:13.0.8+neoforge")
    implementation("maven.modrinth:blueprint:8.0.6")
    implementation("maven.modrinth:corgilib:1.21.1-5.0.0.5-NeoForge")
    implementation("maven.modrinth:data-anchor:2.0.0.12-neoforge")
    implementation("software.bernie.geckolib:geckolib-neoforge-${property("minecraft_version")}:4.7.7")
    implementation("maven.modrinth:glitchcore:2.1.0.0-neoforge")
    implementation("maven.modrinth:cloth-config:15.0.140+neoforge")
    implementation("maven.modrinth:terrablender:4.1.0.8-neoforge")
    implementation("maven.modrinth:trimmed:1.21-3.0.0+neoforge")
    implementation("maven.modrinth:moonlight:1.21-2.22.4-neoforge")
    implementation("maven.modrinth:oh-the-trees-youll-grow:1.21.1-5.0.14-NeoForge")
    implementation("maven.modrinth:puzzles-lib:v21.1.38-1.21.1-NeoForge")
    implementation("maven.modrinth:resourceful-config:3.0.10-neoforge")
    implementation("maven.modrinth:resourceful-lib:3.0.12-neoforge")
    compileOnly("maven.modrinth:runiclib:4.3.4-forge")

    // abnormals mods
    compileOnly("maven.modrinth:abnormals-delight:5.0.0-forge")
    implementation("maven.modrinth:atmospheric:7.0.0")
    implementation("maven.modrinth:autumnity:6.0.0")
    implementation("maven.modrinth:berry-good:8.0.0")
    implementation("maven.modrinth:buzzier-bees:7.0.0")
    compileOnly("maven.modrinth:caverns-and-chasms:2.0.0-forge")
    implementation("maven.modrinth:clayworks:4.0.0")
    compileOnly("maven.modrinth:endergetic:5.0.0-forge")
    compileOnly("maven.modrinth:environmental:4.0.0-forge")
    implementation("maven.modrinth:neapolitan:6.0.0")
    compileOnly("maven.modrinth:savage-and-ravage:6.0.0-forge")
    implementation("maven.modrinth:woodworks:4.0.0")
    implementation("maven.modrinth:upgrade-aquatic:7.0.0")

    // supplementaries
    implementation("maven.modrinth:supplementaries:neoforge_1.21-3.4.9")
    implementation("maven.modrinth:amendments:neoforge_1.21-2.0.5")
    implementation("maven.modrinth:supplementaries-squared:neoforge_1.21-1.2.12")
    implementation("maven.modrinth:snowy-spirit:neoforge_1.21.1-3.0.17")

    // oreganized
    implementation("maven.modrinth:oreganized:5.1.1")
    compileOnly("maven.modrinth:doom-gloom:2.0.0-forge")

    compileOnly("maven.modrinth:windswept:3.0.3")

    // farmers delight
    implementation("maven.modrinth:farmers-delight:1.21.1-1.2.7")
    implementation("maven.modrinth:rustic-delight:1.4.1-neoforge")
    implementation("maven.modrinth:crate-delight:24.11.22-1.21-neoforge")
    implementation("maven.modrinth:my-nethers-delight:1.8")
    implementation("maven.modrinth:ends-delight:2.5.1+neoforge.1.21.1")
    compileOnly("maven.modrinth:dungeons_delight:1.2.6")

    // mob overhauls
    implementation("maven.modrinth:enderman-overhaul:2.0.2-neoforge")
    implementation("maven.modrinth:creeper-overhaul:4.0.6-neoforge")

    // biome mods
    compileOnly("maven.modrinth:biomes-o-plenty:21.1.0.7-neoforge")
    implementation("maven.modrinth:oh-the-biomes-weve-gone:2.4.3-NeoForge")

    // fabric-exclusive
    compileOnly("maven.modrinth:cinderscapes:4.0.10")

    compileOnly("maven.modrinth:gipples-galore:1.1.1")
    compileOnly("maven.modrinth:nears:2.1.2-1.21.1")
    compileOnly("maven.modrinth:pearfection:1.3.1-1.21.1")

    compileOnly("maven.modrinth:trailier-tales:1.1.2-mc1.21.1")
    compileOnly("maven.modrinth:wilder-wild:4.0.2-mc1.21.1")

    compileOnly("maven.modrinth:thermoo:4.4")
    compileOnly("maven.modrinth:frostiful:2.2-beta.1")
    compileOnly("maven.modrinth:scorchful:0.15-beta.1")

    // misc
    compileOnly("maven.modrinth:galosphere:1.20.1-1.4.1-forge")
    compileOnly("maven.modrinth:spawn-mod:1.0.2-forge")
    compileOnly("maven.modrinth:twigs:1.20.1-3.1.1-forge")

    compileOnly("maven.modrinth:elysium-api:1.20.1-1.1.0")
    compileOnly("maven.modrinth:jadens-nether-expansion:2.3.2")

    implementation("maven.modrinth:rubinated-nether:2.0.0")

    compileOnly("maven.modrinth:soulfulnether:1.0.0-forge")

    implementation("maven.modrinth:pigsteel-forge:7.1.0")

    compileOnly("maven.modrinth:sullysmod:3.2.1-beta")

    implementation("maven.modrinth:wetland-whimsy:2.0-1.21.1")

    implementation("maven.modrinth:friends-and-foes-forge:neoforge-mc1.21.1-3.0.7")

    implementation("maven.modrinth:enderscape:1.0.5-neoforge")

    implementation("maven.modrinth:illager-invasion:v21.1.6-1.21.1-NeoForge")

    implementation("maven.modrinth:enhanced-celestials:6.0.2.4-neoforge")

    implementation("maven.modrinth:yungs-api:1.21.1-NeoForge-5.1.6")
    implementation("maven.modrinth:yungs-cave-biomes:1.21.1-NeoForge-3.1.1")

    compileOnly("maven.modrinth:zeta:1.20.1-1.0-24-forge")
    compileOnly("maven.modrinth:quark:1.20.1-4.0-460-forge")
    compileOnly("maven.modrinth:quark-oddities:1.20.1-forge")

    compileOnly("maven.modrinth:dye-depot:1.0.0-forge")
    compileOnly("maven.modrinth:dye-the-world:1.1.2-forge")

    // jei & jade
    runtimeOnly("maven.modrinth:jade:15.9.4+neoforge")
    runtimeOnly("maven.modrinth:jei:19.21.0.247-neoforge")
}

tasks.withType<Jar> {
    archiveBaseName.set(modid)
    archiveVersion.set(project.version.toString())
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
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    id("fabric-loom")
    `maven-publish`
    java
}

group = property("maven_group")!!
version = property("mod_version")!!

repositories {
    mavenCentral()
    maven {
        name = "ParchmentMC"
        url = uri("https://maven.parchmentmc.org")
        content {
            includeGroupAndSubgroups("org.parchmentmc")
        }
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
        url = uri("https://maven.jamieswhiteshirt.com/libs-release")
        content {
            includeGroup("com.jamieswhiteshirt")
        }
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
    maven { url = uri("https://mvn.devos.one/releases/") }

    maven {
        name = "Ladysnake Mods"
        url = uri("https://maven.ladysnake.org/releases")
    }

    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/")
    }

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

loom {
    accessWidenerPath = file("src/main/resources/supplemental_patches.accesswidener")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${property("minecraft_version")}:${property("parchment_version")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc:fabric-language-kotlin:${property("fabric_kotlin_version")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    modImplementation("com.github.Fallen-Breath.conditional-mixin:conditional-mixin-fabric:0.6.4")
    include("com.github.Fallen-Breath.conditional-mixin:conditional-mixin-fabric:0.6.4")

    localRuntime("org.joml:joml:1.10.4")
    localRuntime("org.anarres:jcpp:1.4.14")
    localRuntime("io.github.douira:glsl-transformer:2.0.2")

    implementation("io.sigpipe:jbsdiff:1.0")
    implementation("com.eliotlash.mclib:mclib:20")
    implementation("com.electronwill.night-config:toml:3.8.1")
    implementation("org.reflections:reflections:0.10.2")

    // rendering / optimisation mods
    modImplementation("maven.modrinth:sodium:mc1.21.4-0.6.10-fabric")
    modImplementation("maven.modrinth:iris:1.8.8+1.21.4-fabric")
    modImplementation("maven.modrinth:euphoria-patches:1.5.2-r5.4-fabric")

    // general library mods
    modImplementation("maven.modrinth:architectury-api:15.0.3+fabric")
    compileOnly("maven.modrinth:blueprint:8.0.3")
    modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-base:6.2.2")
    modImplementation("org.ladysnake.cardinal-components-api:cardinal-components-entity:6.2.2")
    modImplementation("maven.modrinth:cloth-config:17.0.144+fabric")
    modImplementation("maven.modrinth:frozenlib:2.0.3-mc1.21.4")
    modImplementation("software.bernie.geckolib:geckolib-fabric-${property("minecraft_version")}:4.8.4")
    modImplementation("maven.modrinth:midnightlib:1.7.0+1.21.4-fabric")
    compileOnly("maven.modrinth:moonlight:fabric_1.21-2.17.32")
    modImplementation("maven.modrinth:resourceful-config:3.4.3-fabric")
    modImplementation("maven.modrinth:resourceful-lib:3.4.5-fabric")
    modImplementation("maven.modrinth:terrablender:4.3.0.2-fabric")
    modImplementation("com.terraformersmc.terraform-api:terraform-wood-api-v1:13.0.0-beta.1")
    modImplementation("maven.modrinth:yacl:3.6.5+1.21.4-fabric")

    // abnormals mods
    compileOnly("maven.modrinth:abnormals-delight:5.0.0-forge")
    compileOnly("maven.modrinth:atmospheric:6.1.0-forge")
    compileOnly("maven.modrinth:autumnity:5.0.1-forge")
    compileOnly("maven.modrinth:berry-good:8.0.0")
    compileOnly("maven.modrinth:buzzier-bees:7.0.0")
    compileOnly("maven.modrinth:caverns-and-chasms:2.0.0-forge")
    compileOnly("maven.modrinth:clayworks:4.0.0")
    compileOnly("maven.modrinth:endergetic:5.0.0-forge")
    compileOnly("maven.modrinth:environmental:4.0.0-forge")
    compileOnly("maven.modrinth:neapolitan:5.1.0-forge")
    compileOnly("maven.modrinth:savage-and-ravage:6.0.0-forge")
    compileOnly("maven.modrinth:woodworks:4.0.0")
    compileOnly("maven.modrinth:upgrade-aquatic:6.0.1-forge")

    // supplementaries
    compileOnly("maven.modrinth:supplementaries:fabric_1.21-3.0.40-beta")
    compileOnly("maven.modrinth:amendments:fabric_1.21-1.2.24")
    compileOnly("maven.modrinth:supplementaries-squared:fabric_1.21-1.2.3")

    // oreganized
    compileOnly("maven.modrinth:oreganized:3.1.2")
    compileOnly("maven.modrinth:doom-gloom:1.0.2")

    // farmers delight
    compileOnly("maven.modrinth:farmers-delight-refabricated:1.21.1-3.0.1")
    compileOnly("maven.modrinth:rustic-delight:1.3.3-fabric,1.21.1")
    compileOnly("maven.modrinth:crate-delight:24.11.22-1.21-fabric")
    compileOnly("maven.modrinth:my-nethers-delight-refabricated:2.0.3+1.7.8")

    // mob overhauls
    compileOnly("maven.modrinth:enderman-overhaul:2.0.2-neoforge")
    compileOnly("maven.modrinth:creeper-overhaul:4.0.6-fabric")

    // fabric-exclusive
    modImplementation("maven.modrinth:cinderscapes:5.2.1")

    modImplementation("maven.modrinth:gipples-galore:1.1.8-1.21.4")
    modImplementation("maven.modrinth:nears:2.1.4-1.21.4")
    modImplementation("maven.modrinth:pearfection:1.2.6-1.21.4")

    modImplementation("maven.modrinth:trailier-tales:1.1.3-mc1.21.4")
    modImplementation("maven.modrinth:wilder-wild:4.0.3-mc1.21.4")

    modImplementation("maven.modrinth:thermoo:5.3")
    modCompileOnly("maven.modrinth:frostiful:2.2+1.21.4")
    modCompileOnly("maven.modrinth:scorchful:0.15.1+1.21.4")

    // misc
    compileOnly("maven.modrinth:galosphere:1.21-1.4.2-fabric")
    compileOnly("maven.modrinth:spawn-mod:1.0.3-fabric")
    compileOnly("maven.modrinth:twigs:3.1.0-fabric")

    compileOnly("maven.modrinth:elysium-api:1.20.1-1.1.0")
    compileOnly("maven.modrinth:jadens-nether-expansion:2.3.2")

    compileOnly("maven.modrinth:rubinated-nether:1.3.1c")

    compileOnly("maven.modrinth:soulfulnether:1.0.0")

    modImplementation("maven.modrinth:pigsteel-fabric:2.4.0-1.21.4")

    compileOnly("maven.modrinth:sullysmod:3.2.1-beta")

    compileOnly("maven.modrinth:wetland-whimsy:1.1.7-1.20.1")

    compileOnly("maven.modrinth:rodspawn:1.0.2")

    modImplementation("maven.modrinth:friends-and-foes:fabric-mc1.21.4-4.0.1")

    compileOnly("maven.modrinth:yungs-api:1.20-Fabric-4.0.6")
    compileOnly("maven.modrinth:yungs-cave-biomes:1.20.1-Fabric-2.0.2")

    compileOnly("maven.modrinth:zeta:1.20.1-1.0-24-forge")
    compileOnly("maven.modrinth:quark:1.20.1-4.0-460-forge")
    compileOnly("maven.modrinth:quark-oddities:1.20.1-forge")

    compileOnly("maven.modrinth:dye-depot:1.0.3-fabric")
    compileOnly("maven.modrinth:dye-the-world:1.1.2-forge")

    // jei & jade
    modRuntimeOnly("maven.modrinth:jade:17.2.2+fabric")
    compileOnly("mezz.jei:jei-1.21.1-fabric:19.21.1.248")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(getProperties())
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(remapJar) {
                    builtBy(remapJar)
                }
                artifact(kotlinSourcesJar) {
                    builtBy(remapSourcesJar)
                }
            }
        }

        // select the repositories you want to publish to
        repositories {
            // uncomment to publish to the local maven
            // mavenLocal()
        }
    }

    compileKotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}
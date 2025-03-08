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

    modImplementation("com.github.Chocohead:Fabric-ASM:2.3")
    modImplementation("com.jamieswhiteshirt:reach-entity-attributes:2.4.0")

    val portLibModules = "accessors,base,networking,tags,config,tool_actions,loot,lazy_registration,recipe_book_categories"
    portLibModules.split(",").forEach {
        modImplementation("io.github.fabricators_of_create.Porting-Lib:$it:2.3.8+1.20.1")
    }

    // rendering / optimisation mods
    modImplementation("maven.modrinth:sodium:mc1.20.1-0.5.13-fabric")
    modImplementation("maven.modrinth:indium:1.0.36+mc1.20.1")
    modImplementation("maven.modrinth:iris:1.7.6+1.20.1")
    modImplementation("maven.modrinth:euphoria-patches:1.5.2-r5.4-fabric")

    // general library mods
    modImplementation("maven.modrinth:athena-ctm:3.1.2-fabric")
    modImplementation("maven.modrinth:architectury-api:9.2.14+fabric")
    compileOnly("maven.modrinth:blueprint:7.1.1-forge")
    modImplementation("maven.modrinth:cloth-config:11.1.136+fabric")
    modImplementation("software.bernie.geckolib:geckolib-fabric-${property("minecraft_version")}:4.4.9")
    modImplementation("maven.modrinth:midnightlib:1.4.1-fabric")
    modImplementation("maven.modrinth:moonlight:fabric_1.20-2.13.71")
    modImplementation("maven.modrinth:porting_lib:2.3.8+1.20.1")
    modImplementation("maven.modrinth:resourceful-config:2.1.3-fabric")
    modImplementation("maven.modrinth:resourceful-lib:2.1.29-fabric")
    modImplementation("maven.modrinth:terrablender:3.0.1.7-fabric")
    modImplementation("com.terraformersmc.terraform-api:terraform-wood-api-v1:7.0.0-beta.1")

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
    compileOnly("maven.modrinth:supplementaries:1.20-3.1.16-fabric")
    compileOnly("maven.modrinth:amendments:1.20-1.2.19-fabric")
    compileOnly("maven.modrinth:supplementaries-squared:1.20-1.1.18-fabric")

    // oreganized
    compileOnly("maven.modrinth:oreganized:3.1.2")
    compileOnly("maven.modrinth:doom-gloom:1.0.2")

    // farmers delight
    modImplementation("maven.modrinth:farmers-delight-refabricated:1.20.1-2.3.0")
    modImplementation("maven.modrinth:rustic-delight:1.3.2-fabric")
    modImplementation("maven.modrinth:crate-delight:24.11.22-1.20-fabric")

    // mob overhauls
    modImplementation("maven.modrinth:enderman-overhaul:1.0.4-fabric")
    modImplementation("maven.modrinth:creeper-overhaul:3.0.2-fabric")

    // fabric-exclusive
    modImplementation("maven.modrinth:hybrid-aquatic:1.4.1-1.20.1")

    modImplementation("maven.modrinth:soulfulnether:1.0.0")
    modImplementation("maven.modrinth:cinderscapes:4.0.10")

    modImplementation("maven.modrinth:gipples-galore:1.0.0")
    modImplementation("maven.modrinth:nears:2.1.2-1.20.1")
    modImplementation("maven.modrinth:pearfection:1.1.1")

    // misc
    modImplementation("maven.modrinth:galosphere:1.20.1-1.4.1-fabric")
    modImplementation("maven.modrinth:spawn-mod:1.0.3-fabric")
    modImplementation("maven.modrinth:twigs:3.1.0-fabric")

    compileOnly("maven.modrinth:elysium-api:1.20.1-1.0.2")
    compileOnly("maven.modrinth:jadens-nether-expansion:2.2.1")

    compileOnly("maven.modrinth:sullysmod:3.2.1-beta")

    compileOnly("maven.modrinth:wetland-whimsy:1.1.7-1.20.1")

    modImplementation("maven.modrinth:friends-and-foes:fabric-mc1.20.1-3.0.7")

    modImplementation("maven.modrinth:yungs-api:1.20-Fabric-4.0.6")
    modImplementation("maven.modrinth:yungs-cave-biomes:1.20.1-Fabric-2.0.2")

    compileOnly("maven.modrinth:villagersplus:3.1-forge")

    compileOnly("maven.modrinth:immersive-weathering:1.20.1-2.0.2-forge")

    compileOnly("maven.modrinth:zeta:1.20.1-1.0-24-forge")
    compileOnly("maven.modrinth:quark:1.20.1-4.0-460-forge")
    compileOnly("maven.modrinth:quark-oddities:1.20.1-forge")
    compileOnly("maven.modrinth:biome-makeover:fabric-1.20.1-1.11.4")

    modImplementation("maven.modrinth:dye-depot:1.0.3-fabric")
    compileOnly("maven.modrinth:dye-the-world:1.1.2-forge")

    // jei & jade
    modImplementation("maven.modrinth:jade:11.12.3+fabric")
//    modRuntimeOnly("mezz.jei:jei-1.20.1-fabric:15.20.0.106")
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
        kotlinOptions.jvmTarget = "17"
    }
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()
}
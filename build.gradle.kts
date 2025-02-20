import java.text.SimpleDateFormat
import java.util.*

plugins {
    eclipse
    idea
    `java-library`
    `maven-publish`
    id("net.neoforged.gradle.userdev") version "7.0.173"
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.21"
}

group = "io.github.jedlimlx.supplemental_patches"
version = "0.1.0-beta"

val modid = "supplemental_patches"
val vendor = "jedlimlx"

val minecraftVersion = "1.21.1"
val neoVersion = "21.1.120"
val jeiVersion = "19.21.1.248"

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

println(
    "Java: ${System.getProperty("java.version")} JVM: ${System.getProperty("java.vm.version")}(${
        System.getProperty(
            "java.vendor"
        )
    }) Arch: ${System.getProperty("os.arch")}"
)

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
    create("gameTestServer") {}

    create("data") {
        arguments(
            "--mod",
            modid,
            "--all",
            "--output",
            "src/generated/resources/",
            "--existing",
            "src/main/resources"
        )
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

dependencies {
    implementation("net.neoforged:neoforge:$neoVersion")
    annotationProcessor("org.spongepowered:mixin:0.8.7:processor")
    implementation("thedarkcolour:kotlinforforge-neoforge:5.6.0")

    implementation("maven.modrinth:sodium:mc1.21.1-0.6.9-neoforge")
    implementation("maven.modrinth:iris:1.8.6+1.21.1-neoforge")
    implementation("maven.modrinth:wetland-whimsy:0.6")

    compileOnly("mezz.jei:jei-$minecraftVersion-neoforge-api:$jeiVersion")
    runtimeOnly("mezz.jei:jei-$minecraftVersion-neoforge:$jeiVersion")
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
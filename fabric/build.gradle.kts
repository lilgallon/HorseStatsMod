plugins {
    id("fabric-loom") version loomVersion
    kotlin("jvm")
    `maven-publish`
    java
}

base {
    archivesBaseName = "$modArchive-fabric"
}

group = "$modGroup.fabric"
version = modVersion

repositories {
    maven("https://maven.shedaniel.me/") // cloth config
    maven("https://maven.terraformersmc.com/releases/") // mod menu
}

val inJar = configurations.create("inJar")
configurations.implementation.extendsFrom(inJar)

dependencies {
    inJar(project(":core"))

    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings("net.fabricmc:yarn:$yarnMappings:v2")
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")

    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")
    modApi("me.shedaniel.cloth:cloth-config-fabric:$clothConfigVersion") {
        exclude("net.fabricmc.fabric-api")
    }
    modApi("com.terraformersmc:modmenu:$modMenuVersion")
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from("LICENSE")
        from(
            inJar.map {
                if (it.isDirectory) it else zipTree(it)
            }
        )
    }

    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
}

java {
    withSourcesJar()
}

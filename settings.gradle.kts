rootProject.name = "HorseStatsMod"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.minecraftforge.net/") {
            name = "Forge"
        }
    }
}

include("core")
include("fabric")
include("forge")

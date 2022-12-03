import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.text.SimpleDateFormat
import java.util.Date

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinGradlePlugin")
    }
}

plugins {
    java
    kotlin("jvm")
    id("net.minecraftforge.gradle") version forgeGradlePlugin
}
apply(from = "https://raw.githubusercontent.com/thedarkcolour/KotlinForForge/site/thedarkcolour/kotlinforforge/gradle/kff-$kotlinForForge.gradle")

group = "$modGroup.forge"
version = modVersion

val inJar = configurations.create("inJar")
configurations.implementation.extendsFrom(inJar)

repositories {
    mavenCentral()
    maven("https://maven.shedaniel.me/") // cloth config
}

dependencies {
    minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")
    api(fg.deobf("me.shedaniel.cloth:cloth-config-forge:$clothConfigVersion"))
    inJar(project(":core"))
}

val Project.minecraft: net.minecraftforge.gradle.common.util.MinecraftExtension
    get() = extensions.getByType()

minecraft.let {
    it.mappings("official", minecraftVersion)
    it.runs {
        create("client") {
            workingDirectory(project.file("run"))
            property("forge.logging.console.level", "debug")
            mods {
                this.create(modVersion) {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

tasks {
    val javaVersion = JavaVersion.valueOf("VERSION_$jvmTarget")
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.toString()
        targetCompatibility = javaVersion.toString()
        if (JavaVersion.current().isJava9Compatible) {
            options.release.set(javaVersion.toString().toInt())
        }
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion.toString()
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
        }
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }

    withType<Jar> {
        from(
            inJar.map {
                if (it.isDirectory) it else zipTree(it)
            }
        )
        archiveBaseName.set("$modArchive-forge")
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version,
                    "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd").format(Date())
                )
            )
        }
        finalizedBy("reobfJar")
    }
}

plugins {
    kotlin("jvm")
}

group = "$modGroup.core"
version = modVersion

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
}

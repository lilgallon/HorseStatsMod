plugins {
    kotlin("jvm")
}

group = "$modGroup.core"
version = modVersion

repositories {
    mavenCentral()
}

dependencies {
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = jvmTarget
    }
}

plugins {
    kotlin("jvm") version "2.1.20"
}

group = "dev.gengy"
version = "0.1.1"

repositories {
    mavenCentral()
}

allprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
            name = "spigotmc-repo"
        }
        mavenLocal()
    }
}

kotlin {
    jvmToolchain(21)
}
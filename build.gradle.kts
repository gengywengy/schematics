import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "2.1.20"
    id("com.vanniktech.maven.publish") version "0.31.0"
}

group = "dev.gengy"
version = "0.1.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") {
        name = "spigotmc-repo"
    }
}

dependencies {
    api("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    api("net.kyori:adventure-nbt:4.19.0")
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

mavenPublishing {
    coordinates(
        groupId = "dev.gengy",
        artifactId = "schematics",
        version = version as String
    )
    pom {
        name.set("Schematics")
        description.set("A library for quickly and efficiently pasting schematics.")
        inceptionYear.set("2025")
        url.set("https://github.com/gengywengy/schematics/")

        licenses {
            license {
                name.set("Apache License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
                distribution.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }

        developers {
                developer {
                    id.set("gengyweny")
                    name.set("Gengar")
                    url.set("https://github.com/gengywengy")
                }
        }

        scm {
            url.set("https://github.com/gengywengy/schematics/")
            connection.set("scm:git:git://github.com/gengywengy/schematics.git")
            developerConnection.set("scm:git:ssh://git@github.com/gengywengy/schematics.git")
        }
    }

    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, automaticRelease = true)
    signAllPublications()
}
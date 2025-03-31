plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "schem"

include(":api")
include(
    ":spigot:common",
    ":spigot:1_21_R1"
)

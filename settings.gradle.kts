pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "stellar-terminal"

include(
    ":shared",
    ":server:catalog-service",
    ":server:satellite-service",
    ":server:user-service",
    ":client"
)
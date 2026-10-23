pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "catalog-service"

// Репозиторий shared живёт отдельно. Если его исходники лежат рядом —
// локально в монорепо (../../shared) или их выкачал checkout внутрь workspace в CI (shared) —
// подставляем исходники вместо скачивания артефакта из mavenLocal()/GitHub Packages.
val sharedBuildDir = listOf("shared", "../../shared").firstOrNull { file(it).isDirectory }
if (sharedBuildDir != null) {
    includeBuild(sharedBuildDir) {
        dependencySubstitution {
            substitute(module("com.artemwhite:shared")).using(project(":"))
        }
    }
}
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

// Остаётся в монорепо
include(":client")

// Сервисы и shared живут в отдельных репозиториях.
// Для локальной разработки разработчик клонирует их в server/ и shared/,
// и Gradle подставляет локальные исходники вместо скачивания артефактов.
if (file("server/catalog-service").exists()) {
    includeBuild("server/catalog-service") {
        dependencySubstitution {
            substitute(module("com.artemwhite:catalog-service")).using(project(":"))
        }
    }
}
if (file("server/satellite-service").exists()) {
    includeBuild("server/satellite-service") {
        dependencySubstitution {
            substitute(module("com.artemwhite:satellite-service")).using(project(":"))
        }
    }
}
if (file("server/user-service").exists()) {
    includeBuild("server/user-service") {
        dependencySubstitution {
            substitute(module("com.artemwhite:user-service")).using(project(":"))
        }
    }
}
if (file("shared").exists()) {
    includeBuild("shared") {
        dependencySubstitution {
            substitute(module("com.artemwhite:shared")).using(project(":"))
        }
    }
}
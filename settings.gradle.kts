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
includeBuild("server/catalog-service") {
    dependencySubstitution {
        substitute(module("com.artemwhite:catalog-service")).using(project(":"))
    }
}
includeBuild("server/satellite-service") {
    dependencySubstitution {
        substitute(module("com.artemwhite:satellite-service")).using(project(":"))
    }
}
includeBuild("server/user-service") {
    dependencySubstitution {
        substitute(module("com.artemwhite:user-service")).using(project(":"))
    }
}
includeBuild("shared") {
    dependencySubstitution {
        substitute(module("com.artemwhite:shared")).using(project(":"))
    }
}
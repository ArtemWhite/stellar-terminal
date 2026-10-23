plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    jacoco
}

group = "com.artemwhite"
version = "1.0.0"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/ArtemWhite/shared")
        credentials {
            username = System.getenv("GITHUB_ACTOR") ?: ""
            password = System.getenv("GITHUB_TOKEN") ?: ""
        }
    }
}

kotlin {
    jvmToolchain(17)
}

// Keep a single jar in build/libs so Docker's `COPY build/libs/*.jar` is unambiguous.
tasks.jar {
    enabled = false
}

// Integration tests live in their own source set: separate directories, classpath
// and task, so `./gradlew test` runs only unit tests and `./gradlew integrationTest`
// runs the DB-backed tests with their own application-integration.yml.
val integrationTestSourceSet = sourceSets.create("integrationTest") {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}
configurations["integrationTestImplementation"].extendsFrom(configurations.testImplementation.get())
configurations["integrationTestRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
    implementation(libs.shared)
    implementation(platform(libs.spring.boot.bom))
    implementation(libs.spring.boot.webmvc)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.actuator)
    implementation(libs.kotlin.reflect)
    implementation(libs.spring.boot.liquibase)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.junit.platform.launcher)
    // Testcontainers is only needed by the DB-backed integration tests, not the unit tests.
    "integrationTestImplementation"(libs.testcontainers.postgresql)
    "integrationTestImplementation"(libs.testcontainers.jdbc)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Test>("integrationTest") {
    description = "Runs integration tests (requires Docker)."
    group = "verification"
    testClassesDirs = integrationTestSourceSet.output.classesDirs
    classpath = integrationTestSourceSet.runtimeClasspath
    useJUnitPlatform()
    shouldRunAfter(tasks.test)
}

// JaCoCo coverage report. Aggregates unit (test) and, when already produced,
// integration (integrationTest) exec data. `integrationTest` needs Docker, so
// its *.exec file is folded in only if it exists — the report never forces a
// Docker run on its own.
tasks.jacocoTestReport {
    dependsOn(tasks.test)
    executionData.setFrom(
        fileTree(layout.buildDirectory.get().asFile) { include("jacoco/*.exec") },
    )
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("**/CatalogServiceApplication*")
        },
    )
}

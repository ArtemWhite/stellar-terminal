plugins {
    kotlin("jvm") version "2.4.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.artemwhite:catalog-service:1.0.0")
    implementation("com.artemwhite:satellite-service:1.0.0")
    implementation("com.artemwhite:user-service:1.0.0")
    implementation("com.artemwhite:shared:1.0.0")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
}
plugins {
    kotlin("jvm") version "2.3.21"
    application
}

group = "dev.bapps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass.set("dev.bapps.MainKt")
}

tasks.test {
    useJUnitPlatform()
}
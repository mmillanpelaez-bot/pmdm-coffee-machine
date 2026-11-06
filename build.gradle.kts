plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "org.example"
version = "1.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("org.example.MainKt")
}

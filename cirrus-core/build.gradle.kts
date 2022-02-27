@file:Suppress("PropertyName")
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply true
    kotlin("plugin.serialization") version "1.5.21"
}

group = "cyou.shinobi9"
version = "0.0.2"

val java8 = JavaVersion.VERSION_1_8.toString()

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xopt-in=kotlin.RequiresOptIn")
        jvmTarget = java8
        sourceCompatibility = java8
        targetCompatibility = java8
    }
}

dependencies {
    api("io.ktor:ktor-client-core:1.6.1")
    api("io.ktor:ktor-client-cio:1.6.1")
    api("io.ktor:ktor-client-websockets:1.6.1")
    api("io.ktor:ktor-client-logging-jvm:1.6.1")
    api("io.ktor:ktor-client-serialization:1.6.1")
//    api("org.jetbrains.kotlinx:atomicfu:0.16.2")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    api("io.github.microutils:kotlin-logging-jvm:2.0.10")
    testImplementation("ch.qos.logback:logback-classic:1.2.5")
}

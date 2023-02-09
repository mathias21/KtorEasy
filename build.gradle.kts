import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = AppConfig.group
version = AppConfig.versionName

plugins {
    kotlin("jvm") version Dependencies.Versions.kotlinPluginVersion
    id("io.ktor.plugin") version Dependencies.Versions.ktorPluginVersion
}

apply(plugin = "com.github.johnrengelman.shadow")

application {
    mainClass.set("com.batcuevasoft.ApplicationKt")
}

ktor {
    fatJar {
        archiveFileName.set("KtorEasy.jar")
    }
}

repositories {
    mavenCentral()
}

buildscript {
    repositories {
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath("com.github.jengelman.gradle.plugins:shadow:${Dependencies.Versions.shadowwarVersion}")
    }
}


dependencies {

    implementation(kotlin("stdlib-jdk8"))
    implementation(Dependencies.ktorNetty)
    implementation(Dependencies.ktorAuth)
    implementation(Dependencies.ktorJwt)
    implementation(Dependencies.ktorContentNegotiation)
    implementation(Dependencies.ktorSerialization)
    implementation(Dependencies.ktorLogging)
    implementation(Dependencies.ktorStatusPages)
    implementation(Dependencies.ktorMetrics)
    implementation(Dependencies.logback)
    implementation(Dependencies.exposedCore)
    implementation(Dependencies.exposedDao)
    implementation(Dependencies.exposedJdbc)
    implementation(Dependencies.hikari)
    implementation(Dependencies.h2)
    implementation(Dependencies.mysqlConnector)

    implementation(Dependencies.bcrypt)

    // Koin for Kotlin
    implementation(Dependencies.koin)

    // Koin for Unit tests
    testImplementation(Dependencies.koinTest)
    testImplementation(Dependencies.ktorServerTest)
    testImplementation(Dependencies.assertJ)
    testImplementation(Dependencies.junit)
    testRuntimeOnly(Dependencies.junitEngine)
    testImplementation(Dependencies.mockK)
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

// config JVM target to 1.8 for kotlin compilation tasks
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks {
    "shadowJar"(ShadowJar::class) {
        baseName = "KtorEasy"
        version = "0.9"
    }
}

plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    `maven-publish`
}

base {
    java.toolchain.languageVersion = JavaLanguageVersion.of(8)
}

group = "su.mandora"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
}

tasks.withType<JavaCompile> {
    options.release = 8
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = project.group as String
            artifactId = rootProject.name
            version = project.version as String

            from(components["java"])

            pom {
                name.set(rootProject.name)
                description.set("Simple library to log into yggdrasil (minecraft/scrolls) accounts")
                url.set("https://github.com/Sumandora/AuthLib")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://raw.githubusercontent.com/Sumandora/AuthLib/master/LICENSE")
                    }
                }
            }
        }
    }
}
plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("de.eldoria.plugin-yml.paper") version "0.8.0"
    id("com.diffplug.spotless") version "7.0.0.BETA2"
}

group = "fi.fabianadrian"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")

    implementation("org.spongepowered:configurate-yaml:4.1.2") {
        exclude("org.yaml")
    }

    implementation("org.incendo:cloud-paper:2.0.0-beta.13")
    implementation("org.incendo:cloud-minecraft-extras:2.0.0-beta.13")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        minimize()
        sequenceOf(
            "org.incendo.cloud",
            "org.spongepowered.configurate",
            "io.leangen.geantyref"
        ).forEach { pkg ->
            relocate(pkg, "fi.fabianadrian.fawarp.dependency.$pkg")
        }
    }
}

paper {
    main = "fi.fabianadrian.fawarp.FAWarp"
    apiVersion = "1.21.10"
    authors = listOf("FabianAdrian")
}

spotless {
    java {
        endWithNewline()
        formatAnnotations()
        indentWithTabs()
        removeUnusedImports()
        trimTrailingWhitespace()
    }
}
plugins {
    id("java")
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
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    paperLibrary("org.spongepowered:configurate-yaml:4.1.2") {
        exclude("org.yaml")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks {
    generatePaperPluginDescription {
        useDefaultCentralProxy()
    }
}

paper {
    main = "fi.fabianadrian.fawarp.FAWarp"
    loader = "fi.fabianadrian.fawarp.FAWarpLoader"
    apiVersion = "1.21.11"
    generateLibrariesJson = true
    authors = listOf("FabianAdrian", "Jim (AnEnragedPigeon)")
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
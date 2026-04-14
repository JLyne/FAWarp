import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("de.eldoria.plugin-yml.paper") version "0.9.0"
    id("com.diffplug.spotless") version "7.0.0.BETA2"
}

group = "fi.fabianadrian"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")

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
    apiVersion = "26.1.2"
    generateLibrariesJson = true
    authors = listOf("FabianAdrian", "Jim (AnEnragedPigeon)")
    permissions {
        register("fwarp.command.root.reload") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows use of /fawarp reload"
        }

        register("fawarp.command.setwarp") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows use of /setwarp"
        }

        register("fawarp.command.unsetwarp") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows use of /unsetwarp"
        }

        register("fawarp.command.warp") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows use of /warp to teleport yourself"
        }

        register("fawarp.command.warp.player") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows use of /warp to teleport other players"
        }

        register("fawarp.command.warplist") {
            default = BukkitPluginDescription.Permission.Default.OP
            description = "Allows use of /warplist"
        }
    }
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
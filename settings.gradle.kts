rootProject.name = "minecraft-npk"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
  repositories {
    gradlePluginPortal()

    mavenCentral()

    maven {
      name = "Jitpack"
      url = uri("https://jitpack.io")
    }

    maven {
      name = "Spigot"
      url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

      content {
        includeGroup("org.bukkit")
        includeGroup("org.spigotmc")
      }
    }
  }
}

pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenCentral()
  }
}

include("common")

pluginManagement {
    repositories {
        google() // Репозиторий Google
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SmartApiary"
include(":app")

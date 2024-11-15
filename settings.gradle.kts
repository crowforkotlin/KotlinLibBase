pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://maven.aliyun.com/nexus/content/groups/public/") }
        maven { setUrl("https://jitpack.io") }
    }
}


dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    versionCatalogs {
        create("compose") { from(files("gradle/compose.versions.toml")) }
        create("app") { from(files("gradle/app.versions.toml") )}
    }

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { setUrl("https://plugins.gradle.org/m2/") }
        maven { setUrl("https://maven.aliyun.com/nexus/content/groups/public/") }
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "KotlinLibBase"
include(":app")
include(":lib_base")
include(":lib_base_compose")

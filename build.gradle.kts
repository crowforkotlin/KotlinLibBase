buildscript {

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
        google()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = Versions.kotlin_version))
        classpath("com.android.tools.build:gradle:8.3.0-alpha04")
    }
}
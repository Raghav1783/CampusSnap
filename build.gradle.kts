buildscript {
    dependencies {
        classpath(libs.google.services)
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false

    id("com.android.library") version "8.2.0" apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false
}
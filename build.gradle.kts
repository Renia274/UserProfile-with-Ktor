// Top-level build file where you can add configuration options common to all sub-projects/modules.


buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}


plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    id("com.android.library") version "8.1.2" apply false
    id("com.google.protobuf") version "0.9.4" apply false

}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}



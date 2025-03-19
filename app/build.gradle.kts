plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")


}


android {
    namespace = "com.example.practice"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.practice"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/AL2.0"
            excludes += "/META-INF/LGPL2.1"
            excludes+= "/META-INF/INDEX.LIST"

        }
    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.5")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.androidbrowserhelper:androidbrowserhelper:2.5.0")


    //navigation with drawer
    implementation("androidx.navigation:navigation-compose:2.4.0")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.compose.material3:material3:1.1.0")
    implementation("androidx.compose.material3:material3:1.1.2")
    //bottom navigation and bottom navigation item
    implementation("androidx.compose.material:material:1.5.4")

    //animation
    implementation("androidx.compose.animation:animation:1.5.4")

    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0-rc01")
    implementation("androidx.activity:activity-ktx:1.8.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.fragment:fragment-ktx:1.6.2")

    //livedata
    implementation("androidx.compose.runtime:runtime-livedata:1.6.0-beta01")




//KTOR
    implementation ("io.ktor:ktor-client-logging:1.6.3")
    implementation ("ch.qos.logback:logback-classic:1.2.3")
    implementation ("io.ktor:ktor-client-core:1.6.4")
    implementation ("io.ktor:ktor-client-android:1.6.4")
    implementation ("io.ktor:ktor-client-json:1.6.4")
    implementation( "io.ktor:ktor-client-serialization:1.6.4")

    //html builder
    implementation("io.ktor:ktor-html-builder:1.6.5")





    //HILT DI
    implementation("com.google.dagger:hilt-android:2.48.1")
    testImplementation("org.testng:testng:6.9.6")
    ksp("com.google.dagger:hilt-compiler:2.48.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")


    //Google services
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))

    //Firebase and Google Services
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth:22.3.0")


    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor( "androidx.room:room-compiler:2.6.1")
    ksp( "androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")



    //Test and Debug
    // Mockito for local unit tests
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito:mockito-inline:5.4.0")
    testImplementation("org.mockito:mockito-kotlin:4.0.0")

    // Mockito for Android instrumentation tests
    androidTestImplementation("org.mockito:mockito-core:5.4.0")
    androidTestImplementation("org.mockito:mockito-inline:5.4.0")
    androidTestImplementation("org.mockito:mockito-android:5.4.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")

    // Espresso for UI tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.0")

    testImplementation ("androidx.arch.core:core-testing:2.2.0")


    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Testing for urls
    androidTestImplementation("org.robolectric:robolectric:4.10.2")






}


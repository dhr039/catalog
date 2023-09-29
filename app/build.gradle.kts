import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.cannonades.petconnect"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.cannonades.petconnect"
        // 29 - to avoid requesting WRITE_EXTERNAL_STORAGE, modern Android version use Scoped Storage:
        minSdk = 29
        //noinspection EditedTargetSdkVersion
        targetSdk = 34

        versionCode = 63
        versionName = "3.2"

        testInstrumentationRunner = "com.cannonades.petconnect.common.CustomTestRunner"
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
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildFeatures {
        buildConfig = true
    }

    sourceSets {
        getByName("androidTest") {
            assets.srcDirs("src/debug/assets")
        }
    }

    // Load local.properties file
    val props = Properties()
    val propFile = rootProject.file("local.properties")
    if (propFile.exists()) {
        FileInputStream(propFile).use { props.load(it) }

        props.getProperty("API_KEY")?.let { apiKey ->
            defaultConfig {
                buildConfigField("String", "API_KEY", "\"$apiKey\"")
            }
        }
    }
}

dependencies {
    val composeBom = "2023.08.00"
    val hiltVersion = "2.47"
    val navVersion = "2.7.3"
    val retrofitVersion = "2.9.0"
    val okhttpVersion = "4.11.0"
    val moshiVersion = "1.15.0"
    val mockwebserverVersion = "4.11.0"
    val roomVersion = "2.5.2"
    val googleTruthVersion = "1.1.5"

    implementation("com.android.billingclient:billing-ktx:6.0.1")

    //needed for 'colorControlNormal' in the xml vector drawables:
    implementation("androidx.appcompat:appcompat:1.6.1")

    // DI
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Compose
    implementation(platform("androidx.compose:compose-bom:$composeBom"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.navigation:navigation-compose:$navVersion")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBom"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.lifecycle:lifecycle-runtime-compose")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("io.coil-kt:coil-compose:2.4.0")
    androidTestImplementation("androidx.navigation:navigation-testing:$navVersion")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBom"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // network
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofitVersion")
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
    testImplementation("com.squareup.okhttp3:mockwebserver:$mockwebserverVersion")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:$mockwebserverVersion")

    // persistence
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:$composeBom"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("com.google.truth:truth:$googleTruthVersion")
    androidTestImplementation("com.google.truth:truth:$googleTruthVersion")
    testImplementation("org.mockito:mockito-core:5.4.0")
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
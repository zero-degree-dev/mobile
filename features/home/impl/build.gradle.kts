plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.zero_degree.features.home.impl"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // API dependency
    implementation(project(":features:home:api"))
    
    // Feature API dependencies
    implementation(project(":features:bars:api"))
    implementation(project(":features:bars:impl"))
    implementation(project(":features:drinks:api"))
    implementation(project(":features:drinks:impl"))
    implementation(project(":features:events:api"))
    implementation(project(":features:events:impl"))
    implementation(project(":features:bookings:api"))
    implementation(project(":features:bookings:impl"))
    
    // Core dependencies
    implementation(project(":core:api"))
    implementation(project(":core:storage"))
    implementation(project(":core-ui"))
    
    // Retrofit (needed for Response type)
    implementation(libs.retrofit)
    
    // Coil для загрузки изображений
    implementation(libs.coil)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    
    // Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    
    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.zero_degree"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.zero_degree"
        minSdk = 35
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    
    // Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)
    
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.gson)
    
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    
    implementation(libs.coil)
    
    // Yandex Maps
    implementation("com.yandex.android:maps.mobile:4.4.0-lite")
    
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.viewpager2)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":lib:injector"))
    
    // Libs modules
    implementation(project(":libs:ImageLoader:api"))
    implementation(project(":libs:ImageLoader:impl"))
    implementation(project(":libs:User:api"))
    implementation(project(":libs:User:impl"))
    
    // Core modules
    implementation(project(":core:api"))
    implementation(project(":core:storage"))
    implementation(project(":core-ui"))
    
    // Feature modules - impl only (api is transitive dependency)
    implementation(project(":features:auth:impl"))
    implementation(project(":features:home:impl"))
    implementation(project(":features:bars:impl"))
    implementation(project(":features:drinks:impl"))
    implementation(project(":features:events:impl"))
    implementation(project(":features:bookings:impl"))
    implementation(project(":features:profile:impl"))
}
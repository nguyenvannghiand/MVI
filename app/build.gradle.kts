plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") // Kích hoạt KSP
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.google.dagger:hilt-android:2.51.1")

    // 2. Hilt Compiler (Dùng KSP thay vì kapt)
    ksp("com.google.dagger:hilt-compiler:2.51.1")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Converter cho Kotlinx Serialization (Lưu ý: version của converter nên khớp với version retrofit)
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")

    // Thư viện Serialization cơ bản
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.1")
// Test Coroutines
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.1")

    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
// flowWithLifecycle & repeatOnLifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycle_version")

    // Coil (Dùng Coroutines ngầm, cực nhẹ)
    implementation("io.coil-kt:coil:2.6.0")

// LeakCanary (Kiểm tra Memory Leak - Chỉ dùng cho debug)
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.14")

	// need View binding
	implementation("com.github.kirich1409:viewbindingpropertydelegate-noreflection:1.5.9")
}
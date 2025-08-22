plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

// Parse .env file at the top-level (outside android block)
val envFile = rootProject.file(".env")
val envMap = mutableMapOf<String, String>()
if (envFile.exists()) {
    envFile.readLines().forEach { line ->
        val trimmed = line.trim()
        if (trimmed.isNotEmpty() && !trimmed.startsWith("#") && trimmed.contains("=")) {
            val (key, value) = trimmed.split("=", limit = 2)
            envMap[key.trim()] = value.trim()
        }
    }
}

android {
    namespace = "com.viettel.tvbox"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.viettel.tvbox"
        minSdk = 23
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        val apiUrl = envMap["API_BASE_URL"] ?: ""
        val imageUrl = envMap["IMAGE_URL"] ?: ""
        val videoUrl = envMap["VIDEO_URL"] ?: ""
        val blacknutUrl = envMap["BLACKNUT_URL"] ?: ""
        val blacknutImageUrl = envMap["BLACKNUT_IMAGE_URL"] ?: ""
        buildConfigField("String", "API_BASE_URL", "\"$apiUrl\"")
        buildConfigField("String", "IMAGE_URL", "\"$imageUrl\"")
        buildConfigField("String", "VIDEO_URL", "\"$videoUrl\"")
        buildConfigField("String", "BLACKNUT_URL", "\"$blacknutUrl\"")
        buildConfigField("String", "BLACKNUT_IMAGE_URL", "\"$blacknutImageUrl\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            pickFirsts += "META-INF/INDEX.LIST"
            pickFirsts += "META-INF/DEPENDENCIES"
            pickFirsts += "META-INF/io.netty.versions.properties"
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.tv.foundation)
    implementation(libs.androidx.tv.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.cardview)
    implementation(libs.retrofit)
    implementation(libs.refconverter)
    implementation(libs.androidx.leanback)
    implementation(libs.gson)
    implementation(libs.glide)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.runtime.saveable)
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.androidx.foundation)
    implementation(libs.coil.compose)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.accompanist.webview)
    implementation(libs.jsoup)
    implementation(libs.ui)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
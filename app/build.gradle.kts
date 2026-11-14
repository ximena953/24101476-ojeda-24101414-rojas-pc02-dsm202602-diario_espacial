import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

// La clave de la NASA se lee de local.properties, que no se sube al repositorio.
val propiedadesLocales = Properties().apply {
    val archivo = rootProject.file("local.properties")
    if (archivo.exists()) {
        archivo.inputStream().use { load(it) }
    }
}
val nasaApiKey: String = propiedadesLocales.getProperty("NASA_API_KEY", "")

android {
    namespace = "com.example.diarioespacial"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.diarioespacial"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        // Queda disponible en el código como BuildConfig.NASA_API_KEY
        buildConfigField("String", "NASA_API_KEY", "\"$nasaApiKey\"")
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ViewModel y lectura de StateFlow desde Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Navigation Compose: barra inferior y navegación interna
    implementation(libs.androidx.navigation.compose)

    // Retrofit + Gson para consumir la API de la NASA
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // Room (SQLite): el compilador genera el código de los DAO con KSP
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Coil para cargar imágenes desde una URL
    implementation(libs.coil.compose)
}

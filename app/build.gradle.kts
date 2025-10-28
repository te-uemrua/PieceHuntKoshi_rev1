plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.piecehuntkoshi_ver1"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.piecehuntkoshi_ver1"
        minSdk = 26
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material) // ← Materialライブラリを信頼する
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // RecyclerViewとCardView
    implementation(libs.recyclerview)
    // implementation("androidx.cardview:cardview:1.0.0") // ← 古い指定方法は削除

    // Roomデータベース
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    // Googleマップと位置情報サービス
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // テストライブラリ
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "mad.training.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 33

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
    testOptions {
        unitTests {
            isIncludeAndroidResources = true // Опционально, если тестам нужны Android ресурсы
            all { test -> // Обходим каждый тест
                test.useJUnitPlatform() // Применяем JUnit Platform к каждому
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // --- Основные зависимости (твои текущие) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat) // Эта и material обычно не нужны в чисто network модуле
    implementation(libs.material)           // Возможно, их стоит удалить из :network, если он не содержит UI

    // RxJava
    implementation("io.reactivex.rxjava3:rxjava:3.1.8") // Обновил до последней стабильной 3.1.x
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Эта версия ок
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava3:2.9.0")

    // OkHttp (рекомендую использовать BOM для согласования версий OkHttp)
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0")) // Обновил до последней стабильной
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // Koin (Привожу версии для Koin 3.5.x, так как 4.x еще не стабильна)
    // Если ты используешь Koin 4.x, найди актуальные тестовые артефакты для него.
    // Для Koin 3.5.3:
    implementation("io.insert-koin:koin-core:3.5.3")
    implementation("io.insert-koin:koin-android:3.5.3") // Если нужен androidContext в network модуле для Koin

    // --- Зависимости для Юнит-тестов (testImplementation) ---
    testImplementation(libs.junit) // JUnit 4, если используешь его. Для JUnit 5 ниже.
    // JUnit 5 (Jupiter) - Рекомендуется
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.2") // Для параметризованных тестов

    // MockK (для мокирования в Kotlin) - Рекомендуется вместо Mockito для Kotlin
    testImplementation("io.mockk:mockk:1.13.10")
    // Если нужны Android-специфичные моки с MockK (например, Context), но для network модуля это редкость
    // testImplementation "io.mockk:mockk-android:1.13.10"

    // Truth (для более читаемых ассертов, альтернатива AssertJ) - Опционально
    testImplementation("com.google.truth:truth:1.4.0")

    // OkHttp MockWebServer (для интеграционных тестов HTTP клиента)
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0") // Версия должна совпадать с OkHttp

    // Koin Test (для Koin 3.5.x)
    testImplementation("io.insert-koin:koin-test:3.5.3")
    testImplementation("io.insert-koin:koin-test-junit5:3.5.3") // Для JUnit 5
    // testImplementation "io.insert-koin:koin-test-junit4:3.5.3" // Для JUnit 4

    // Turbine (для тестирования Kotlin Flows, если бы они использовались вместо RxJava)
    // testImplementation "app.cash.turbine:turbine:1.1.0"

    // Coroutines Test (если бы использовались корутины вместо RxJava)
    // testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"


    // --- Зависимости для Android Инструментальных Тестов (androidTestImplementation) ---
    // Эти тесты запускаются на устройстве/эмуляторе. Для чисто network модуля они могут быть не так важны,
    // как юнит-тесты, но могут пригодиться для тестов с реальным Context.
    androidTestImplementation(libs.androidx.junit) // androidx.test.ext:junit
    androidTestImplementation(libs.androidx.espresso.core) // androidx.test.espresso:espresso-core
    testImplementation("io.insert-koin:koin-test:3.5.3")
    testImplementation("io.insert-koin:koin-test-junit5:3.5.3")
    // MockK для Android инструментальных тестов
    androidTestImplementation("io.mockk:mockk-android:1.13.10")

    // Koin Test для Android инструментальных тестов (для Koin 3.5.x)
    // androidTestImplementation "io.insert-koin:koin-android-test:3.5.3" // Редко нужно в data/network модуле
}
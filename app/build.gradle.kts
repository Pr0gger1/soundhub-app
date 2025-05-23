import java.util.Properties

plugins {
	kotlin("kapt")
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("com.google.devtools.ksp")
	id("kotlinx-serialization")
	id("dagger.hilt.android.plugin")
	id("com.google.dagger.hilt.android")
	id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
	id("kotlin-kapt")
}

android {
	namespace = "com.soundhub"
	compileSdk = 35

	buildFeatures {
		buildConfig = true
		compose = true
		viewBinding = true
	}

	buildTypes {
		release {
			isMinifyEnabled = true
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
		debug {
			isMinifyEnabled = false
		}
	}

	hilt {
		enableAggregatingTask = true
	}

	defaultConfig {
		applicationId = "com.soundhub"
		minSdk = 26
		targetSdk = 35
		versionCode = 122
		versionName = "1.2.2"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}

		val properties = Properties()
		properties.load(project.rootProject.file("local.properties").inputStream())

		// Set API keys in BuildConfig
		buildConfigField("String", "DISCOGS_KEY", properties.getProperty("DISCOGS_KEY"))
		buildConfigField("String", "DISCOGS_SECRET", properties.getProperty("DISCOGS_SECRET"))
		buildConfigField("String", "LAST_FM_API_KEY", properties.getProperty("LAST_FM_API_KEY"))
		buildConfigField(
			"String",
			"LAST_FM_SHARED_SECRET",
			properties.getProperty("LAST_FM_SHARED_SECRET")
		)
		buildConfigField(
			"String",
			"SOUNDHUB_API_HOSTNAME",
			properties.getProperty("SOUNDHUB_API_HOSTNAME")
		)
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.14"
	}

	packaging {
		resources {
			excludes += listOf(
				"/META-INF/{AL2.0,LGPL2.1}",
				"META-INF/LICENSE.md",
				"META-INF/LICENSE",
				"META-INF/LICENSE.txt",
				"META-INF/NOTICE",
				"META-INF/NOTICE.txt",
				"META-INF/ASL2.0",
				"META-INF/LICENSE-notice.md"
			)
		}
	}
}

dependencies {
	// Android Jetpack
	implementation(libs.androidCoreKtx)
	implementation(libs.lifecycleRuntimeKtx)
	implementation(libs.activityCompose)
	implementation(platform(libs.composeBom))
	implementation(libs.composeUi)
	implementation(libs.composeUiGraphics)
	implementation(libs.composeUiToolingPreview)
	implementation(libs.material3)
	implementation(libs.appcompat)
	implementation(libs.material)
	implementation(libs.annotation)
	implementation(libs.constraintlayout)
	implementation(libs.lifecycleLivedataKtx)
	implementation(libs.lifecycleViewmodelKtx)
	implementation(libs.navigationCompose)
	implementation(libs.navigationFragmentKtx)
	implementation(libs.navigationUiKtx)
	implementation(libs.hiltNavigationCompose)

	// Kotlinx Serialization
	implementation(libs.serializationJson)
	implementation(libs.activityKtx)

	// Dagger - Hilt for Robolectric tests
	testImplementation(libs.hiltAndroidTesting)
	kaptTest(libs.hiltAndroidCompiler)
	testAnnotationProcessor(libs.hiltAndroidCompiler)

	// Dagger - Hilt for instrumented tests
	androidTestImplementation(libs.hiltAndroidTesting)
	kaptAndroidTest(libs.hiltAndroidCompiler)
	androidTestAnnotationProcessor(libs.hiltAndroidCompiler)

	// Testing
	testImplementation(libs.junit)
	androidTestImplementation(libs.composeUiTestJunit4)
	androidTestImplementation(libs.testExtJunit)
	androidTestImplementation(libs.espressoCore)
	androidTestImplementation(libs.composeUiTestJunit4)
	androidTestImplementation(libs.mockk.android)
	debugImplementation(libs.composeUiTooling)
	debugImplementation(libs.composeUiTestManifest)
	testImplementation(libs.coroutinesTest)
	testImplementation(libs.mockk)
	testImplementation(libs.robolectric)


	// Kotlin Coroutines
	implementation(libs.coroutinesCore)

	// Dagger - Hilt
	implementation(libs.hiltAndroid)
	ksp(libs.hiltCompiler)

	// Glide
	implementation(libs.glideCompose)
	implementation(libs.glide)
	ksp(libs.glideKsp)

	// Datastore Preferences
	implementation(libs.datastorePreferences)
	implementation(libs.lifecycleRuntimeCompose)
	implementation(libs.datastore)
	implementation(libs.protobufJavalite)

	// Retrofit
	implementation(libs.retrofit)
	implementation(libs.converterGson)
	implementation(libs.okhttp)
	implementation(libs.loggingInterceptor)

	// Splash Screen
	implementation(libs.coreSplashscreen)

	// MapStruct
	implementation(libs.mapstruct)
	kapt(libs.mapstructProcessor)

	// Coil
	implementation(libs.coil.compose)
	implementation(libs.coil.network.okhttp)

	// Room
	implementation(libs.roomRuntime)
	ksp(libs.roomCompiler)
	implementation(libs.roomKtx)

	// Stomp WebSocket Client
	implementation(libs.stompProtocolAndroid)
	implementation(libs.rxandroid)

	implementation(libs.paging.compose)

	implementation(libs.kotlinx.coroutines.android)
}

kapt {
	correctErrorTypes = true
}

tasks.withType<Test> {
	jvmArgs("--add-opens", "java.base/java.time=ALL-UNNAMED")
}
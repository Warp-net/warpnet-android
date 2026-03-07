import java.util.Properties

// Define hasSigningProps as false (signing config not configured)
// This is set to false as per issue requirements - signing configuration is not set up
val hasSigningProps = false

// Register vendorDependencies task using shared utility function
registerVendorDependenciesTask("android")
buildscript {
    repositories {
        google()
    }

    dependencies {
    }
}

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("warpnet-android.android.application")
    alias(libs.plugins.kotlin.compose)
}


android {
    lint {
        disable.add("MissingTranslation")
    }
    flavorDimensions.add("channel")

    buildTypes {
        debug {
            if (hasSigningProps) {
                signingConfig = signingConfigs.getByName("warpnet")
            }
            manifestPlaceholders.apply {
                put("appIcon", "@mipmap/ic_launcher")
                put("appIconRound", "@mipmap/ic_launcher_round")
            }
        }
        release {
            if (hasSigningProps) {
                signingConfig = signingConfigs.getByName("warpnet")
            }
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            manifestPlaceholders.apply {
                put("appIcon", "@mipmap/ic_launcher")
                put("appIconRound", "@mipmap/ic_launcher_round")
            }
        }
    }
    sourceSets.forEach {
        it.res {
            srcDirs(project.files("src/${it.name}/res-localized"))
        }
        it.java {
            srcDirs("src/${it.name}/kotlin")
        }
    }
    sourceSets {
        findByName("androidTest")?.let {
            it.assets {
                srcDirs(files("$projectDir/schemas"))
            }
        }
    }
    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "META-INF/AL2.0",
                    "META-INF/LGPL2.1",
                    "DebugProbesKt.bin"
                )
            )
        }
    }
}

dependencies {
    implementation(projects.common)
    implementation(libs.androidx.startup)
}

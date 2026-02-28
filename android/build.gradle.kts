import java.util.Properties

dependencies {
    // Use the simplest possible file reference to avoid the 'module' error
    implementation(fileTree("libs") { include("*.jar") })
}

// Wrap task registration in afterEvaluate to ensure configurations are available
afterEvaluate {
    tasks.register<Copy>("vendorDependencies") {
        // For Android projects, use the release runtime configuration
        val runtimeDeps = configurations.findByName("releaseRuntimeClasspath") 
            ?: configurations.findByName("debugRuntimeClasspath")
        
        if (runtimeDeps != null) {
            from(runtimeDeps)
            into(rootProject.layout.projectDirectory.dir("repo"))
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        } else {
            logger.warn("No runtime configuration found for vendoring in android module")
        }
    }
}
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
    packagingOptions {
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

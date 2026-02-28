import java.util.Properties

dependencies {
    // Use the simplest possible file reference to avoid the 'module' error
    implementation(fileTree("libs") { include("*.jar") })
}
tasks.register<Copy>("vendorDependencies") {
    // Explicitly grab the files from the runtime configuration
    val runtimeDeps = configurations.named("runtimeClasspath").get()

    from(runtimeDeps)
    into(layout.projectDirectory.dir("vendor/libs"))

    // Optional: filter out directories, just get the JARs
    eachFile {
        if (this.relativePath.getFile(destinationDir).exists()) {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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

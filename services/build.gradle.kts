@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("warpnet-android.project.kmp")
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

group = Package.group
version = Package.versionName

dependencies {
    // Use the simplest possible file reference to avoid the 'module' error
    implementation(fileTree("libs") { include("*.jar") })
}

// Wrap task registration in afterEvaluate to ensure configurations are available
afterEvaluate {
    tasks.register<Copy>("vendorDependencies") {
        // For KMP projects, we target the Android runtime configuration
        val runtimeDeps = configurations.findByName("releaseRuntimeClasspath") 
            ?: configurations.findByName("debugRuntimeClasspath")
            ?: configurations.findByName("jvmRuntimeClasspath")
        
        if (runtimeDeps != null) {
            from(runtimeDeps)
            into(rootProject.layout.projectDirectory.dir("repo"))
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        } else {
            logger.warn("No runtime configuration found for vendoring in services module")
        }
    }
}
kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.bundles.kotlinx)
                implementation(libs.bundles.reftrofit2)
                implementation(libs.bundles.ktor)
                implementation(libs.square.okhttp)
                implementation("com.github.Tlaster:Hson:0.1.4")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

android {
    namespace = "${Package.id}.services"
}

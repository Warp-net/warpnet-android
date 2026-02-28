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

// Register vendorDependencies task using shared utility function
registerVendorDependenciesTask("services")
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

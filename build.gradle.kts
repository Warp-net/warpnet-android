@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("warpnet-android.versionsCheck")
}

// Root-level task to vendor dependencies from all modules
tasks.register("vendorAllDependencies") {
    description = "Vendor dependencies from all modules to the repo directory"
    group = "build setup"
    
    dependsOn(
        ":android:vendorDependencies",
        ":common:vendorDependencies",
        ":services:vendorDependencies"
    )
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = Versions.Java.jvmTarget

            allWarningsAsErrors = true
            freeCompilerArgs = freeCompilerArgs + listOf(
                "-Xcontext-receivers",
                "-Xskip-prerelease-check"
            )
        }
    }
    configurations.all {
        resolutionStrategy {
            force("org.objenesis:objenesis:3.2")
        }
    }
}

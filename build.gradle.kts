@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("warpnet-android.versionsCheck")
}

// Root-level task to vendor dependencies from all modules
// Dynamically discovers all subprojects that have a vendorDependencies task
tasks.register("vendorAllDependencies") {
    description = "Vendor dependencies from all modules to the repo directory"
    group = "build setup"
    
    // Wait for all subprojects to be evaluated before configuring dependencies
    gradle.projectsEvaluated {
        val vendorTasks = subprojects
            .mapNotNull { subproject ->
                subproject.tasks.findByName("vendorDependencies")
            }
            .map { it.path }
        
        if (vendorTasks.isNotEmpty()) {
            dependsOn(vendorTasks)
        } else {
            logger.warn("No vendorDependencies tasks found in any subprojects")
        }
    }
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

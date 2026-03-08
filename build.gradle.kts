@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("warpnet-android.versionsCheck")
}

// Root-level task to vendor dependencies from all modules
// Dynamically discovers all subprojects that have a vendorDependencies task
afterEvaluate {
    tasks.register("vendorAllDependencies") {
        description = "Vendor dependencies from all modules to the repo directory"
        group = "build setup"
        
        val vendorTasks = subprojects
            .mapNotNull { subproject ->
                subproject.tasks.findByName("vendorDependencies")
            }
        
        if (vendorTasks.isNotEmpty()) {
            dependsOn(vendorTasks)
        } else {
            logger.warn("No vendorDependencies tasks found in any subprojects")
        }
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(Versions.Java.jvmTarget))

//            allWarningsAsErrors.set(true)
            freeCompilerArgs.addAll(
                "-Xcontext-parameters",
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

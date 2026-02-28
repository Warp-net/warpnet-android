package warpnetandroid

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Copy

/**
 * Finds the appropriate runtime configuration for vendoring dependencies.
 * 
 * This function tries to find runtime configurations in the following order:
 * 1. releaseRuntimeClasspath (preferred for production vendoring)
 * 2. debugRuntimeClasspath (fallback for debug builds)
 * 3. jvmRuntimeClasspath (fallback for JVM-specific KMP targets)
 * 
 * The release configuration is preferred because it typically contains the
 * production dependencies without debug-only libraries.
 * 
 * @return The runtime configuration if found, null otherwise
 */
fun Project.findRuntimeConfigurationForVendoring(): Configuration? {
    return configurations.findByName("releaseRuntimeClasspath")
        ?: configurations.findByName("debugRuntimeClasspath")
        ?: configurations.findByName("jvmRuntimeClasspath")
}

/**
 * Registers a vendorDependencies task that copies all runtime dependencies
 * to the project's root repo directory.
 * 
 * This task must be called within an afterEvaluate block to ensure configurations
 * are available after all plugins have been applied.
 * 
 * @param moduleName The name of the module for logging purposes
 */
fun Project.registerVendorDependenciesTask(moduleName: String) {
    afterEvaluate {
        tasks.register<Copy>("vendorDependencies") {
            val runtimeDeps = findRuntimeConfigurationForVendoring()
            
            if (runtimeDeps != null) {
                from(runtimeDeps)
                into(rootProject.layout.projectDirectory.dir("repo"))
                duplicatesStrategy = org.gradle.api.file.DuplicatesStrategy.EXCLUDE
            } else {
                logger.warn("No runtime configuration found for vendoring in $moduleName module")
            }
        }
    }
}

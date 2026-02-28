import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Copy
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

fun KotlinJvmTarget.setupJvm() {
  compilations.all {
    kotlinOptions.jvmTarget = Versions.Java.jvmTarget
  }
}

fun DependencyHandlerScope.kspAll(dependencyNotation: Any) {
  add("kspCommonMainMetadata", dependencyNotation)
  add("kspAndroid", dependencyNotation)
  add("kspDesktop", dependencyNotation)
  // add("kspIosArm64", dependencyNotation)
  // add("kspIosX64", dependencyNotation)
  // add("kspMacosArm64", dependencyNotation)
  // add("kspMacosX64", dependencyNotation)
}

fun DependencyHandlerScope.kspAndroid(dependencyNotation: Any) {
  add("kspAndroid", dependencyNotation)
}

/**
 * Finds the appropriate runtime configuration for vendoring dependencies.
 */
fun Project.findRuntimeConfigurationForVendoring(): Configuration? {
    return configurations.findByName("releaseRuntimeClasspath")
        ?: configurations.findByName("debugRuntimeClasspath")
        ?: configurations.findByName("jvmRuntimeClasspath")
}

/**
 * Registers a vendorDependencies task that copies all runtime dependencies
 * to the project's root repo directory.
 */
fun Project.registerVendorDependenciesTask(moduleName: String) {
    afterEvaluate {
        tasks.register("vendorDependencies", Copy::class.java) {
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

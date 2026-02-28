package warpnet-android.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import warpnet-android.compose
import warpnet-android.kotlin

class ProjectKmpComposePlugin : Plugin<Project> {
  @Suppress("UNUSED_VARIABLE")
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("org.jetbrains.compose")
        apply("warpnet-android.project.kmp")
        apply("warpnet-android.detekt")
      }
      kotlin {
        sourceSets.apply {
          val commonMain = getByName("commonMain") {
            dependencies {
              api(compose.ui)
              api(compose.runtime)
              api(compose.foundation)
              api(compose.material)
              api(compose.materialIconsExtended)
            }
          }
          val androidMain = getByName("androidMain") {
            dependencies {
            }
          }
        }
      }
    }
  }
}

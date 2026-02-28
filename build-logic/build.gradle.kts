plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
}

dependencies {
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.compose)

    implementation(libs.gradlePlugin.spotless)
    implementation(libs.gradlePlugin.detekt)
    implementation(libs.gradlePlugin.versionsCheck)
}

gradlePlugin {
    plugins {
        register("kmp") {
            id = "warpnet-android.kmp"
            implementationClass = "warpnet-android.plugin.KmpPlugin"
        }
        register("kmp.compose") {
            id = "warpnet-android.kmp.compose"
            implementationClass = "warpnet-android.plugin.KmpComposePlugin"
        }
        register("android") {
            id = "warpnet-android.android"
            implementationClass = "warpnet-android.plugin.AndroidLibraryPlugin"
        }
        register("android.application") {
            id = "warpnet-android.android.application"
            implementationClass = "warpnet-android.plugin.AndroidApplicationPlugin"
        }
        register("project.kmp") {
            id = "warpnet-android.project.kmp"
            implementationClass = "warpnet-android.plugin.ProjectKmpPlugin"
        }
        register("project.kmp.compose") {
            id = "warpnet-android.project.kmp.compose"
            implementationClass = "warpnet-android.plugin.ProjectKmpComposePlugin"
        }
        //
        // // Tool
        //
        register("spotless") {
            id = "warpnet-android.spotless"
            implementationClass = "warpnet-android.tool.SpotlessPlugin"
        }
        register("detekt") {
            id = "warpnet-android.detekt"
            implementationClass = "warpnet-android.tool.DetektPlugin"
        }
        register("versionsCheck") {
            id = "warpnet-android.versionsCheck"
            implementationClass = "warpnet-android.tool.VersionsCheckPlugin"
        }
        register("composeMetrics") {
            id = "warpnet-android.composeMetrics"
            implementationClass = "warpnet-android.tool.ComposeMetricsPlugin"
        }
        // register("composeMendable") {
        //     id = "warpnet-android.mendableBuild"
        //     implementationClass = "warpnet-android.tool.MendableBuildPlugin"
        // }
    }
}

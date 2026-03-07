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

    implementation(libs.gradlePlugin.versionsCheck)
    
    // JSON library for build scripts (removed from Gradle API in 9.0)
    implementation("org.json:json:20240303")
}

gradlePlugin {
    plugins {
        register("kmp") {
            id = "warpnet-android.kmp"
            implementationClass = "warpnetandroid.plugin.KmpPlugin"
        }
        register("kmp.compose") {
            id = "warpnet-android.kmp.compose"
            implementationClass = "warpnetandroid.plugin.KmpComposePlugin"
        }
        register("android") {
            id = "warpnet-android.android"
            implementationClass = "warpnetandroid.plugin.AndroidLibraryPlugin"
        }
        register("android.application") {
            id = "warpnet-android.android.application"
            implementationClass = "warpnetandroid.plugin.AndroidApplicationPlugin"
        }
        register("project.kmp") {
            id = "warpnet-android.project.kmp"
            implementationClass = "warpnetandroid.plugin.ProjectKmpPlugin"
        }
        register("project.kmp.compose") {
            id = "warpnet-android.project.kmp.compose"
            implementationClass = "warpnetandroid.plugin.ProjectKmpComposePlugin"
        }
        //
        // // Tool
        //
        register("versionsCheck") {
            id = "warpnet-android.versionsCheck"
            implementationClass = "warpnetandroid.tool.VersionsCheckPlugin"
        }
        register("composeMetrics") {
            id = "warpnet-android.composeMetrics"
            implementationClass = "warpnetandroid.tool.ComposeMetricsPlugin"
        }
        // register("composeMendable") {
        //     id = "warpnet-android.mendableBuild"
        //     implementationClass = "warpnetandroid.tool.MendableBuildPlugin"
        // }
    }
}

import org.gradle.api.JavaVersion

object Versions {
    object Android {
      const val min = 31
      const val compile = 36
      const val target = compile
    }

    object Java {
        const val jvmTarget = "21"
        val java = JavaVersion.VERSION_21
    }
}

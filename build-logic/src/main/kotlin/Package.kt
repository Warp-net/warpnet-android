object Package {
    const val group = "com.warpnet"
    const val name = "Warpnet Android"
    const val id = "$group.warpnetandroid"
    val versionName =
        "${Version.main}.${Version.mirror}.${Version.patch}${if (Version.revision.isNotEmpty()) "-${Version.revision}" else ""}"
    const val copyright = "Copyright (C) WarpnetProject and Contributors"
    const val versionCode = Version.build

    object Version {
        const val main = "1"
        const val mirror = "7"
        const val patch = "0"
        const val revision = "beta02"
        const val build = 61
    }
}

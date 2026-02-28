/*
 *  Warpnet Android
 *
 *  Copyright (C) WarpnetProject and Contributors
 *
 *  This file is part of Warpnet Android.
 *
 *  Warpnet Android is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Warpnet Android is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Warpnet Android. If not, see <http://www.gnu.org/licenses/>.
 */
package com.warpnet.warpnetandroid.kmp

import java.io.File

// for media caches e.g image, video
val StorageProvider.cacheFiles get() = CacheFiles(cacheDir = cacheDir, mediaCacheDir = mediaCacheDir)
val StorageProvider.appFiles get() = AppFiles(appDir)

class CacheFiles(private val cacheDir: String, private val mediaCacheDir: String) {
  fun mediaFile(name: String) = "${mediaCacheDir.mkdirs()}/$name"

  val dataDir get() = "$cacheDir/data".mkdirs()
  fun dataFile(name: String) = "$dataDir/$name"

  val databaseDir get() = "$cacheDir/database".mkdirs()
  fun databaseFile(name: String) = "$databaseDir/$name"
}

class AppFiles(private val appDir: String) {
  val mediaDir get() = "$appDir/medias".mkdirs()
  fun mediaFile(name: String) = "$mediaDir/$name"

  val dataStoreDir get() = "$appDir/datastore".mkdirs()
  fun dataStoreFile(name: String) = "$dataStoreDir/$name"

  val databaseDir get() = "$appDir/database".mkdirs()
  fun databaseFile(name: String) = "$databaseDir/$name"
}

fun String.mkdirs(): String {
  File(this).apply {
    if (!exists()) mkdirs()
  }
  return this
}

fun String.file(createIfNotExists: Boolean = true): File {
  return File(this).apply {
    if (createIfNotExists && !exists()) createNewFile()
  }
}

fun String.mkFile(): String {
  file()
  return this
}

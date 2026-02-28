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
package com.warpnet.warpnet-android.kmp

expect class StorageProvider {
  companion object {
    fun create(): StorageProvider
  }

  // for persistence data
  val appDir: String

  // for cache data
  val cacheDir: String

  // for media caches e.g image, video
  val mediaCacheDir: String

  /**
   * @param dir, cache dir needs to be clear
   * @param deleteDirAlso, delete all files in dir include dir itself
   */
  fun clearCaches(dir: String, deleteDirAlso: Boolean = false)
}

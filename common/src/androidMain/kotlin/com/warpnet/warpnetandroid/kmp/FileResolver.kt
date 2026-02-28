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

import android.content.Context
import android.graphics.BitmapFactory
import com.warpnet.warpnetandroid.extensions.toUri
import java.io.InputStream
import java.io.OutputStream

actual class FileResolver(private val context: Context) {
  private val contentResolver = context.contentResolver
  actual fun getMimeType(file: String): String? {
    return contentResolver.getType(file.toUri(context))
  }

  actual fun getFileSize(file: String): Long? {
    return contentResolver.openFileDescriptor(file.toUri(context), "r")?.statSize
  }

  actual fun openInputStream(file: String): InputStream? {
    return contentResolver.openInputStream(file.toUri(context))
  }

  actual fun openOutputStream(file: String): OutputStream? {
    return contentResolver.openOutputStream(file.toUri(context))
  }

  actual fun getMediaSize(file: String): MediaSize {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(file.toUri(context).path, options)
    return MediaSize(
      width = options.outWidth.toLong(),
      height = options.outHeight.toLong()
    )
  }
}

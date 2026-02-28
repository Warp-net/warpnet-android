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
package com.warpnet.warpnet-android.extensions

import android.content.Context
import android.net.Uri
import com.warpnet.warpnet-android.model.enums.MediaType

fun Uri.mediaType(context: Context): MediaType {
  val mimeType = context.contentResolver.getType(this) ?: ""
  return when {
    mimeType.startsWith("video") -> MediaType.video
    mimeType == "image/gif" -> MediaType.animated_gif
    mimeType.startsWith("image") -> MediaType.photo
    mimeType.startsWith("audio") -> MediaType.audio
    else -> MediaType.other
  }
}

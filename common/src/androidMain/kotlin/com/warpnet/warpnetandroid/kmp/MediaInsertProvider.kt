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
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.warpnet.warpnetandroid.extensions.toUri
import com.warpnet.warpnetandroid.model.enums.MediaType
import com.warpnet.warpnetandroid.model.ui.UiMediaInsert
import kotlinx.coroutines.coroutineScope

actual class MediaInsertProvider(private val context: Context) {

  actual suspend fun provideUiMediaInsert(filePath: String): UiMediaInsert {
    val androidUri = filePath.toUri(context)
    val type = (context.contentResolver.getType(androidUri) ?: "image/*").let {
      when {
        it.startsWith("video") -> MediaType.video
        it == "image/gif" -> MediaType.animated_gif
        else -> MediaType.photo
      }
    }
    return UiMediaInsert(
      filePath = androidUri.toString(),
      preview = if (type == MediaType.video) getVideoThumbnail(androidUri) ?: androidUri.toString() else androidUri.toString(),
      type = type,
    )
  }

  private suspend fun getVideoThumbnail(uri: Uri): Bitmap? {
    return coroutineScope {
      var bitmap: Bitmap? = null
      var mediaMetadataRetriever: MediaMetadataRetriever? = null
      try {
        mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(context, uri)
        bitmap = mediaMetadataRetriever.getFrameAtTime(
          1000,
          MediaMetadataRetriever.OPTION_CLOSEST_SYNC
        )
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        mediaMetadataRetriever?.release()
      }
      bitmap
    }
  }
}

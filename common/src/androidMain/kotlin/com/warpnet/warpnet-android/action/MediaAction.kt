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
package com.warpnet.warpnet-android.action

import android.content.Context
import androidx.work.WorkManager
import com.warpnet.warpnet-android.extensions.toUri
import com.warpnet.warpnet-android.kmp.StorageProvider
import com.warpnet.warpnet-android.kmp.cacheFiles
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.worker.DownloadMediaWorker
import com.warpnet.warpnet-android.worker.ShareMediaWorker

actual class MediaAction(
  private val workManager: WorkManager,
  private val context: Context,
  private val storageProvider: StorageProvider
) {
  actual fun download(
    source: String,
    target: String,
    accountKey: MicroBlogKey
  ) {
    workManager.enqueue(
      DownloadMediaWorker.create(
        accountKey = accountKey,
        source = source,
        target = target
      )
    )
  }

  actual fun share(source: String, fileName: String, accountKey: MicroBlogKey, extraText: String) {
    val uri = storageProvider.cacheFiles.mediaFile(fileName).toUri(context)
    DownloadMediaWorker.create(
      accountKey = accountKey,
      source = source,
      target = uri.toString()
    ).let {
      workManager.beginWith(it)
        .then(
          ShareMediaWorker.create(
            target = uri,
            extraText = extraText,
          )
        ).enqueue()
    }
  }
}

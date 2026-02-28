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
package com.warpnet.warpnet-android.jobs.compose

import com.warpnet.services.warpnet.WarpnetService
import com.warpnet.warpnet-android.dataprovider.mapper.toUi
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.kmp.ExifScrambler
import com.warpnet.warpnet-android.kmp.FileResolver
import com.warpnet.warpnet-android.kmp.RemoteNavigator
import com.warpnet.warpnet-android.kmp.ResLoader
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.ComposeType
import com.warpnet.warpnet-android.model.job.ComposeData
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.notification.AppNotificationManager
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.repository.StatusRepository

class WarpnetComposeJob constructor(
  accountRepository: AccountRepository,
  notificationManager: AppNotificationManager,
  exifScrambler: ExifScrambler,
  remoteNavigator: RemoteNavigator,
  resLoader: ResLoader,
  private val statusRepository: StatusRepository,
  private val fileResolver: FileResolver,
  private val cacheDatabase: CacheDatabase,
) : ComposeJob<WarpnetService>(
  accountRepository,
  notificationManager,
  exifScrambler,
  remoteNavigator,
  resLoader,
) {
  override suspend fun compose(
    service: WarpnetService,
    composeData: ComposeData,
    accountKey: MicroBlogKey,
    mediaIds: ArrayList<String>
  ): UiStatus {
    val lat = composeData.lat
    val long = composeData.long
    val content = composeData.content.let {
      if (composeData.composeType == ComposeType.Quote && composeData.statusKey != null) {
        val status = statusRepository.loadFromCache(
          composeData.statusKey,
          accountKey = accountKey
        )
        it + " ${status?.generateShareLink()}"
      } else {
        it
      }
    }
    val result = service.update(
      content,
      media_ids = mediaIds,
      in_reply_to_status_id = if (composeData.composeType == ComposeType.Reply || composeData.composeType == ComposeType.Thread) composeData.statusKey?.id else null,
      repost_status_id = if (composeData.composeType == ComposeType.Quote) composeData.statusKey?.id else null,
      lat = lat,
      long = long,
      exclude_reply_user_ids = composeData.excludedReplyUserIds
    ).toUi(accountKey)
    cacheDatabase.statusDao().insertAll(listOf = listOf(result), accountKey = accountKey)
    return result
  }

  override suspend fun uploadImage(
    originUri: String,
    scramblerUri: String,
    service: WarpnetService
  ): String {
    val type = fileResolver.getMimeType(originUri)
    val size = fileResolver.getFileSize(scramblerUri)
    return fileResolver.openInputStream(scramblerUri)?.use {
      service.uploadFile(
        it,
        type ?: "image/*",
        size ?: it.available().toLong()
      )
    } ?: throw Error()
  }

  override val imageMaxSize: Long
    get() = 5 * 1024 * 1024 // https://help.warpnet.com/en/using-warpnet/tweeting-gifs-and-pictures
}

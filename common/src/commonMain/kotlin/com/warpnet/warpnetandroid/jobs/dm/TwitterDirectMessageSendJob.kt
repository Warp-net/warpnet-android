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
package com.warpnet.warpnetandroid.jobs.dm

import com.warpnet.services.microblog.LookupService
import com.warpnet.services.warpnet.WarpnetService
import com.warpnet.warpnetandroid.dataprovider.mapper.autolink
import com.warpnet.warpnetandroid.dataprovider.mapper.toUi
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.kmp.FileResolver
import com.warpnet.warpnetandroid.kmp.ResLoader
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.job.DirectMessageSendData
import com.warpnet.warpnetandroid.model.ui.UiDMEvent
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.notification.AppNotificationManager
import com.warpnet.warpnetandroid.repository.AccountRepository

class WarpnetDirectMessageSendJob(
  accountRepository: AccountRepository,
  notificationManager: AppNotificationManager,
  fileResolver: FileResolver,
  cacheDatabase: CacheDatabase,
  resLoader: ResLoader,
) : DirectMessageSendJob<WarpnetService>(
  cacheDatabase,
  accountRepository,
  notificationManager,
  fileResolver,
  resLoader,
) {

  override suspend fun sendMessage(
    service: WarpnetService,
    sendData: DirectMessageSendData,
    mediaIds: ArrayList<String>
  ): UiDMEvent = service.sendDirectMessage(
    recipientId = sendData.recipientUserKey.id,
    text = sendData.text,
    attachmentType = "media",
    mediaId = mediaIds.firstOrNull()
  )?.toUi(
    accountKey = sendData.accountKey,
    sender = lookUpUser(cacheDatabase, sendData.accountKey, service)
  ) ?: throw Error()

  private suspend fun lookUpUser(database: CacheDatabase, userKey: MicroBlogKey, service: WarpnetService): UiUser {
    return database.userDao().findWithUserKey(userKey) ?: let {
      val user = (service as LookupService).lookupUser(userKey.id)
        .toUi(userKey)
      database.userDao().insertAll(listOf(user))
      user
    }
  }

  override suspend fun uploadImage(
    originUri: String,
    scramblerUri: String,
    service: WarpnetService
  ): String? {
    val type = fileResolver.getMimeType(originUri)
    val size = fileResolver.getFileSize(originUri)
    return fileResolver.openInputStream(scramblerUri)?.use {
      service.uploadFile(
        it,
        type ?: "image/*",
        size ?: it.available().toLong()
      )
    } ?: throw Error()
  }

  override suspend fun autoLink(text: String): String {
    return autolink.autoLink(text)
  }
}

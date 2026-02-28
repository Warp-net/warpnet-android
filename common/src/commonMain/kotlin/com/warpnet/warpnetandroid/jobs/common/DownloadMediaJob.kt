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
package com.warpnet.warpnetandroid.jobs.common

import com.warpnet.services.microblog.DownloadMediaService
import com.warpnet.warpnetandroid.kmp.FileResolver
import com.warpnet.warpnetandroid.kmp.ResLoader
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.notification.StringNotificationEvent.Companion.show
import com.warpnet.warpnetandroid.repository.AccountRepository

class DownloadMediaJob(
  private val accountRepository: AccountRepository,
  private val inAppNotification: InAppNotification,
  private val fileResolver: FileResolver,
  private val resLoader: ResLoader,
) {
  suspend fun execute(
    target: String,
    source: String,
    accountKey: MicroBlogKey,
  ) {
    val accountDetails = accountKey.let {
      accountRepository.findByAccountKey(accountKey = it)
    } ?: throw Error("Can't find any account matches:$$accountKey")
    val service = accountDetails.service
    if (service !is DownloadMediaService) {
      throw Error("Service must be DownloadMediaService")
    }
    fileResolver.openOutputStream(target)?.use {
      service.download(target = source).copyTo(it)
    } ?: throw Error("Download failed")
    inAppNotification.show(resLoader.getString(com.warpnet.warpnetandroid.MR.strings.common_controls_actions_save))
  }
}

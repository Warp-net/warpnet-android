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
package com.warpnet.warpnet-android.jobs.status

import com.warpnet.services.microblog.StatusService
import com.warpnet.warpnet-android.dataprovider.mapper.toUi
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.job.StatusResult
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.notification.InAppNotification
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.repository.StatusRepository

class UnRetweetStatusJob(
  accountRepository: AccountRepository,
  statusRepository: StatusRepository,
  inAppNotification: InAppNotification,
) : StatusJob(
  accountRepository,
  statusRepository,
  inAppNotification
) {
  override suspend fun doWork(
    accountKey: MicroBlogKey,
    service: StatusService,
    status: UiStatus
  ): StatusResult {
    val newStatus = service.unRetweet(status.statusId)
      .toUi(accountKey = accountKey).let {
        it.retweet ?: it
      }
    return StatusResult(
      statusKey = newStatus.statusKey,
      accountKey = accountKey,
      retweeted = false,
      retweetCount = newStatus.metrics.retweet,
      likeCount = newStatus.metrics.like,
    )
  }
  override fun fallback(
    accountKey: MicroBlogKey,
    status: UiStatus,
  ) = StatusResult(
    accountKey = accountKey,
    statusKey = status.statusKey,
    retweeted = true,
  )
}

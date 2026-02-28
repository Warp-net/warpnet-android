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
package com.warpnet.warpnetandroid.jobs.status

import com.warpnet.services.microblog.StatusService
import com.warpnet.warpnetandroid.dataprovider.mapper.toUi
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.job.StatusResult
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.repository.StatusRepository

class RetweetStatusJob(
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
    val newStatus = service.retweet(status.statusId)
      .toUi(accountKey = accountKey).let {
        it.retweet ?: it
      }
    return StatusResult(
      statusKey = newStatus.statusKey,
      accountKey = accountKey,
      retweeted = true,
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
    retweeted = false,
  )
}

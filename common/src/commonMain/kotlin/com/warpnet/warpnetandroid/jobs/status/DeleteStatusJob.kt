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
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.repository.StatusRepository
import com.warpnet.warpnetandroid.utils.notifyError

class DeleteStatusJob(
  private val accountRepository: AccountRepository,
  private val statusRepository: StatusRepository,
  private val inAppNotification: InAppNotification
) {
  suspend fun execute(
    accountKey: MicroBlogKey,
    statusKey: MicroBlogKey
  ) {
    val status = statusKey.let {
      statusRepository.loadFromCache(it, accountKey = accountKey)
    } ?: throw Error("Can't find any status matches:$statusKey")
    val service = accountRepository.findByAccountKey(accountKey)?.let {
      it.service as? StatusService
    } ?: throw Error()
    try {
      service.delete(status.statusId)
    } catch (e: Throwable) {
      inAppNotification.notifyError(e)
      throw e
    }
  }
}

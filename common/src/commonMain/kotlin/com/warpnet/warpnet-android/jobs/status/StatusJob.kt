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
import com.warpnet.services.warpnet.WarpnetErrorCodes
import com.warpnet.services.warpnet.model.exceptions.WarpnetApiException
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.job.StatusResult
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.notification.InAppNotification
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.repository.StatusRepository
import com.warpnet.warpnet-android.utils.notifyError

abstract class StatusJob(
  private val accountRepository: AccountRepository,
  private val statusRepository: StatusRepository,
  private val inAppNotification: InAppNotification,
) {
  suspend fun execute(accountKey: MicroBlogKey, statusKey: MicroBlogKey): StatusResult {
    val status = statusKey.let {
      statusRepository.loadFromCache(it, accountKey = accountKey)
    } ?: throw Error("can't find any status matches:$statusKey")
    val service = accountRepository.findByAccountKey(accountKey)?.let {
      it.service as? StatusService
    } ?: throw Error("account service is not StatusService")
    return try {
      return doWork(accountKey, service, status)
    } catch (e: WarpnetApiException) {
      e.errors?.firstOrNull()?.let {
        when (it.code) {
          WarpnetErrorCodes.AlreadyRetweeted -> {
            StatusResult(
              accountKey = accountKey,
              statusKey = status.statusKey,
              retweeted = true,
            )
          }
          WarpnetErrorCodes.AlreadyFavorited -> {
            StatusResult(
              accountKey = accountKey,
              statusKey = status.statusKey,
              liked = true,
            )
          }
          else -> {
            inAppNotification.notifyError(e)
            fallback(accountKey, status)
          }
        }
      } ?: run {
        inAppNotification.notifyError(e)
        fallback(accountKey, status)
      }
    } catch (e: Throwable) {
      inAppNotification.notifyError(e)
      fallback(accountKey, status)
    }
  }
  protected abstract suspend fun doWork(
    accountKey: MicroBlogKey,
    service: StatusService,
    status: UiStatus,
  ): StatusResult

  protected open fun fallback(
    accountKey: MicroBlogKey,
    status: UiStatus,
  ) = StatusResult(
    accountKey = accountKey,
    statusKey = status.statusKey,
    liked = status.liked,
    retweeted = status.retweeted,
  )
}

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

import com.warpnet.services.mastodon.MastodonService
import com.warpnet.warpnetandroid.dataprovider.mapper.toUi
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.model.ui.UiPoll
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.repository.StatusRepository
import com.warpnet.warpnetandroid.utils.notifyError

class MastodonVoteJob(
  private val accountRepository: AccountRepository,
  private val statusRepository: StatusRepository,
  private val inAppNotification: InAppNotification,
) {
  suspend fun execute(votes: List<Int>, accountKey: MicroBlogKey, statusKey: MicroBlogKey) {
    val status = statusRepository.loadFromCache(statusKey, accountKey = accountKey) ?: throw Error("Can't find any status matches:$statusKey")
    val pollId = status.poll?.id
    if (status.poll == null || status.platformType != PlatformType.Mastodon || pollId == null) {
      throw Error()
    }
    val service = accountRepository.findByAccountKey(accountKey)?.let {
      it.service as? MastodonService
    } ?: throw Error()

    var originPoll: UiPoll? = null
    statusRepository.updateStatus(statusKey = status.statusKey, accountKey = accountKey) {
      originPoll = it.poll
      it.copy(
        poll = it.poll?.copy(
          voted = true,
          ownVotes = votes
        )
      )
    }
    try {
      val newPoll = service.vote(pollId, votes).toUi()
      statusRepository.updateStatus(statusKey = status.statusKey, accountKey = accountKey) {
        it.copy(
          poll = newPoll
        )
      }
    } catch (e: Throwable) {
      statusRepository.updateStatus(statusKey = status.statusKey, accountKey = accountKey) {
        it.copy(
          poll = originPoll
        )
      }
      inAppNotification.notifyError(e)
      throw e
    }
  }
}

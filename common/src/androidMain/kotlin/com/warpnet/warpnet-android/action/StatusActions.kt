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

import androidx.work.WorkManager
import com.warpnet.warpnet-android.model.AccountDetails
import com.warpnet.warpnet-android.model.job.StatusResult
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.worker.database.DeleteDbStatusWorker
import com.warpnet.warpnet-android.worker.status.DeleteStatusWorker
import com.warpnet.warpnet-android.worker.status.LikeWorker
import com.warpnet.warpnet-android.worker.status.MastodonVoteWorker
import com.warpnet.warpnet-android.worker.status.RetweetWorker
import com.warpnet.warpnet-android.worker.status.StatusWorker
import com.warpnet.warpnet-android.worker.status.UnLikeWorker
import com.warpnet.warpnet-android.worker.status.UnRetweetWorker
import com.warpnet.warpnet-android.worker.status.UpdateStatusWorker

actual class StatusActions(
  private val workManager: WorkManager,
) : IStatusActions {
  actual override fun delete(status: UiStatus, account: AccountDetails) {
    workManager.beginWith(
      DeleteStatusWorker.create(
        status = status,
        accountKey = account.accountKey
      )
    ).then(DeleteDbStatusWorker.create(status.statusKey))
      .enqueue()
  }

  actual override fun like(status: UiStatus, account: AccountDetails) {
    workManager.beginWith(
      UpdateStatusWorker.create(
        StatusResult(
          accountKey = account.accountKey,
          statusKey = status.statusKey,
          liked = !status.liked
        )
      )
    ).then(
      if (status.liked) {
        StatusWorker.create<UnLikeWorker>(
          accountKey = account.accountKey,
          status = status
        )
      } else {
        StatusWorker.create<LikeWorker>(
          accountKey = account.accountKey,
          status = status
        )
      }
    ).then(listOf(UpdateStatusWorker.create())).enqueue()
  }

  actual override fun retweet(status: UiStatus, account: AccountDetails) {
    workManager.beginWith(
      UpdateStatusWorker.create(
        StatusResult(
          accountKey = account.accountKey,
          statusKey = status.statusKey,
          retweeted = !status.retweeted
        )
      )
    ).then(
      if (status.retweeted) {
        StatusWorker.create<UnRetweetWorker>(
          accountKey = account.accountKey,
          status = status
        )
      } else {
        StatusWorker.create<RetweetWorker>(
          accountKey = account.accountKey,
          status = status
        )
      }
    ).then(listOf(UpdateStatusWorker.create())).enqueue()
  }

  actual override fun vote(status: UiStatus, account: AccountDetails, votes: List<Int>) {
    workManager.beginWith(
      MastodonVoteWorker.create(
        statusKey = status.statusKey,
        accountKey = account.accountKey,
        votes = votes
      )
    ).enqueue()
  }
}

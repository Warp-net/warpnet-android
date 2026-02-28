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
package com.warpnet.warpnetandroid.action

import androidx.work.WorkManager
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.model.job.ComposeData
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.worker.compose.MastodonComposeWorker
import com.warpnet.warpnetandroid.worker.compose.WarpnetComposeWorker
import com.warpnet.warpnetandroid.worker.draft.RemoveDraftWorker
import com.warpnet.warpnetandroid.worker.draft.SaveDraftWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

actual class ComposeAction(
  private val workManager: WorkManager,
  private val repository: AccountRepository,
) {
  val scope = CoroutineScope(Dispatchers.IO)
  actual fun commit(
    data: ComposeData,
  ) {
    scope.launch {
      repository.activeAccount.firstOrNull()?.toUi()?.let { account ->
        val platformType = account.platformType
        val accountKey = account.userKey
        val worker = when (platformType) {
          PlatformType.Warpnet -> WarpnetComposeWorker.create(
            accountKey = accountKey,
            data = data,
          )
          PlatformType.StatusNet -> TODO()
          PlatformType.Fanfou -> TODO()
          PlatformType.Mastodon -> MastodonComposeWorker.create(
            accountKey = accountKey,
            data = data,
          )
        }
        workManager
          .beginWith(SaveDraftWorker.create(data = data))
          .then(worker)
          .then(RemoveDraftWorker.create(draftId = data.draftId))
          .enqueue()
      }
    }
  }
}

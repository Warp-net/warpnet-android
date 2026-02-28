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
package com.warpnet.warpnetandroid.jobs.draft

import com.warpnet.warpnetandroid.model.job.ComposeData
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.repository.DraftRepository
import com.warpnet.warpnetandroid.utils.notifyError

class SaveDraftJob(
  private val repository: DraftRepository,
  private val inAppNotification: InAppNotification,
) {
  suspend fun execute(data: ComposeData) {
    with(data) {
      try {
        repository.addOrUpgrade(
          content,
          images,
          composeType = composeType,
          statusKey = statusKey,
          draftId = draftId,
          excludedReplyUserIds = excludedReplyUserIds,
        )
      } catch (e: Throwable) {
        inAppNotification.notifyError(e)
        throw e
      }
    }
  }
}

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
import com.warpnet.warpnetandroid.model.job.DirectMessageDeleteData
import com.warpnet.warpnetandroid.model.job.DirectMessageSendData
import com.warpnet.warpnetandroid.worker.dm.DirectMessageDeleteWorker
import com.warpnet.warpnetandroid.worker.dm.WarpnetDirectMessageSendWorker

actual class DirectMessageAction(
  private val workManager: WorkManager,
) {
  actual fun send(
    platformType: PlatformType,
    data: DirectMessageSendData,
  ) {
    if (platformType == PlatformType.Warpnet) {
      val worker = WarpnetDirectMessageSendWorker.create(
        data = data,
      )
      workManager
        .beginWith(worker)
        .enqueue()
    }
  }

  actual fun delete(
    data: DirectMessageDeleteData
  ) {
    val worker = DirectMessageDeleteWorker.createWorker(deleteData = data)
    workManager
      .beginWith(worker)
      .enqueue()
  }
}

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
package com.warpnet.warpnet-android.worker.dm

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.warpnet.warpnet-android.db.transform.toWorkData
import com.warpnet.warpnet-android.jobs.dm.WarpnetDirectMessageSendJob
import com.warpnet.warpnet-android.model.job.DirectMessageSendData

class WarpnetDirectMessageSendWorker(
  context: Context,
  workerParams: WorkerParameters,
  warpnetDirectMessageSendJob: WarpnetDirectMessageSendJob
) : DirectMessageSendWorker(
  context,
  workerParams,
  warpnetDirectMessageSendJob
) {
  companion object {
    fun create(
      data: DirectMessageSendData,
    ) = OneTimeWorkRequestBuilder<WarpnetDirectMessageSendWorker>()
      .setInputData(
        Data.Builder()
          .putAll(data.toWorkData())
          .build()
      )
      .build()
  }
}

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
package com.warpnet.warpnet-android.worker.draft

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.warpnet.warpnet-android.jobs.draft.RemoveDraftJob

class RemoveDraftWorker(
  appContext: Context,
  params: WorkerParameters,
  private val removeDraftJob: RemoveDraftJob
) : CoroutineWorker(appContext, params) {

  companion object {
    fun create(draftId: String) = OneTimeWorkRequestBuilder<RemoveDraftWorker>()
      .setInputData(
        workDataOf(
          "draftId" to draftId
        )
      )
      .build()
  }

  override suspend fun doWork(): Result {
    val draftId = inputData.getString("draftId") ?: return Result.failure()
    return try {
      removeDraftJob.execute(
        draftId = draftId
      )
      Result.success()
    } catch (e: Throwable) {
      e.printStackTrace()
      Result.failure()
    }
  }
}

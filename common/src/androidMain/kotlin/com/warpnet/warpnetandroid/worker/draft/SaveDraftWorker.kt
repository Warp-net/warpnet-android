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
package com.warpnet.warpnetandroid.worker.draft

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.warpnet.warpnetandroid.db.transform.toComposeData
import com.warpnet.warpnetandroid.db.transform.toWorkData
import com.warpnet.warpnetandroid.jobs.draft.SaveDraftJob
import com.warpnet.warpnetandroid.model.job.ComposeData

class SaveDraftWorker(
  context: Context,
  workerParams: WorkerParameters,
  private val saveDraftJob: SaveDraftJob
) : CoroutineWorker(context, workerParams) {

  companion object {
    fun create(data: ComposeData) = OneTimeWorkRequestBuilder<SaveDraftWorker>()
      .setInputData(data.toWorkData())
      .build()
  }

  override suspend fun doWork(): Result {
    val data = inputData.toComposeData()
    return try {
      saveDraftJob.execute(data = data)
      Result.success()
    } catch (e: Throwable) {
      e.printStackTrace()
      Result.failure()
    }
  }
}

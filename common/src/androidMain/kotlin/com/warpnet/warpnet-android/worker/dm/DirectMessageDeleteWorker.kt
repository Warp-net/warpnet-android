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
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.warpnet.warpnet-android.db.transform.toDirectMessageDeleteData
import com.warpnet.warpnet-android.db.transform.toWorkData
import com.warpnet.warpnet-android.jobs.dm.DirectMessageDeleteJob
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.job.DirectMessageDeleteData

class DirectMessageDeleteWorker(
  context: Context,
  workerParams: WorkerParameters,
  private val deleteJob: DirectMessageDeleteJob
) : CoroutineWorker(
  context,
  workerParams
) {
  companion object {
    fun createWorker(deleteData: DirectMessageDeleteData) = OneTimeWorkRequestBuilder<DirectMessageDeleteWorker>()
      .setInputData(
        deleteData.toWorkData()
      )
      .build()
  }

  override suspend fun doWork(): Result {
    val deleteData = inputData.toDirectMessageDeleteData()
    val accountKey = inputData.getString("accountKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    return try {
      deleteJob.execute(
        deleteData = deleteData,
        accountKey = accountKey
      )
      Result.success()
    } catch (e: Throwable) {
      e.printStackTrace()
      Result.failure()
    }
  }
}

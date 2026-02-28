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
package com.warpnet.warpnet-android.worker.status

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.warpnet.warpnet-android.db.transform.toWorkData
import com.warpnet.warpnet-android.jobs.status.StatusJob
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.ui.UiStatus

abstract class StatusWorker(
  appContext: Context,
  params: WorkerParameters,
  private val statusJob: StatusJob,
) : CoroutineWorker(appContext, params) {
  companion object {
    inline fun <reified T : StatusWorker> create(
      accountKey: MicroBlogKey,
      status: UiStatus,
    ) = OneTimeWorkRequestBuilder<T>()
      .setInputData(
        workDataOf(
          "accountKey" to accountKey.toString(),
          "statusKey" to status.statusKey.toString(),
        )
      )
      .build()
  }

  override suspend fun doWork(): Result {
    val accountKey = inputData.getString("accountKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    val statusKey = inputData.getString("statusKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    return try {
      statusJob.execute(
        accountKey = accountKey,
        statusKey = statusKey
      ).let {
        Result.success(it.toWorkData())
      }
    } catch (e: Throwable) {
      Result.failure()
    }
  }
}

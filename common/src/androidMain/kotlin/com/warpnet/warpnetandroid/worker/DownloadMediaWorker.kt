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
package com.warpnet.warpnetandroid.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.warpnet.warpnetandroid.jobs.common.DownloadMediaJob
import com.warpnet.warpnetandroid.model.MicroBlogKey

class DownloadMediaWorker(
  context: Context,
  workerParams: WorkerParameters,
  private val downloadMediaJob: DownloadMediaJob,
) : CoroutineWorker(context, workerParams) {

  companion object {
    fun create(
      accountKey: MicroBlogKey,
      source: String,
      target: String,
    ) = OneTimeWorkRequestBuilder<DownloadMediaWorker>()
      .setInputData(
        Data.Builder()
          .putString("accountKey", accountKey.toString())
          .putString("source", source)
          .putString("target", target)
          .build()
      )
      .build()
  }

  override suspend fun doWork(): Result {
    val target = inputData.getString("target") ?: return Result.failure()
    val source = inputData.getString("source") ?: return Result.failure()
    val accountKey = inputData.getString("accountKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    return try {
      downloadMediaJob.execute(
        target = target,
        source = source,
        accountKey = accountKey
      )
      Result.success()
    } catch (e: Throwable) {
      e.printStackTrace()
      Result.failure()
    }
  }
}

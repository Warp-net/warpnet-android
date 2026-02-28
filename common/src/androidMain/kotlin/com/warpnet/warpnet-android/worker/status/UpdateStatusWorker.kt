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
import androidx.work.OverwritingInputMerger
import androidx.work.WorkerParameters
import androidx.work.setInputMerger
import com.warpnet.warpnet-android.db.transform.toWorkData
import com.warpnet.warpnet-android.extensions.getNullableBoolean
import com.warpnet.warpnet-android.extensions.getNullableLong
import com.warpnet.warpnet-android.jobs.status.UpdateStatusJob
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.job.StatusResult

class UpdateStatusWorker(
  appContext: Context,
  params: WorkerParameters,
  private val updateStatusJob: UpdateStatusJob,
) : CoroutineWorker(appContext, params) {
  companion object {
    fun create(statusResult: StatusResult? = null) = OneTimeWorkRequestBuilder<UpdateStatusWorker>()
      .setInputMerger(OverwritingInputMerger::class)
      .apply {
        statusResult?.let {
          setInputData(it.toWorkData())
        }
      }
      .build()
  }

  override suspend fun doWork(): Result {
    val accountKey = inputData.getString("accountKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    val statusKey = inputData.getString("statusKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    val liked = inputData.getNullableBoolean("liked")
    val retweeted = inputData.getNullableBoolean("retweeted")
    val retweetCount = inputData.getNullableLong("retweetCount")
    val likeCount = inputData.getNullableLong("likeCount")
    updateStatusJob.execute(
      accountKey = accountKey,
      statusKey = statusKey,
      liked = liked,
      likeCount = likeCount,
      retweeted = retweeted,
      retweetCount = retweetCount,
    )
    return Result.success()
  }
}

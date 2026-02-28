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
package com.warpnet.warpnet-android.worker.compose

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.warpnet.services.microblog.MicroBlogService
import com.warpnet.warpnet-android.db.transform.toComposeData
import com.warpnet.warpnet-android.jobs.compose.ComposeJob
import com.warpnet.warpnet-android.model.MicroBlogKey

abstract class ComposeWorker<T : MicroBlogService>(
  protected val context: Context,
  workerParams: WorkerParameters,
  private val composeJob: ComposeJob<*>
) : CoroutineWorker(context, workerParams) {

  override suspend fun doWork(): Result {
    val composeData = inputData.toComposeData()
    val accountKey = inputData.getString("accountKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    return try {
      composeJob.execute(
        composeData = composeData,
        accountKey = accountKey
      )
      Result.success()
    } catch (e: Throwable) {
      e.printStackTrace()
      Result.failure()
    }
  }
}

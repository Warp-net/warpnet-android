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
package com.warpnet.warpnetandroid.worker.dm

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.warpnet.warpnetandroid.jobs.dm.DirectMessageFetchJob
import java.util.concurrent.TimeUnit

class DirectMessageFetchWorker(
  context: Context,
  workerParams: WorkerParameters,
  private val directMessageFetchJob: DirectMessageFetchJob
) : CoroutineWorker(
  context,
  workerParams
) {
  companion object {
    fun createRepeatableWorker() = PeriodicWorkRequestBuilder<DirectMessageFetchWorker>(15, TimeUnit.MINUTES)
      .build()
  }

  override suspend fun doWork(): Result {
    try {
      directMessageFetchJob.execute()
    } catch (e: Throwable) {
      // no need to handle this error
    }
    return Result.success()
  }
}

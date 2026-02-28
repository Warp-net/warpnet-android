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
import androidx.work.WorkerParameters
import com.warpnet.warpnetandroid.db.transform.toDirectMessageSendData
import com.warpnet.warpnetandroid.jobs.dm.DirectMessageSendJob
import com.warpnet.warpnetandroid.model.MicroBlogKey

abstract class DirectMessageSendWorker(
  context: Context,
  workerParams: WorkerParameters,
  private val directMessageSendJob: DirectMessageSendJob<*>,
) : CoroutineWorker(
  context,
  workerParams
) {

  override suspend fun doWork(): Result {
    val sendData = inputData.toDirectMessageSendData()
    val accountKey = inputData.getString("accountKey")?.let {
      MicroBlogKey.valueOf(it)
    } ?: return Result.failure()
    return try {
      directMessageSendJob.execute(
        sendData = sendData,
        accountKey = accountKey
      )
      Result.success()
    } catch (e: Throwable) {
      e.printStackTrace()
      Result.failure()
    }
  }
}

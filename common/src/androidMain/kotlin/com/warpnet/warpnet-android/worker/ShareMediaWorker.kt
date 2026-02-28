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
package com.warpnet.warpnet-android.worker

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkerParameters
import com.warpnet.warpnet-android.jobs.common.ShareMediaJob

class ShareMediaWorker(
  context: Context,
  workerParams: WorkerParameters,
  private val shareMediaJob: ShareMediaJob,
) : CoroutineWorker(context, workerParams) {

  companion object {
    fun create(
      target: Uri,
      extraText: String = "",
    ) = OneTimeWorkRequestBuilder<ShareMediaWorker>()
      .setInputData(
        Data.Builder()
          .putString("target", target.toString())
          .putString("extraText", extraText)
          .build()
      )
      .build()
  }

  override suspend fun doWork(): Result {
    val target = inputData.getString("target") ?: return Result.failure()
    val extraText = inputData.getString("extraText").orEmpty()
    return try {
      shareMediaJob.execute(target, extraText)
      Result.success()
    } catch (e: Throwable) {
      e.printStackTrace()
      Result.failure()
    }
  }
}

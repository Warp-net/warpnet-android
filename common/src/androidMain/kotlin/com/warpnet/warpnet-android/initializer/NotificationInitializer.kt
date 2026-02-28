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
package com.warpnet.warpnet-android.initializer

import android.content.Context
import androidx.startup.Initializer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.warpnet.warpnet-android.worker.NotificationWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class NotificationInitializerHolder

private const val NotificationWorkName = "warpnet-android_notification"

class NotificationInitializer : Initializer<NotificationInitializerHolder>, KoinComponent {
  private val workManager: WorkManager by inject()

  override fun create(context: Context): NotificationInitializerHolder {
    val request = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)
      .build()
    workManager.enqueueUniquePeriodicWork(
      NotificationWorkName,
      ExistingPeriodicWorkPolicy.KEEP,
      request
    )

    return NotificationInitializerHolder()
  }

  override fun dependencies() = listOf(
    NotificationChannelInitializer::class.java,
  )
}

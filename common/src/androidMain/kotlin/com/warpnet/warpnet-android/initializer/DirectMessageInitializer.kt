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
import androidx.work.WorkManager
import com.warpnet.warpnet-android.worker.dm.DirectMessageFetchWorker
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DirectMessageInitializerHolder

private const val DirectMessageWorkName = "warpnet-android_direct_message"

class DirectMessageInitializer : Initializer<DirectMessageInitializerHolder>, KoinComponent {
  private val workManager: WorkManager by inject()

  override fun create(context: Context): DirectMessageInitializerHolder {
    workManager.enqueueUniquePeriodicWork(
      DirectMessageWorkName,
      ExistingPeriodicWorkPolicy.KEEP,
      DirectMessageFetchWorker.createRepeatableWorker()
    )
    return DirectMessageInitializerHolder()
  }

  override fun dependencies(): MutableList<Class<out Initializer<*>>> {
    return mutableListOf()
  }
}

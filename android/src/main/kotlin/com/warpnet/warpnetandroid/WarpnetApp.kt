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
package com.warpnet.warpnetandroid

import android.content.Context
import android.net.ConnectivityManager
import androidx.startup.AppInitializer
import com.warpnet.warpnetandroid.initializer.DirectMessageInitializer
import com.warpnet.warpnetandroid.initializer.NotificationChannelInitializer
import com.warpnet.warpnetandroid.initializer.NotificationInitializer
import com.warpnet.warpnetandroid.initializer.WarpnetServiceInitializer
import com.warpnet.warpnetandroid.utils.AppShortcutCreator
import com.warpnet.warpnetandroid.utils.asIsActiveNetworkFlow
import kotlinx.coroutines.flow.Flow

class WarpnetApp : WarpnetApplication() {
  override fun onCreate() {
    super.onCreate()
    appShortcutCreator = AppShortcutCreator(this).apply {
      configureAppShortcuts()
    }
    // Note:Installs with missing splits are now blocked on devices which have Play Protect active or run on Android 10.
    // But there are still some custom roms allows missing splits which causes resources not found exception
    if (MissingSplitsCheckerImpl().requiredSplits(this)) {
      return
    }
    // manually setup NotificationInitializer since it require HiltWorkerFactory
    AppInitializer.getInstance(this)
      .apply {
        initializeComponent(NotificationChannelInitializer::class.java)
        initializeComponent(NotificationInitializer::class.java)
        initializeComponent(DirectMessageInitializer::class.java)
        initializeComponent(WarpnetServiceInitializer::class.java)
      }
    isNetworkActiveFlow = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).asIsActiveNetworkFlow()
  }

  interface MissingSplitsChecker {
    fun requiredSplits(context: Context): Boolean
  }

  companion object {
    lateinit var isNetworkActiveFlow: Flow<Boolean>
  }
}

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
package com.warpnet.warpnetandroid.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * In-memory account preferences (no persistence).
 */
class AccountPreferences {
  private val isNotificationEnabledFlow = MutableStateFlow(true)
  private val homeMenuOrderFlow = MutableStateFlow<List<Pair<HomeMenus, Boolean>>>(
    HomeMenus.values().map { it to it.showDefault }
  )

  val isNotificationEnabled: Flow<Boolean>
    get() = isNotificationEnabledFlow

  val homeMenuOrder: Flow<List<Pair<HomeMenus, Boolean>>>
    get() = homeMenuOrderFlow

  suspend fun setIsNotificationEnabled(value: Boolean) {
    isNotificationEnabledFlow.value = value
  }

  fun close() {
    // No-op for in-memory implementation
  }

  suspend fun setHomeMenuOrder(data: List<Pair<HomeMenus, Boolean>>) {
    homeMenuOrderFlow.value = data
  }
}

expect class AccountPreferencesFactory {
  fun create(accountKey: MicroBlogKey): AccountPreferences
}

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
package com.warpnet.warpnetandroid.preferences

import androidx.datastore.core.DataStore
import com.warpnet.warpnetandroid.preferences.model.AccountPreferences
import com.warpnet.warpnetandroid.preferences.model.AppearancePreferences
import com.warpnet.warpnetandroid.preferences.model.DisplayPreferences
import com.warpnet.warpnetandroid.preferences.model.MiscPreferences
import com.warpnet.warpnetandroid.preferences.model.NotificationPreferences
import com.warpnet.warpnetandroid.preferences.model.SwipePreferences
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.firstOrNull

data class PreferencesHolder(
  val accountPreferences: DataStore<AccountPreferences>,
  val appearancePreferences: DataStore<AppearancePreferences>,
  val displayPreferences: DataStore<DisplayPreferences>,
  val miscPreferences: DataStore<MiscPreferences>,
  val notificationPreferences: DataStore<NotificationPreferences>,
  val swipePreferences: DataStore<SwipePreferences>,
) {
  suspend fun warmup() = coroutineScope {
    awaitAll(
      async { accountPreferences.data.firstOrNull() },
      async { appearancePreferences.data.firstOrNull() },
      async { displayPreferences.data.firstOrNull() },
      async { miscPreferences.data.firstOrNull() },
      async { notificationPreferences.data.firstOrNull() },
      async { swipePreferences.data.firstOrNull() },
    )
  }
}

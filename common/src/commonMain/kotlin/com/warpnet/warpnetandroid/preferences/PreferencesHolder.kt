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

import com.warpnet.warpnetandroid.preferences.model.AccountPreferences
import com.warpnet.warpnetandroid.preferences.model.AppearancePreferences
import com.warpnet.warpnetandroid.preferences.model.DisplayPreferences
import com.warpnet.warpnetandroid.preferences.model.MiscPreferences
import com.warpnet.warpnetandroid.preferences.model.NotificationPreferences
import com.warpnet.warpnetandroid.preferences.model.SwipePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * In-memory preferences holder that doesn't persist data.
 * All preferences are kept in memory and reset when the app restarts.
 */
data class PreferencesHolder(
  private val accountPreferencesFlow: MutableStateFlow<AccountPreferences> = MutableStateFlow(AccountPreferences()),
  private val appearancePreferencesFlow: MutableStateFlow<AppearancePreferences> = MutableStateFlow(AppearancePreferences()),
  private val displayPreferencesFlow: MutableStateFlow<DisplayPreferences> = MutableStateFlow(DisplayPreferences()),
  private val miscPreferencesFlow: MutableStateFlow<MiscPreferences> = MutableStateFlow(MiscPreferences()),
  private val notificationPreferencesFlow: MutableStateFlow<NotificationPreferences> = MutableStateFlow(NotificationPreferences()),
  private val swipePreferencesFlow: MutableStateFlow<SwipePreferences> = MutableStateFlow(SwipePreferences()),
) {
  val accountPreferences: Flow<AccountPreferences> = accountPreferencesFlow
  val appearancePreferences: Flow<AppearancePreferences> = appearancePreferencesFlow
  val displayPreferences: Flow<DisplayPreferences> = displayPreferencesFlow
  val miscPreferences: Flow<MiscPreferences> = miscPreferencesFlow
  val notificationPreferences: Flow<NotificationPreferences> = notificationPreferencesFlow
  val swipePreferences: Flow<SwipePreferences> = swipePreferencesFlow

  suspend fun updateAccountPreferences(transform: (AccountPreferences) -> AccountPreferences) {
    accountPreferencesFlow.value = transform(accountPreferencesFlow.value)
  }

  suspend fun updateAppearancePreferences(transform: (AppearancePreferences) -> AppearancePreferences) {
    appearancePreferencesFlow.value = transform(appearancePreferencesFlow.value)
  }

  suspend fun updateDisplayPreferences(transform: (DisplayPreferences) -> DisplayPreferences) {
    displayPreferencesFlow.value = transform(displayPreferencesFlow.value)
  }

  suspend fun updateMiscPreferences(transform: (MiscPreferences) -> MiscPreferences) {
    miscPreferencesFlow.value = transform(miscPreferencesFlow.value)
  }

  suspend fun updateNotificationPreferences(transform: (NotificationPreferences) -> NotificationPreferences) {
    notificationPreferencesFlow.value = transform(notificationPreferencesFlow.value)
  }

  suspend fun updateSwipePreferences(transform: (SwipePreferences) -> SwipePreferences) {
    swipePreferencesFlow.value = transform(swipePreferencesFlow.value)
  }

  suspend fun warmup() {
    // No-op for in-memory implementation
  }
}

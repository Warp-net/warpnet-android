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
package com.warpnet.warpnet-android.scenes.settings.account

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.preferences.PreferencesHolder
import com.warpnet.warpnet-android.preferences.model.AccountPreferences
import kotlinx.coroutines.flow.Flow

@Composable
fun PrivacyAndSafetyPresenter(
  event: Flow<PrivacyAndSafetyEvent>,
  preferencesHolder: PreferencesHolder = get(),
): PrivacyAndSafetyState {
  val accountPreferences by remember {
    preferencesHolder.accountPreferences.data
  }.collectAsState(AccountPreferences())
  LaunchedEffect(Unit) {
    event.collect { event ->
      when (event) {
        is PrivacyAndSafetyEvent.SetIsAlwaysShowSensitiveMedia -> {
          preferencesHolder.accountPreferences.updateData {
            it.copy(isAlwaysShowSensitiveMedia = event.bool)
          }
        }
      }
    }
  }
  return PrivacyAndSafetyState(
    account = accountPreferences,
  )
}

sealed interface PrivacyAndSafetyEvent {
  data class SetIsAlwaysShowSensitiveMedia(val bool: Boolean) : PrivacyAndSafetyEvent
}

@Immutable
data class PrivacyAndSafetyState(
  val account: AccountPreferences,
)

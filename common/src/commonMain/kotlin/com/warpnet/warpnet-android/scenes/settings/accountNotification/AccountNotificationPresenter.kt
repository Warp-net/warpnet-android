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
package com.warpnet.warpnet-android.scenes.settings.accountNotification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.ui.UiUser
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.repository.findByAccountKeyFlow
import kotlinx.coroutines.flow.Flow

@Composable
fun AccountNotificationPresenter(
  accountKey: MicroBlogKey,
  event: Flow<AccountNotificationEvent>,
  accountRepository: AccountRepository = get(),
): AccountNotificationState {
  val account by accountRepository.findByAccountKeyFlow(accountKey).collectAsState(null)
  val user = remember(account) {
    account?.toUi()
  }
  val preferences = remember { accountRepository.getAccountPreferences(accountKey) }
  val isNotificationEnabled by preferences.isNotificationEnabled.collectAsState(false)

  LaunchedEffect(Unit) {
    event.collect {
      when (it) {
        is AccountNotificationEvent.SetIsNotificationEnabled -> {
          preferences.setIsNotificationEnabled(it.value)
        }
      }
    }
  }

  return AccountNotificationState(
    user = user,
    isNotificationEnabled = isNotificationEnabled
  )
}

data class AccountNotificationState(
  val user: UiUser?,
  val isNotificationEnabled: Boolean,
)

interface AccountNotificationEvent {
  data class SetIsNotificationEnabled(val value: Boolean) : AccountNotificationEvent
}

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
package com.warpnet.warpnetandroid.viewmodel.warpnet.user

import com.warpnet.services.microblog.LookupService
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.repository.UserRepository
import com.warpnet.warpnetandroid.utils.notifyError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import moe.tlaster.precompose.viewmodel.ViewModel

class WarpnetUserViewModel(
  private val repository: UserRepository,
  private val inAppNotification: InAppNotification,
  private val accountRepository: AccountRepository,
  private val screenName: String,
) : ViewModel() {

  private val account by lazy {
    accountRepository.activeAccount.mapNotNull { it }
  }

  val error = MutableStateFlow<Throwable?>(null)

  val user by lazy {
    account.map {
      try {
        repository.lookupUserByName(
          screenName,
          accountKey = it.accountKey,
          lookupService = it.service as LookupService,
        )
      } catch (e: Throwable) {
        inAppNotification.notifyError(e)
        error.value = e
        null
      }
    }
  }
}

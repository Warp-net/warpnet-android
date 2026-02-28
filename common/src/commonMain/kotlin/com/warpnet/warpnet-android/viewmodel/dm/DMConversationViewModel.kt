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
package com.warpnet.warpnet-android.viewmodel.dm

import androidx.paging.cachedIn
import com.warpnet.services.microblog.DirectMessageService
import com.warpnet.services.microblog.LookupService
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.repository.DirectMessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class DMConversationViewModel(
  private val repository: DirectMessageRepository,
  private val accountRepository: AccountRepository,
) : ViewModel() {
  private val account by lazy {
    accountRepository.activeAccount.mapNotNull { it }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  val source by lazy {
    account.mapNotNull { it }.filter { it.service is DirectMessageService }.flatMapLatest { account ->
      repository.dmConversationListSource(
        accountKey = account.accountKey,
        service = account.service as DirectMessageService,
        lookupService = account.service as LookupService
      )
    }.cachedIn(viewModelScope)
  }
}

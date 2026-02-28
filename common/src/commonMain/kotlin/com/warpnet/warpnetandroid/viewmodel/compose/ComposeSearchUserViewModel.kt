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
package com.warpnet.warpnetandroid.viewmodel.compose

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.warpnet.services.microblog.SearchService
import com.warpnet.warpnetandroid.defaultLoadCount
import com.warpnet.warpnetandroid.paging.source.SearchUserPagingSource
import com.warpnet.warpnetandroid.repository.AccountRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class ComposeSearchUserViewModel(
  private val accountRepository: AccountRepository,
) : ViewModel() {
  private val account by lazy {
    accountRepository.activeAccount.mapNotNull { it }
  }

  val text = MutableStateFlow("")

  @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
  val source = text.debounce(666L).filterNot { it.isEmpty() }.flatMapLatest { str ->
    account.flatMapLatest { account ->
      Pager(
        config = PagingConfig(
          pageSize = defaultLoadCount,
          enablePlaceholders = false,
        )
      ) {
        SearchUserPagingSource(
          accountKey = account.accountKey,
          str,
          account.service as SearchService
        )
      }.flow
    }
  }.cachedIn(viewModelScope)
}

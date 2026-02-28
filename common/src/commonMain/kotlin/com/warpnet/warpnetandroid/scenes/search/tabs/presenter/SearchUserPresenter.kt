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
package com.warpnet.warpnetandroid.scenes.search.tabs.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.services.microblog.SearchService
import com.warpnet.warpnetandroid.defaultLoadCount
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.paging.source.SearchUserPagingSource
import com.warpnet.warpnetandroid.scenes.CurrentAccountPresenter
import com.warpnet.warpnetandroid.scenes.CurrentAccountState

@Composable
fun SearchUserPresenter(
  keyword: String,
  following: Boolean = false,
): SearchUserState {
  val accountState = CurrentAccountPresenter()

  if (accountState !is CurrentAccountState.Account) {
    return SearchUserState.NoAccount
  }

  val data = remember(accountState) {
    Pager(
      config = PagingConfig(
        pageSize = defaultLoadCount,
        enablePlaceholders = false,
      )
    ) {
      SearchUserPagingSource(
        accountKey = accountState.account.accountKey,
        keyword,
        accountState.account.service as SearchService,
        following = following
      )
    }.flow
  }

  return SearchUserState.Data(
    data = data.collectAsLazyPagingItems()
  )
}

interface SearchUserState {
  data class Data(
    val data: LazyPagingItems<UiUser>
  ) : SearchUserState

  object NoAccount : SearchUserState
}

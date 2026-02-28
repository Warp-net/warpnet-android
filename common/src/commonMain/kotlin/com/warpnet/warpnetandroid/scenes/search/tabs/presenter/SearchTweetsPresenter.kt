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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import com.warpnet.services.microblog.SearchService
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.paging.mediator.paging.pager
import com.warpnet.warpnetandroid.paging.mediator.search.SearchStatusMediator
import com.warpnet.warpnetandroid.scenes.CurrentAccountPresenter
import com.warpnet.warpnetandroid.scenes.CurrentAccountState
import kotlinx.coroutines.flow.map

@Composable
fun SearchTweetsPresenter(
  keyword: String,
  database: CacheDatabase = get(),
): SearchTweetsState {
  val scope = rememberCoroutineScope()
  val accountState = CurrentAccountPresenter()
  if (accountState !is CurrentAccountState.Account) {
    return SearchTweetsState.NoAccount
  }
  val data = remember(accountState) {
    SearchStatusMediator(
      keyword,
      database,
      accountState.account.accountKey,
      accountState.account.service as SearchService
    ).pager().flow.map { it.map { it.status } }.cachedIn(scope)
  }
  return SearchTweetsState.Data(
    data = data.collectAsLazyPagingItems()
  )
}

interface SearchTweetsState {
  data class Data(
    val data: LazyPagingItems<UiStatus>
  ) : SearchTweetsState
  object NoAccount : SearchTweetsState
}

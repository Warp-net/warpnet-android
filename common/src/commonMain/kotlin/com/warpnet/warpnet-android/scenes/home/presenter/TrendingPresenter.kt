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
package com.warpnet.warpnet-android.scenes.home.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.services.microblog.TrendService
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.extensions.rememberNestedPresenter
import com.warpnet.warpnet-android.model.ui.UiTrend
import com.warpnet.warpnet-android.repository.TrendRepository
import com.warpnet.warpnet-android.scenes.CurrentAccountPresenter
import com.warpnet.warpnet-android.scenes.CurrentAccountState
import com.warpnet.warpnet-android.scenes.search.presenter.SearchInputEvent
import com.warpnet.warpnet-android.scenes.search.presenter.SearchInputPresenter
import com.warpnet.warpnet-android.scenes.search.presenter.SearchInputState
import kotlinx.coroutines.flow.Flow

@Composable
fun TrendingPresenter(
  events: Flow<SearchInputEvent>,
  repository: TrendRepository = get(),
): SearchItemState {
  val accountState = CurrentAccountPresenter()

  if (accountState !is CurrentAccountState.Account) {
    return SearchItemState.NoAccount
  }

  val (state, channel) = rememberNestedPresenter<SearchInputState, SearchInputEvent> {
    SearchInputPresenter(it, keyword = "")
  }

  LaunchedEffect(Unit) {
    events.collect {
      channel.trySend(it)
    }
  }

  val pagingData = remember {
    repository.trendsSource(
      accountKey = accountState.account.accountKey,
      service = accountState.account.service as TrendService
    )
  }

  return SearchItemState.Data(
    data = pagingData.collectAsLazyPagingItems(),
    searchInputState = state
  )
}

interface SearchItemState {
  data class Data(
    val data: LazyPagingItems<UiTrend>,
    val searchInputState: SearchInputState
  ) : SearchItemState
  object NoAccount : SearchItemState
}

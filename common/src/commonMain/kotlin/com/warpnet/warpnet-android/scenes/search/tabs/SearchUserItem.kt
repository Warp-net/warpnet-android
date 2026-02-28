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
package com.warpnet.warpnet-android.scenes.search.tabs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.LoadState
import com.warpnet.warpnet-android.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnet-android.component.lazy.ui.LazyUiUserList
import com.warpnet.warpnet-android.component.navigation.user
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.extensions.refreshOrRetry
import com.warpnet.warpnet-android.extensions.rememberPresenter
import com.warpnet.warpnet-android.navigation.rememberUserNavigationData
import com.warpnet.warpnet-android.scenes.search.tabs.presenter.SearchUserPresenter
import com.warpnet.warpnet-android.scenes.search.tabs.presenter.SearchUserState
import moe.tlaster.precompose.navigation.Navigator

class SearchUserItem : SearchSceneItem {
  @Composable
  override fun name(): String {
    return stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_search_tabs_users)
  }

  @Composable
  override fun Content(
    keyword: String,
    navigator: Navigator,
  ) {
    val state by rememberPresenter {
      SearchUserPresenter(keyword = keyword)
    }.collectAsState()

    (state as? SearchUserState.Data)?.let {
      val userNavigationData = rememberUserNavigationData(navigator)
      SwipeToRefreshLayout(
        refreshingState = it.data.loadState.refresh is LoadState.Loading,
        onRefresh = {
          it.data.refreshOrRetry()
        }
      ) {
        LazyUiUserList(
          items = it.data,
          onItemClicked = {
            navigator.user(it)
          },
          userNavigationData = userNavigationData,
        )
      }
    }
  }
}

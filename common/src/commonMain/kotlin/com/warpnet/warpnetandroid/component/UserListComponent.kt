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
package com.warpnet.warpnetandroid.component

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.warpnet.warpnetandroid.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnetandroid.component.lazy.ui.LazyUiUserList
import com.warpnet.warpnetandroid.extensions.refreshOrRetry
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.navigation.UserNavigationData

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserListComponent(
  source: LazyPagingItems<UiUser>,
  userNavigationData: UserNavigationData,
  action: @Composable (user: UiUser) -> Unit = {},
) {
  SwipeToRefreshLayout(
    refreshingState = source.loadState.refresh is LoadState.Loading,
    onRefresh = {
      source.refreshOrRetry()
    }
  ) {
    LazyUiUserList(
      items = source,
      userNavigationData = userNavigationData,
      onItemClicked = {
        userNavigationData.statusNavigation.toUser(it)
      },
      action = action,
    )
  }
}

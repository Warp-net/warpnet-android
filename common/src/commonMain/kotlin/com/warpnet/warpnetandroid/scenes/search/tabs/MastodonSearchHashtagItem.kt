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
package com.warpnet.warpnetandroid.scenes.search.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.items
import com.warpnet.warpnetandroid.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnetandroid.component.navigation.hashtag
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.refreshOrRetry
import com.warpnet.warpnetandroid.extensions.rememberPresenter
import com.warpnet.warpnetandroid.scenes.search.tabs.presenter.MastodonSearchHashtagPresenter
import com.warpnet.warpnetandroid.scenes.search.tabs.presenter.MastodonSearchHashtagState
import moe.tlaster.precompose.navigation.Navigator

class MastodonSearchHashtagItem : SearchSceneItem {
  @Composable
  override fun name(): String {
    return stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_search_tabs_hashtag)
  }

  @OptIn(ExperimentalMaterialApi::class)
  @Composable
  override fun Content(
    keyword: String,
    navigator: Navigator,
  ) {
    val state by rememberPresenter {
      MastodonSearchHashtagPresenter(keyword = keyword)
    }.collectAsState()

    (state as? MastodonSearchHashtagState.Data)?.let {
      SwipeToRefreshLayout(
        refreshingState = it.data.loadState.refresh is LoadState.Loading,
        onRefresh = {
          it.data.refreshOrRetry()
        }
      ) {
        if (it.data.itemCount > 0) {
          LazyColumn {
            items(it.data) {
              it?.name?.let { name ->
                ListItem(
                  modifier = Modifier
                    .clickable {
                      navigator.hashtag(name)
                    }
                ) {
                  Text(text = name)
                }
              }
            }
          }
        }
      }
    }
  }
}

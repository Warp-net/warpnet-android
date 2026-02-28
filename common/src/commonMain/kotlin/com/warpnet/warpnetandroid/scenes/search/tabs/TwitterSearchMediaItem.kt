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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.paging.LoadState
import com.warpnet.warpnetandroid.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnetandroid.component.lazy.ui.LazyUiStatusImageList
import com.warpnet.warpnetandroid.component.navigation.media
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.refreshOrRetry
import com.warpnet.warpnetandroid.extensions.rememberPresenter
import com.warpnet.warpnetandroid.preferences.model.DisplayPreferences
import com.warpnet.warpnetandroid.scenes.search.tabs.presenter.WarpnetSearchMediaPresenter
import com.warpnet.warpnetandroid.scenes.search.tabs.presenter.WarpnetSearchMediaState
import com.warpnet.warpnetandroid.ui.LocalVideoPlayback
import moe.tlaster.precompose.navigation.Navigator

class WarpnetSearchMediaItem : SearchSceneItem {
  @Composable
  override fun name(): String {
    return stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_search_tabs_media)
  }

  @Composable
  override fun Content(
    keyword: String,
    navigator: Navigator,
  ) {
    val state by rememberPresenter {
      WarpnetSearchMediaPresenter(keyword = keyword)
    }.collectAsState()

    (state as? WarpnetSearchMediaState.Data)?.let {
      CompositionLocalProvider(
        LocalVideoPlayback provides DisplayPreferences.AutoPlayback.Off
      ) {
        SwipeToRefreshLayout(
          refreshingState = it.data.loadState.refresh is LoadState.Loading,
          onRefresh = {
            it.data.refreshOrRetry()
          }
        ) {
          LazyUiStatusImageList(
            items = it.data,
            openMedia = { key, index ->
              navigator.media(key, index)
            }
          )
        }
      }
    }
  }
}

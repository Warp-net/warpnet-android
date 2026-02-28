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
package com.warpnet.warpnet-android.scenes.mastodon

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnet-android.component.lazy.ui.LazyUiStatusList
import com.warpnet.warpnet-android.di.ext.getViewModel
import com.warpnet.warpnet-android.extensions.refreshOrRetry
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.navigation.RootDeepLinks
import com.warpnet.warpnet-android.navigation.rememberStatusNavigationData
import com.warpnet.warpnet-android.ui.WarpnetScene
import com.warpnet.warpnet-android.viewmodel.mastodon.MastodonHashtagViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import io.github.seiko.precompose.annotation.Path
import moe.tlaster.precompose.navigation.Navigator
import org.koin.core.parameter.parametersOf

@NavGraphDestination(
  route = Root.Mastodon.Hashtag.route,
  deepLink = [RootDeepLinks.Mastodon.Hashtag.route]
)
@Composable
fun MastodonHashtagScene(
  @Path("keyword") keyword: String,
  navigator: Navigator,
) {
  val viewModel: MastodonHashtagViewModel = getViewModel {
    parametersOf(keyword)
  }
  val source = viewModel.source.collectAsLazyPagingItems()
  val statusNavigationData = rememberStatusNavigationData(navigator)
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          navigationIcon = {
            AppBarNavigationButton(
              onBack = {
                navigator.popBackStack()
              }
            )
          },
          title = {
            Text(text = keyword)
          }
        )
      }
    ) {
      SwipeToRefreshLayout(
        refreshingState = source.loadState.refresh is LoadState.Loading,
        onRefresh = {
          source.refreshOrRetry()
        },
      ) {
        LazyUiStatusList(
          items = source,
          statusNavigation = statusNavigationData,
        )
      }
    }
  }
}

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
package com.warpnet.warpnet-android.scenes.home.mastodon

import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.painter.Painter
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.foundation.Pager
import com.warpnet.warpnet-android.component.foundation.TextTabsComponent
import com.warpnet.warpnet-android.component.foundation.rememberPagerState
import com.warpnet.warpnet-android.component.lazy.LazyListController
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.model.HomeNavigationItem
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.scenes.home.AllNotificationItem
import com.warpnet.warpnet-android.scenes.home.MentionItem
import com.warpnet.warpnet-android.ui.LocalActiveAccount
import com.warpnet.warpnet-android.ui.WarpnetScene
import io.github.seiko.precompose.annotation.NavGraphDestination
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator

class MastodonNotificationItem : HomeNavigationItem() {
  @Composable
  override fun name(): String {
    return stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_notification_title)
  }

  override val route: String
    get() = Root.Mastodon.Notification

  @Composable
  override fun icon(): Painter {
    return painterResource(res = com.warpnet.warpnet-android.MR.files.ic_bell)
  }

  override var lazyListController: LazyListController = LazyListController()

  @Composable
  override fun Content(navigator: Navigator) {
    MastodonNotificationSceneContent(
      setLazyListController = {
        lazyListController = it
      },
      navigator = navigator,
    )
  }
}

@NavGraphDestination(
  route = Root.Mastodon.Notification,
)
@Composable
fun MastodonNotificationScene(
  navigator: Navigator
) {
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          title = {
            Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_notification_title))
          },
          navigationIcon = {
            AppBarNavigationButton(
              onBack = {
                navigator.popBackStack()
              }
            )
          }
        )
      }
    ) {
      MastodonNotificationSceneContent(
        navigator = navigator
      )
    }
  }
}

@Composable
fun MastodonNotificationSceneContent(
  navigator: Navigator,
  setLazyListController: ((lazyListController: LazyListController) -> Unit)? = null,
) {
  val account = LocalActiveAccount.current ?: return
  val tabs = remember(account) {
    listOf(
      AllNotificationItem(),
      MentionItem()
    )
  }
  val pagerState = rememberPagerState(pageCount = tabs.size)
  LaunchedEffect(pagerState.currentPage) {
    // FIXME: 2021/5/17 A little bit dirty
    setLazyListController?.invoke(tabs[pagerState.currentPage].lazyListController)
  }
  val scope = rememberCoroutineScope()
  Scaffold(
    topBar = {
      TextTabsComponent(
        items = tabs.map { it.name() },
        selectedItem = pagerState.currentPage,
        onItemSelected = {
          scope.launch {
            pagerState.selectPage {
              pagerState.currentPage = it
            }
          }
        },
      )
    }
  ) {
    Pager(state = pagerState) {
      tabs[page].Content(navigator)
    }
  }
}

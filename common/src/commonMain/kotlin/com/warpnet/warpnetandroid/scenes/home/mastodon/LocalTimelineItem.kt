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
package com.warpnet.warpnetandroid.scenes.home.mastodon

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.warpnet.services.mastodon.MastodonService
import com.warpnet.warpnetandroid.component.TimelineComponent
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.lazy.LazyListController
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.model.HomeNavigationItem
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.navigation.rememberStatusNavigationData
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.timeline.SavedStateKeyType
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

class LocalTimelineItem : HomeNavigationItem() {
  @Composable
  override fun name(): String {
    return stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_local_title)
  }

  override val route: String
    get() = Root.Mastodon.LocalTimeline

  @Composable
  override fun icon(): Painter {
    return painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_users)
  }

  @Composable
  override fun Content(navigator: Navigator) {
    LocalTimelineContent(
      lazyListController = lazyListController,
      navigator = navigator,
    )
  }
}

@NavGraphDestination(
  route = Root.Mastodon.LocalTimeline,
)
@Composable
fun LocalTimelineScene(
  navigator: Navigator,
) {
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
            Text(text = "Local")
          }
        )
      }
    ) {
      LocalTimelineContent(
        navigator = navigator,
      )
    }
  }
}

@Composable
fun LocalTimelineContent(
  navigator: Navigator,
  lazyListController: LazyListController? = null,
) {
  val account = LocalActiveAccount.current ?: return
  if (account.service !is MastodonService) {
    return
  }
  val statusNavigationData = rememberStatusNavigationData(navigator)
  TimelineComponent(
    lazyListController = lazyListController,
    savedStateKeyType = SavedStateKeyType.LOCAL,
    statusNavigation = statusNavigationData,
  )
}

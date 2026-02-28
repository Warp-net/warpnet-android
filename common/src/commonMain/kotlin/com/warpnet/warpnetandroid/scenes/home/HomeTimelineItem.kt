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
package com.warpnet.warpnetandroid.scenes.home

import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.component.TimelineComponent
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.lazy.LazyListController
import com.warpnet.warpnetandroid.component.navigation.compose
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.model.HomeNavigationItem
import com.warpnet.warpnetandroid.model.enums.ComposeType
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.navigation.StatusNavigationData
import com.warpnet.warpnetandroid.navigation.rememberStatusNavigationData
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.timeline.SavedStateKeyType
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

class HomeTimelineItem : HomeNavigationItem() {

  @Composable
  override fun name(): String = stringResource(com.warpnet.warpnetandroid.MR.strings.scene_timeline_title)
  override val route: String
    get() = Root.HomeTimeline

  @Composable
  override fun icon(): Painter = painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_home)

  @Composable
  override fun Content(navigator: Navigator) {
    val statusNavigation = rememberStatusNavigationData(navigator)
    HomeTimelineSceneContent(
      lazyListController = lazyListController,
      statusNavigation = statusNavigation,
    )
  }

  @Composable
  override fun Fab(navigator: Navigator) {
    HomeTimelineFab(
      navigator = navigator,
    )
  }

  override val fabSize: Dp
    get() = HomeTimeLineItemDefaults.FabSize
}

@NavGraphDestination(
  route = Root.HomeTimeline,
)
@Composable
fun HomeTimelineScene(
  navigator: Navigator,
) {
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_timeline_title))
          },
          navigationIcon = {
            AppBarNavigationButton(
              onBack = {
                navigator.popBackStack()
              }
            )
          }
        )
      },
      floatingActionButton = {
        HomeTimelineFab(
          navigator = navigator,
        )
      }
    ) {
      val statusNavigation = rememberStatusNavigationData(navigator)
      HomeTimelineSceneContent(
        statusNavigation = statusNavigation,
      )
    }
  }
}

@Composable
private fun HomeTimelineFab(
  navigator: Navigator,
) {
  FloatingActionButton(
    onClick = {
      navigator.compose(ComposeType.New)
    }
  ) {
    Icon(
      painter = painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_feather),
      contentDescription = stringResource(
        res = com.warpnet.warpnetandroid.MR.strings.accessibility_scene_home_compose
      ),
      modifier = Modifier.size(24.dp),
    )
  }
}

@Composable
fun HomeTimelineSceneContent(
  statusNavigation: StatusNavigationData,
  lazyListController: LazyListController? = null,
) {
  TimelineComponent(
    lazyListController = lazyListController,
    savedStateKeyType = SavedStateKeyType.HOME,
    statusNavigation = statusNavigation,
  )
}

private object HomeTimeLineItemDefaults {
  val FabSize = 56.dp
}

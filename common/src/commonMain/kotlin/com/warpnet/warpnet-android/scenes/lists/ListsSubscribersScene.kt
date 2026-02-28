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
package com.warpnet.warpnet-android.scenes.lists

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.warpnet.warpnet-android.component.UserListComponent
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.extensions.rememberPresenterState
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.navigation.rememberUserNavigationData
import com.warpnet.warpnet-android.ui.WarpnetScene
import com.warpnet.warpnet-android.viewmodel.user.UserListEvent
import com.warpnet.warpnet-android.viewmodel.user.UserListPresenter
import com.warpnet.warpnet-android.viewmodel.user.UserListState
import com.warpnet.warpnet-android.viewmodel.user.UserListType
import io.github.seiko.precompose.annotation.NavGraphDestination
import io.github.seiko.precompose.annotation.Path
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Lists.Subscribers.route,
)
@Composable
fun ListsSubscribersScene(
  @Path("listKey") listKey: String,
  navigator: Navigator,
) {
  ListsSubscribersScene(
    listKey = MicroBlogKey.valueOf(listKey),
    navigator = navigator,
  )
}

@Composable
fun ListsSubscribersScene(
  listKey: MicroBlogKey,
  navigator: Navigator,
) {
  val (state) = rememberPresenterState<UserListState, UserListEvent> {
    UserListPresenter(
      it,
      userType = UserListType.Followers(
        listKey
      )
    )
  }
  val userNavigationData = rememberUserNavigationData(navigator)
  (state as? UserListState.Data)?.let { data ->
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
              Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_lists_details_tabs_subscriber))
            }
          )
        },
      ) {
        UserListComponent(
          source = data.source,
          userNavigationData = userNavigationData,
        )
      }
    }
  }
}

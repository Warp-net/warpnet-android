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

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.painter.Painter
import com.warpnet.warpnetandroid.component.UserComponent
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.rememberPresenterState
import com.warpnet.warpnetandroid.model.HomeNavigationItem
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.navigation.rememberUserNavigationData
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.user.UserEvent
import com.warpnet.warpnetandroid.viewmodel.user.UserPresenter
import com.warpnet.warpnetandroid.viewmodel.user.UserState
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

class MeItem : HomeNavigationItem() {

  @Composable
  override fun name(): String = stringResource(com.warpnet.warpnetandroid.MR.strings.scene_profile_title)
  override val route: String
    get() = Root.Me

  @Composable
  override fun icon(): Painter = painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_user)

  override val withAppBar: Boolean
    get() = false

  @Composable
  override fun Content(navigator: Navigator) {
    MeSceneContent(navigator)
  }
}

@NavGraphDestination(
  route = Root.Me,
)
@Composable
fun MeScene(
  navigator: Navigator,
) {
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_profile_title))
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
      MeSceneContent(navigator = navigator)
    }
  }
}

@Composable
fun MeSceneContent(
  navigator: Navigator
) {
  val account = LocalActiveAccount.current
  account?.toUi()?.let { user ->
    val (state, channel) = key(user.userKey) {
      rememberPresenterState<UserState, UserEvent> {
        UserPresenter(it, userKey = user.userKey)
      }
    }
    val userNavigationData = rememberUserNavigationData(navigator)
    UserComponent(
      userKey = user.userKey,
      state = state,
      channel = channel,
      userNavigationData = userNavigationData,
    )
  }
}

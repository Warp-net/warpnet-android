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
package com.warpnet.warpnet-android.scenes.settings.accountNotification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.ColoredSwitch
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.navigation.openLink
import com.warpnet.warpnet-android.component.status.UserName
import com.warpnet.warpnet-android.component.status.UserScreenName
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.extensions.rememberPresenterState
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.ui.WarpnetScene
import io.github.seiko.precompose.annotation.NavGraphDestination
import io.github.seiko.precompose.annotation.Path
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Settings.AccountNotification.route,
)
@Composable
fun AccountNotificationScene(
  @Path("accountKey") accountKey: String,
  navigator: Navigator,
) {
  AccountNotificationScene(
    accountKey = MicroBlogKey.valueOf(accountKey),
    navigator = navigator,
  )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountNotificationScene(
  accountKey: MicroBlogKey,
  navigator: Navigator,
) {
  val (state, channel) = rememberPresenterState { AccountNotificationPresenter(accountKey, it) }
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          title = {
            Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_settings_notification_title))
          },
          navigationIcon = {
            AppBarNavigationButton(
              onBack = {
                navigator.popBackStack()
              }
            )
          },
        )
      },
    ) {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
      ) {
        state.user?.let {
          ListItem(
            modifier = Modifier.clickable {
              channel.trySend(AccountNotificationEvent.SetIsNotificationEnabled(!state.isNotificationEnabled))
            },
            text = {
              UserName(
                user = it,
                onUserNameClicked = {
                  navigator.openLink(it)
                }
              )
            },
            secondaryText = {
              UserScreenName(user = it)
            },
            trailing = {
              ColoredSwitch(
                checked = state.isNotificationEnabled,
                onCheckedChange = {
                  channel.trySend(AccountNotificationEvent.SetIsNotificationEnabled(it))
                },
              )
            }
          )
        }
        AccountNotificationChannelDetail(
          enabled = state.isNotificationEnabled,
          accountKey = accountKey,
        )
      }
    }
  }
}

@Composable
expect fun AccountNotificationChannelDetail(
  enabled: Boolean,
  accountKey: MicroBlogKey,
)

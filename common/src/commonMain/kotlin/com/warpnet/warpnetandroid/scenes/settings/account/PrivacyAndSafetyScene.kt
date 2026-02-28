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
package com.warpnet.warpnetandroid.scenes.settings.account

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.warpnet.warpnetandroid.MR
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.lazy.ItemHeader
import com.warpnet.warpnetandroid.component.settings.switchItem
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.rememberPresenterState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.WarpnetScene
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Settings.PrivacyAndSafety,
)
@Composable
fun PrivacyAndSafetyScene(
  navigator: Navigator,
) {
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          title = {
            Text(text = stringResource(res = MR.strings.scene_settings_privacy_and_safety_title))
          },
          navigationIcon = {
            AppBarNavigationButton {
              navigator.popBackStack()
            }
          },
        )
      },
    ) {
      val (state, channel) = rememberPresenterState { PrivacyAndSafetyPresenter(it) }
      Column {
        ItemHeader {
          Text(text = stringResource(res = MR.strings.scene_settings_privacy_and_safety_section_header_sensitive))
        }
        switchItem(
          value = state.account.isAlwaysShowSensitiveMedia,
          onChanged = {
            channel.trySend(PrivacyAndSafetyEvent.SetIsAlwaysShowSensitiveMedia(it))
          },
          title = {
            Text(text = stringResource(res = MR.strings.scene_settings_privacy_and_safety_always_show_sensitive_media))
          },
        )
      }
    }
  }
}

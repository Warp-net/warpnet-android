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
package com.warpnet.warpnetandroid.scenes.settings.storage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.Dialog
import com.warpnet.warpnetandroid.component.foundation.DialogProperties
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.rememberPresenterState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.WarpnetScene
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Settings.Storage,
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StorageScene(
  navigator: Navigator,
) {
  val (state, channel) = rememberPresenterState { StoragePresenter(it) }
  if (state.loading) {
    Dialog(
      onDismissRequest = { },
      properties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
      )
    ) {
      CircularProgressIndicator()
    }
  }

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
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_storage_title))
          }
        )
      }
    ) {
      Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
      ) {
        ListItem(
          modifier = Modifier
            .clickable {
              channel.trySend(StorageEvent.ClearSearchHistory)
            },
        ) {
          Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_storage_search_title))
        }
        ListItem(
          modifier = Modifier
            .clickable {
              channel.trySend(StorageEvent.ClearImageCache)
            },
          text = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_storage_media_title))
          },
          secondaryText = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_storage_media_sub_title))
          },
        )
        ListItem(
          modifier = Modifier
            .clickable {
              channel.trySend(StorageEvent.ClearAllCaches)
            },
          text = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_storage_all_title), color = Color.Red)
          },
          secondaryText = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_storage_all_sub_title))
          },
        )
      }
    }
  }
}

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
package com.warpnet.warpnet-android.scenes.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.dataprovider.mapper.Strings
import com.warpnet.warpnet-android.extensions.rememberPresenterState
import com.warpnet.warpnet-android.icon.switcher.IconSelectorDialog
import com.warpnet.warpnet-android.icon.switcher.IconSwitcher
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.scenes.settings.display.DisplayEvent
import com.warpnet.warpnet-android.scenes.settings.display.DisplayPresenter
import com.warpnet.warpnet-android.ui.WarpnetScene
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.StringResource
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

data class SettingItem(
  val name: StringResource,
  val icon: FileResource,
  val route: String,
)

private val settings =
  mapOf(
    com.warpnet.warpnet-android.MR.strings.scene_settings_section_header_account to listOf(
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_privacy_and_safety_title,
        com.warpnet.warpnet-android.MR.files.ic_privacy_and_safety,
        route = Root.Settings.PrivacyAndSafety,
      )
    ),
    com.warpnet.warpnet-android.MR.strings.scene_settings_section_header_general to listOf(
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_appearance_title,
        com.warpnet.warpnet-android.MR.files.ic_shirt,
        route = Root.Settings.Appearance,
      ),
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_display_title,
        com.warpnet.warpnet-android.MR.files.ic_template,
        route = Root.Settings.Display,
      ),
      SettingItem(
        Strings.scene_settings_swipe_gestures_tittle,
        com.warpnet.warpnet-android.MR.files.ic_swipe,
        route = Root.Settings.Swipe,
      ),
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_layout_title,
        com.warpnet.warpnet-android.MR.files.ic_layout_sidebar,
        route = Root.Settings.Layout,
      ),
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_notification_title,
        com.warpnet.warpnet-android.MR.files.ic_settings_notification,
        route = Root.Settings.Notification,
      ),
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_storage_title,
        com.warpnet.warpnet-android.MR.files.ic_database,
        route = Root.Settings.Storage,
      ),
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_misc_title,
        com.warpnet.warpnet-android.MR.files.ic_triangle_square_circle,
        route = Root.Settings.Misc,
      ),
    ),
    com.warpnet.warpnet-android.MR.strings.scene_settings_section_header_about to listOf(
      SettingItem(
        com.warpnet.warpnet-android.MR.strings.scene_settings_about_title,
        com.warpnet.warpnet-android.MR.files.ic_info_circle,
        route = Root.Settings.About,
      ),
    )
  )

@NavGraphDestination(
  route = Root.Settings.Home,
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsScene(
  navigator: Navigator,
) {
  var showIconSelector by remember {
    mutableStateOf(false)
  }

  val (state, channel) = rememberPresenterState { DisplayPresenter(it) }

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
            Text(text = stringResource(com.warpnet.warpnet-android.MR.strings.scene_settings_title))
          }
        )
      },
      bottomBar = {
      }
    ) {
      LazyColumn(
        contentPadding = it
      ) {
        settings.forEach { items ->
          item {
            ListItem(
              text = {
                ProvideTextStyle(value = MaterialTheme.typography.button) {
                  Text(text = stringResource(items.key))
                }
              },
            )
          }
          items(items.value) { item ->
            SettingItem(item) {
              navigator.navigate(item.route)
            }
          }
        }
        item {
          IconSwitcher(appIcon = state.display.appIcon) {
            showIconSelector = true
          }
        }
      }
    }
    IconSelectorDialog(
      show = showIconSelector,
      onDismissRequest = {
        showIconSelector = false
      },
      onIconSelect = {
        channel.trySend(DisplayEvent.SetAppIcon(it))
      }
    )
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SettingItem(
  item: SettingItem,
  onClick: () -> Unit,
) {
  ListItem(
    modifier = Modifier.clickable(
      onClick = {
        if (item.route.isNotEmpty()) {
          onClick.invoke()
        }
      }
    ),
    icon = {
      Icon(
        painter = painterResource(item.icon),
        contentDescription = stringResource(item.name),
        modifier = Modifier.size(24.dp),
      )
    },
    text = {
      Text(text = stringResource(item.name))
    },
  )
}

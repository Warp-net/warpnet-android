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
package com.warpnet.warpnetandroid.scenes.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.DropdownMenu
import com.warpnet.warpnetandroid.component.foundation.DropdownMenuItem
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.navigation.openLink
import com.warpnet.warpnetandroid.component.navigation.user
import com.warpnet.warpnetandroid.component.status.UserAvatar
import com.warpnet.warpnetandroid.component.status.UserName
import com.warpnet.warpnetandroid.component.status.UserScreenName
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.LocalActiveAccountViewModel
import com.warpnet.warpnetandroid.ui.WarpnetScene
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@NavGraphDestination(
  route = Root.Settings.AccountManagement,
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AccountManagementScene(
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
            Text(
              text = stringResource(
                res = com.warpnet.warpnetandroid.MR.strings.scene_manage_accounts_title
              )
            )
          },
          actions = {
            IconButton(
              onClick = {
                navigator.navigate(Root.SignIn.General)
              }
            ) {
              Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(
                  res = com.warpnet.warpnetandroid.MR.strings.accessibility_scene_manage_accounts_add
                )
              )
            }
          }
        )
      }
    ) {
      val activeAccountViewModel = LocalActiveAccountViewModel.current
      val accounts by activeAccountViewModel.allAccounts.observeAsState(initial = emptyList())
      LazyColumn {
        items(items = accounts) { detail ->
          detail.toUi().let {
            ListItem(
              icon = {
                UserAvatar(
                  user = it,
                  withPlatformIcon = true,
                  onClick = {
                    navigator.user(it)
                  }
                )
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
                var expanded by remember { mutableStateOf(false) }
                Box {
                  IconButton(
                    onClick = {
                      expanded = true
                    },
                  ) {
                    Icon(
                      imageVector = Icons.Default.MoreVert,
                      contentDescription = stringResource(
                        res = com.warpnet.warpnetandroid.MR.strings.accessibility_common_more
                      )
                    )
                  }
                  DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                  ) {
                    DropdownMenuItem(
                      onClick = {
                        activeAccountViewModel.deleteAccount(detail)
                        if (!activeAccountViewModel.hasAccount()) {
                          navigator.navigate(
                            Root.SignIn.General,
                            NavOptions(
                              popUpTo = PopUpTo(
                                route = Root.Empty,
                                inclusive = true,
                              )
                            )
                          )
                        }
                      },
                    ) {
                      Text(
                        text = stringResource(
                          res = com.warpnet.warpnetandroid.MR.strings.common_controls_actions_remove
                        ),
                        color = Color.Red,
                      )
                    }
                  }
                }
              }
            )
          }
        }
      }
    }
  }
}

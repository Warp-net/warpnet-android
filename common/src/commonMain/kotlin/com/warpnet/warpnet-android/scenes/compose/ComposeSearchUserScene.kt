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
package com.warpnet.warpnet-android.scenes.compose

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.foundation.TextInput
import com.warpnet.warpnet-android.component.lazy.loadState
import com.warpnet.warpnet-android.component.lazy.ui.LazyUiUserList
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.di.ext.getViewModel
import com.warpnet.warpnet-android.extensions.observeAsState
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.navigation.rememberUserNavigationData
import com.warpnet.warpnet-android.ui.LocalActiveAccount
import com.warpnet.warpnet-android.ui.WarpnetScene
import com.warpnet.warpnet-android.viewmodel.compose.ComposeSearchUserViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Compose.Search.User,
)
@Composable
fun ComposeSearchUserScene(
  navigator: Navigator,
) {
  val account = LocalActiveAccount.current ?: return
  val viewModel: ComposeSearchUserViewModel = getViewModel()
  val text by viewModel.text.observeAsState(initial = "")
  val source = viewModel.source.collectAsLazyPagingItems()
  val userNavigationData = rememberUserNavigationData(navigator)
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          title = {
            ProvideTextStyle(value = MaterialTheme.typography.body1) {
              TextInput(
                value = text,
                onValueChange = {
                  viewModel.text.value = it
                },
                maxLines = 1,
                placeholder = {
                  Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_compose_user_search_search_placeholder))
                },
                autoFocus = true,
                alignment = Alignment.CenterStart,
                keyboardActions = KeyboardActions(
                  onDone = {
                    navigator.goBackWith("@$text")
                  }
                ),
                keyboardOptions = KeyboardOptions(
                  imeAction = ImeAction.Done,
                )
              )
            }
          },
          navigationIcon = {
            AppBarNavigationButton(
              onBack = {
                navigator.popBackStack()
              }
            )
          },
          actions = {
            IconButton(
              onClick = {
                navigator.goBackWith("@$text")
              }
            ) {
              Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(
                  res = com.warpnet.warpnet-android.MR.strings.accessibility_common_done
                )
              )
            }
          },
        )
      }
    ) {
      LazyUiUserList(
        items = source,
        userNavigationData = userNavigationData,
        onItemClicked = {
          val displayName = it.getDisplayScreenName(account.accountKey.host)
          navigator.goBackWith(displayName)
        },
        header = {
          loadState(source.loadState.refresh)
        }
      )
    }
  }
}

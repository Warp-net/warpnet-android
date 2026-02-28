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
package com.warpnet.warpnetandroid.scenes.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.foundation.TextInput
import com.warpnet.warpnetandroid.component.lazy.loadState
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.compose.MastodonComposeSearchHashtagViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Mastodon.Compose.Hashtag,
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ComposeSearchHashtagScene(
  navigator: Navigator,
) {
  val viewModel: MastodonComposeSearchHashtagViewModel = getViewModel()
  val text by viewModel.text.observeAsState(initial = "")
  val source = viewModel.source.collectAsLazyPagingItems()
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
                  Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_compose_hashtag_search_search_placeholder))
                },
                autoFocus = true,
                alignment = Alignment.CenterStart,
                keyboardActions = KeyboardActions(
                  onDone = {
                    navigator.goBackWith("#$text")
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
                navigator.goBackWith("#$text")
              }
            ) {
              Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(
                  res = com.warpnet.warpnetandroid.MR.strings.accessibility_common_done
                )
              )
            }
          },
        )
      }
    ) {
      LazyColumn {
        loadState(source.loadState.refresh)
        items(source) {
          it?.name?.let { name ->
            ListItem(
              modifier = Modifier
                .clickable {
                  navigator.goBackWith("#$name")
                }
            ) {
              Text(text = name)
            }
          }
        }
      }
    }
  }
}

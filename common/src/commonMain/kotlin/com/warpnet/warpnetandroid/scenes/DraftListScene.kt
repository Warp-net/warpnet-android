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
package com.warpnet.warpnetandroid.scenes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.warpnet.warpnetandroid.component.lazy.LazyListController
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.DraftViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Draft.List,
)
@Composable
fun DraftListScene(
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
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_drafts_title))
          }
        )
      }
    ) {
      DraftListSceneContent(navigator = navigator)
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DraftListSceneContent(
  navigator: Navigator,
  lazyListController: LazyListController? = null,
) {
  val viewModel: DraftViewModel = getViewModel()
  val source by viewModel.source.observeAsState(initial = emptyList())
  val listState = rememberLazyListState()
  LaunchedEffect(lazyListController) {
    lazyListController?.listState = listState
  }
  LazyColumn(
    state = listState
  ) {
    items(items = source, key = { it.draftId.hashCode() }) {
      ListItem(
        text = {
          Text(text = it.content)
        },
        trailing = {
          var expanded by remember { mutableStateOf(false) }
          Box {
            IconButton(onClick = { expanded = true }) {
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
                  navigator.navigate(Root.Draft.Compose(it.draftId))
                }
              ) {
                Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_drafts_actions_edit_draft))
              }
              DropdownMenuItem(
                onClick = {
                  viewModel.delete(it)
                }
              ) {
                Text(
                  text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.common_controls_actions_remove),
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

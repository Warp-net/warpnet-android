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
package com.warpnet.warpnetandroid.scenes.lists.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.Dialog
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.foundation.LoadingProgress
import com.warpnet.warpnetandroid.component.lists.WarpnetListsModifyComponent
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.lists.ListsCreateViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@NavGraphDestination(
  route = Root.Lists.WarpnetCreate,
)
@Composable
fun WarpnetListsCreateScene(
  navigator: Navigator,
) {
  val scope = rememberCoroutineScope()
  val listsCreateViewModel: ListsCreateViewModel = getViewModel()
  val loading by listsCreateViewModel.loading.observeAsState(initial = false)

  WarpnetScene {
    var name by remember {
      mutableStateOf("")
    }
    var desc by remember {
      mutableStateOf("")
    }
    var isPrivate by remember {
      mutableStateOf(false)
    }
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          navigationIcon = {
            AppBarNavigationButton(
              Icons.Default.Close,
              onBack = {
                navigator.popBackStack()
              }
            )
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_lists_modify_create_title))
          },
          actions = {
            IconButton(
              enabled = name.isNotEmpty(),
              onClick = {
                scope.launch {
                  listsCreateViewModel.createList(
                    title = name,
                    description = desc,
                    private = isPrivate
                  )?.let {
                    navigator.navigate(
                      Root.Lists.Timeline(it.listKey),
                      options = NavOptions(
                        popUpTo = PopUpTo(Root.Lists.Home)
                      )
                    )
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(res = com.warpnet.warpnetandroid.MR.strings.common_controls_actions_confirm),
                tint = if (name.isNotEmpty()) MaterialTheme.colors.primary else LocalContentColor.current.copy(
                  alpha = LocalContentAlpha.current
                )
              )
            }
          }
        )
      }
    ) {
      Box {
        WarpnetListsModifyComponent(
          name = name,
          desc = desc,
          isPrivate = isPrivate,
          onNameChanged = { name = it },
          onDescChanged = { desc = it },
        ) {
          isPrivate = it
        }
        if (loading) {
          Dialog(onDismissRequest = { }) {
            LoadingProgress()
          }
        }
      }
    }
  }
}

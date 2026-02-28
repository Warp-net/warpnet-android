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
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.Dialog
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.foundation.LoadingProgress
import com.warpnet.warpnetandroid.component.lists.WarpnetListsModifyComponent
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.lists.ListsModifyViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import io.github.seiko.precompose.annotation.Path
import moe.tlaster.precompose.navigation.Navigator
import org.koin.core.parameter.parametersOf

@NavGraphDestination(
  route = Root.Lists.WarpnetEdit.route,
)
@Composable
fun WarpnetListsEditScene(
  @Path("listKey") listKey: String,
  navigator: Navigator,
) {
  WarpnetListsEditScene(
    listKey = MicroBlogKey.valueOf(listKey),
    navigator = navigator,
  )
}

@Composable
fun WarpnetListsEditScene(
  listKey: MicroBlogKey,
  navigator: Navigator,
) {
  val listsEditViewModel: ListsModifyViewModel = getViewModel {
    parametersOf(listKey)
  }
  val loading by listsEditViewModel.loading.observeAsState(initial = false)
  val source by listsEditViewModel.source.observeAsState(null)
  source?.let { uiList ->
    WarpnetScene {
      val name by listsEditViewModel.editName.observeAsState(uiList.title)
      val desc by listsEditViewModel.editDesc.observeAsState(uiList.descriptions)
      val isPrivate by listsEditViewModel.editPrivate.observeAsState(uiList.isPrivate)
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
              Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_lists_modify_edit_title))
            },
            actions = {
              IconButton(
                enabled = name.isNotEmpty(),
                onClick = {
                  listsEditViewModel.editList(
                    listKey.id,
                    title = name,
                    description = desc,
                    private = isPrivate
                  ) { success, _ ->
                    if (success) navigator.popBackStack()
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
            onNameChanged = { listsEditViewModel.editName.value = it },
            onDescChanged = { listsEditViewModel.editDesc.value = it },
            onPrivateChanged = { listsEditViewModel.editPrivate.value = it }
          )
          if (loading) {
            Dialog(onDismissRequest = { }) {
              LoadingProgress()
            }
          }
        }
      }
    }
  }
}

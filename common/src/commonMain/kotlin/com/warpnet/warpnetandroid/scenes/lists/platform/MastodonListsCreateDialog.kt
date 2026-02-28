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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.warpnet.warpnetandroid.component.foundation.Dialog
import com.warpnet.warpnetandroid.component.foundation.LoadingProgress
import com.warpnet.warpnetandroid.component.lists.MastodonListsModifyComponent
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.viewmodel.lists.ListsCreateViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Lists.MastodonCreateDialog,
  functionName = "dialog"
)
@Composable
fun MastodonListsCreateDialog(
  navigator: Navigator,
) {
  val scope = rememberCoroutineScope()
  var showMastodonComponent by remember {
    mutableStateOf(true)
  }
  val dismiss = {
    navigator.goBack()
    showMastodonComponent = true
  }
  var name by remember {
    mutableStateOf("")
  }
  val listsCreateViewModel: ListsCreateViewModel = getViewModel()
  val loading by listsCreateViewModel.loading.observeAsState(initial = false)

  if (loading) {
    Dialog(
      onDismissRequest = {
        dismiss()
      }
    ) {
      LoadingProgress()
    }
    return
  }

  if (showMastodonComponent) {
    MastodonListsModifyComponent(
      onDismissRequest = { dismiss() },
      title = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_lists_modify_dialog_create),
      name = name,
      onNameChanged = { name = it }
    ) {
      scope.launch {
        val result = listsCreateViewModel.createList(
          title = it
        )
        dismiss()
        if (result != null) {
          navigator.navigate(
            Root.Lists.Timeline(result.listKey),
          )
        }
      }
    }
  }
}

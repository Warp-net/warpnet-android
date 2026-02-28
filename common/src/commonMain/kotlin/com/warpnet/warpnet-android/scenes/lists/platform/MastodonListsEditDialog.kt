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
package com.warpnet.warpnet-android.scenes.lists.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.warpnet.warpnet-android.component.foundation.Dialog
import com.warpnet.warpnet-android.component.foundation.LoadingProgress
import com.warpnet.warpnet-android.component.lists.MastodonListsModifyComponent
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.di.ext.getViewModel
import com.warpnet.warpnet-android.extensions.observeAsState
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.viewmodel.lists.ListsModifyViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MastodonListsEditDialog(listKey: MicroBlogKey, onDismissRequest: () -> Unit) {
  var showMastodonComponent by remember {
    mutableStateOf(true)
  }
  val dismiss = {
    onDismissRequest.invoke()
    showMastodonComponent = true
  }
  val listsEditViewModel: ListsModifyViewModel = getViewModel {
    parametersOf(listKey)
  }
  val source by listsEditViewModel.source.observeAsState(initial = null)
  val loading by listsEditViewModel.loading.observeAsState(initial = false)
  source?.let { uiList ->
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
      var name by remember {
        mutableStateOf(uiList.title)
      }
      MastodonListsModifyComponent(
        onDismissRequest = { dismiss() },
        title = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_lists_modify_dialog_edit),
        name = name,
        onNameChanged = { name = it }
      ) {
        listsEditViewModel.editList(
          listId = listKey.id,
          title = it
        ) { success, _ ->
          if (success) onDismissRequest.invoke()
        }
      }
    }
  }
}

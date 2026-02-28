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
package com.warpnet.warpnet-android.scenes.dm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.foundation.TextInput
import com.warpnet.warpnet-android.component.lazy.ui.LazyUiUserList
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.di.ext.getViewModel
import com.warpnet.warpnet-android.extensions.observeAsState
import com.warpnet.warpnet-android.model.ui.UiUser
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.navigation.UserNavigationData
import com.warpnet.warpnet-android.navigation.rememberUserNavigationData
import com.warpnet.warpnet-android.ui.WarpnetScene
import com.warpnet.warpnet-android.viewmodel.dm.DMNewConversationViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.PopUpTo

@NavGraphDestination(
  route = Root.Messages.NewConversation,
)
@Composable
fun DMNewConversationScene(
  navigator: Navigator,
) {
  val viewModel: DMNewConversationViewModel = getViewModel()
  val keyWord by viewModel.input.observeAsState("")
  val source = viewModel.sourceFlow.collectAsLazyPagingItems()
  val userNavigationData = rememberUserNavigationData(navigator)
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        Column {
          AppBar(
            navigationIcon = {
              AppBarNavigationButton(
                icon = Icons.Default.Close,
                onBack = {
                  navigator.popBackStack()
                }
              )
            },
            title = {
              Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_messages_new_conversation_title))
            },
            elevation = 0.dp
          )
          SearchInput(
            modifier = Modifier.fillMaxWidth(),
            input = keyWord,
            onValueChanged = { viewModel.input.value = it },
          )
          Divider()
        }
      },
    ) {
      SearchResult(
        source,
        onItemClick = { user ->
          viewModel.createNewConversation(
            user,
            onResult = { key ->
              key?.let {
                navigator.navigate(
                  Root.Messages.Conversation(it),
                  NavOptions(popUpTo = PopUpTo(Root.Messages.Home))
                )
              }
            }
          )
        },
        userNavigationData = userNavigationData,
      )
    }
  }
}

@Composable
fun SearchInput(
  input: String,
  onValueChanged: (value: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.padding(SearchInputDefaults.ContentPadding)
  ) {
    Icon(
      painter = painterResource(res = com.warpnet.warpnet-android.MR.files.ic_search),
      contentDescription = stringResource(
        res = com.warpnet.warpnet-android.MR.strings.scene_search_title
      ),
      modifier = Modifier.size(24.dp),
    )
    Spacer(modifier = Modifier.width(SearchInputDefaults.ContentSpacing))
    TextInput(
      value = input,
      onValueChange = onValueChanged,
      modifier = Modifier.weight(1f),
      placeholder = {
        Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_messages_new_conversation_search))
      },
      maxLines = 1
    )
  }
}

private object SearchInputDefaults {
  val ContentPadding = PaddingValues(16.dp)
  val ContentSpacing = 16.dp
}

@Composable
fun SearchResult(
  source: LazyPagingItems<UiUser>,
  userNavigationData: UserNavigationData,
  onItemClick: (user: UiUser) -> Unit,
) {
  LazyUiUserList(
    items = source,
    onItemClicked = onItemClick,
    userNavigationData = userNavigationData,
    modifier = Modifier.fillMaxSize()
  )
}

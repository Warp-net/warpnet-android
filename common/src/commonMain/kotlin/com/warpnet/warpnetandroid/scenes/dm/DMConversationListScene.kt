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
package com.warpnet.warpnetandroid.scenes.dm

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnetandroid.component.lazy.LazyListController
import com.warpnet.warpnetandroid.component.lazy.ui.LazyUiDMConversationList
import com.warpnet.warpnetandroid.component.navigation.openLink
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.dm.DMConversationViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Messages.Home,
)
@Composable
fun DMConversationListScene(
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
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_messages_title))
          },
        )
      },
      floatingActionButton = {
        DMConversationListSceneFab(navigator)
      },
    ) {
      DMConversationListSceneContent(
        navigator = navigator,
      )
    }
  }
}

@Composable
fun DMConversationListSceneFab(
  navigator: Navigator,
) {
  FloatingActionButton(
    onClick = {
      navigator.navigate(Root.Messages.NewConversation)
    }
  ) {
    Icon(
      painter = painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_add),
      contentDescription = stringResource(
        res = com.warpnet.warpnetandroid.MR.strings.scene_lists_icons_create
      ),
      modifier = Modifier.size(24.dp),
    )
  }
}

@Composable
fun DMConversationListSceneContent(
  navigator: Navigator,
  lazyListController: LazyListController? = null,
) {
  val account = LocalActiveAccount.current ?: return
  if (!account.supportDirectMessage) return
  val viewModel: DMConversationViewModel = getViewModel()
  val source = viewModel.source.collectAsLazyPagingItems()
  val listState = rememberLazyListState()
  LaunchedEffect(lazyListController) {
    lazyListController?.listState = listState
  }
  SwipeToRefreshLayout(
    refreshingState = source.loadState.refresh is LoadState.Loading,
    onRefresh = { source.refresh() }
  ) {
    LazyUiDMConversationList(
      items = source,
      state = listState,
      onItemClicked = {
        navigator.navigate(Root.Messages.Conversation(it.conversation.conversationKey))
      },
      openLink = {
        navigator.openLink(it)
      }
    )
  }
}

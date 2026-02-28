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
package com.warpnet.warpnet-android.scenes.lists

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnet-android.component.lazy.ui.LazyUiListsList
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.di.ext.getViewModel
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.ui.LocalActiveAccount
import com.warpnet.warpnet-android.ui.WarpnetScene
import com.warpnet.warpnet-android.viewmodel.lists.ListsViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator
import java.util.Locale

@NavGraphDestination(
  route = Root.Lists.Home,
)
@Composable
fun ListsScene(
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
            Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_lists_title))
          },
        )
      },
      floatingActionButton = {
        ListsSceneFab(navigator)
      },
      floatingActionButtonPosition = FabPosition.Center
    ) {
      ListsSceneContent(
        navigator,
      )
    }
  }
}

@Composable
fun ListsSceneFab(
  navigator: Navigator,
) {
  val account = LocalActiveAccount.current ?: return
  FloatingActionButton(
    onClick = {
      when (account.type) {
        PlatformType.Warpnet -> navigator.navigate(Root.Lists.WarpnetCreate)
        PlatformType.StatusNet -> TODO()
        PlatformType.Fanfou -> TODO()
        PlatformType.Mastodon -> navigator.navigate(Root.Lists.MastodonCreateDialog)
      }
    }
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(ListsSceneDefaults.Fab.ContentPadding)
    ) {
      Icon(
        painter = painterResource(res = com.warpnet.warpnet-android.MR.files.ic_add),
        contentDescription = stringResource(
          res = com.warpnet.warpnet-android.MR.strings.scene_lists_icons_create
        ),
        modifier = Modifier.padding(ListsSceneDefaults.Fab.IconPadding).size(24.dp),
      )
      Text(
        text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_lists_modify_create_title)
          .uppercase(Locale.getDefault()),
        style = MaterialTheme.typography.button
      )
    }
  }
}

@Composable
fun ListsSceneContent(
  navigator: Navigator
) {
  val account = LocalActiveAccount.current ?: return
  // if list type is all , display title of each type
  val listsViewMode: ListsViewModel = getViewModel()
  val ownerItems = listsViewMode.ownerSource.collectAsLazyPagingItems()
  val subscribeItems = listsViewMode.subscribedSource.collectAsLazyPagingItems()
  val sourceItems = listsViewMode.source.collectAsLazyPagingItems()
  SwipeToRefreshLayout(
    refreshingState = ownerItems.loadState.refresh is LoadState.Loading,
    onRefresh = { ownerItems.refresh() }
  ) {
    LazyUiListsList(
      listType = account.listType,
      source = sourceItems,
      ownerItems = ownerItems,
      subscribedItems = subscribeItems,
      onItemClicked = {
        navigator.navigate(Root.Lists.Timeline(it.listKey))
      }
    )
  }
}

private object ListsSceneDefaults {
  object Fab {
    val ContentPadding = PaddingValues(horizontal = 22.dp)
    val IconPadding = PaddingValues(end = 17.dp)
  }
}

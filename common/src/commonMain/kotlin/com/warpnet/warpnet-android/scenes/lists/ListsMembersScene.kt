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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.DropdownMenu
import com.warpnet.warpnet-android.component.foundation.DropdownMenuItem
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnet-android.component.lazy.ui.LazyUiUserList
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.extensions.refreshOrRetry
import com.warpnet.warpnet-android.extensions.rememberPresenterState
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.navigation.rememberUserNavigationData
import com.warpnet.warpnet-android.ui.WarpnetScene
import com.warpnet.warpnet-android.viewmodel.user.UserListEvent
import com.warpnet.warpnet-android.viewmodel.user.UserListPresenter
import com.warpnet.warpnet-android.viewmodel.user.UserListState
import com.warpnet.warpnet-android.viewmodel.user.UserListType
import io.github.seiko.precompose.annotation.NavGraphDestination
import io.github.seiko.precompose.annotation.Path
import io.github.seiko.precompose.annotation.Query
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator
import java.util.Locale

@NavGraphDestination(
  route = Root.Lists.Members.route,
)
@Composable
fun ListsMembersScene(
  @Path("listKey") listKey: String,
  @Query("owned") owned: Boolean?,
  navigator: Navigator,
) {
  ListsMembersScene(
    listKey = MicroBlogKey.valueOf(listKey),
    owned = owned ?: false,
    navigator = navigator,
  )
}

@Composable
fun ListsMembersScene(
  listKey: MicroBlogKey,
  owned: Boolean,
  navigator: Navigator,
) {
  val (state, channel) = rememberPresenterState<UserListState, UserListEvent> {
    UserListPresenter(it, userType = UserListType.ListUsers(listId = listKey.id))
  }
  if (state !is UserListState.Data) {
    return
  }
  val userNavigationData = rememberUserNavigationData(navigator)
  val scope = rememberCoroutineScope()
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
            Text(text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_lists_details_tabs_members))
          }
        )
      },
      floatingActionButton = {
        if (owned) FloatingActionButton(
          onClick = {
            scope.launch {
              val result =
                navigator.navigateForResult(Root.Lists.AddMembers(listKey = listKey)) as? List<*>?
              if (result != null && result.isNotEmpty()) state.source.refresh()
            }
          }
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(ListsMembersSceneDefaults.Fab.ContentPadding)
          ) {
            Icon(
              painter = painterResource(res = com.warpnet.warpnet-android.MR.files.ic_add),
              contentDescription = stringResource(
                res = com.warpnet.warpnet-android.MR.strings.scene_lists_details_add_members
              ),
              modifier = Modifier.padding(ListsMembersSceneDefaults.Fab.IconPadding).size(24.dp),
            )
            Text(
              text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_lists_users_add_title)
                .uppercase(Locale.getDefault()),
              style = MaterialTheme.typography.button
            )
          }
        }
      },
      floatingActionButtonPosition = FabPosition.Center
    ) {
      SwipeToRefreshLayout(
        refreshingState = state.source.loadState.refresh is LoadState.Loading,
        onRefresh = {
          state.source.refreshOrRetry()
        }
      ) {
        LazyUiUserList(
          items = state.source,
          onItemClicked = {
            // navigator.user(it)
          },
          action = {
            if (!owned) return@LazyUiUserList
            var menuExpand by remember {
              mutableStateOf(false)
            }
            IconButton(onClick = { menuExpand = !menuExpand }) {
              Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(
                  res = com.warpnet.warpnet-android.MR.strings.scene_lists_users_menu_actions_remove
                )
              )
            }
            DropdownMenu(
              expanded = menuExpand,
              onDismissRequest = { menuExpand = false }
            ) {
              DropdownMenuItem(
                onClick = {
                  channel.trySend(UserListEvent.RemoveMember(it))
                }
              ) {
                Text(
                  text = stringResource(
                    com.warpnet.warpnet-android.MR.strings.scene_lists_users_menu_actions_remove
                  )
                )
              }
            }
          },
          userNavigationData = userNavigationData,
        )
      }
    }
  }
}

private object ListsMembersSceneDefaults {
  object Fab {
    val ContentPadding = PaddingValues(horizontal = 22.dp)
    val IconPadding = PaddingValues(end = 17.dp)
  }
}

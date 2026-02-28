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
package com.warpnet.warpnet-android.scenes.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.warpnet.warpnet-android.MR
import com.warpnet.warpnet-android.component.UserComponent
import com.warpnet.warpnet-android.component.foundation.AlertDialog
import com.warpnet.warpnet-android.component.foundation.AppBar
import com.warpnet.warpnet-android.component.foundation.AppBarNavigationButton
import com.warpnet.warpnet-android.component.foundation.DropdownMenu
import com.warpnet.warpnet-android.component.foundation.DropdownMenuItem
import com.warpnet.warpnet-android.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.status.UserName
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.dataprovider.mapper.Files
import com.warpnet.warpnet-android.dataprovider.mapper.Strings
import com.warpnet.warpnet-android.di.ext.getViewModel
import com.warpnet.warpnet-android.extensions.rememberPresenterState
import com.warpnet.warpnet-android.extensions.withElevation
import com.warpnet.warpnet-android.model.AccountDetails
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.navigation.ProvideUserPlatform
import com.warpnet.warpnet-android.navigation.RequirePlatformAccount
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.navigation.RootDeepLinks
import com.warpnet.warpnet-android.navigation.rememberUserNavigationData
import com.warpnet.warpnet-android.ui.LocalActiveAccount
import com.warpnet.warpnet-android.ui.WarpnetScene
import com.warpnet.warpnet-android.viewmodel.dm.DMNewConversationViewModel
import com.warpnet.warpnet-android.viewmodel.user.UserEvent
import com.warpnet.warpnet-android.viewmodel.user.UserPresenter
import com.warpnet.warpnet-android.viewmodel.user.UserState
import io.github.seiko.precompose.annotation.NavGraphDestination
import io.github.seiko.precompose.annotation.Path
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.User.route,
  deepLink = [RootDeepLinks.User.route]
)
@Composable
fun UserScene(
  @Path("userKey") key: String,
  navigator: Navigator,
) {
  val account = LocalActiveAccount.current ?: return
  MicroBlogKey.valueOf(key).let { userKey ->
    ProvideUserPlatform(userKey = userKey) { platformType ->
      RequirePlatformAccount(platformType = platformType) {
        UserScene(
          userKey = userKey,
          navigator = navigator,
          account = account,
        )
      }
    }
  }
}

@Composable
fun UserScene(
  userKey: MicroBlogKey,
  navigator: Navigator,
  account: AccountDetails,
) {
  val (state, channel) = rememberPresenterState<UserState, UserEvent> {
    UserPresenter(it, userKey = userKey)
  }
  if (state !is UserState.Data) {
    return
  }
  val conversationViewModel: DMNewConversationViewModel = getViewModel()
  var expanded by remember { mutableStateOf(false) }
  var showBlockAlert by remember { mutableStateOf(false) }
  var showReportAlert by remember { mutableStateOf(false) }
  val userNavigationData = rememberUserNavigationData(navigator)
  WarpnetScene {
    InAppNotificationScaffold(
      // TODO: Show top bar with actions
      topBar = {
        AppBar(
          backgroundColor = MaterialTheme.colors.surface.withElevation(),
          navigationIcon = {
            AppBarNavigationButton(
              onBack = {
                navigator.popBackStack()
              }
            )
          },
          actions = {
            if (
              account.type == PlatformType.Warpnet &&
              state.user?.platformType == PlatformType.Warpnet &&
              userKey != account.accountKey
            ) {
              IconButton(
                onClick = {
                  conversationViewModel.createNewConversation(
                    state.user,
                    onResult = { conversationKey ->
                      conversationKey?.let {
                        navigator.navigate(Root.Messages.Conversation(it))
                      }
                    }
                  )
                }
              ) {
                Icon(
                  painter = painterResource(res = Files.ic_mail),
                  contentDescription = stringResource(
                    res = Strings.scene_messages_title
                  ),
                  tint = MaterialTheme.colors.onSurface,
                  modifier = Modifier.size(24.dp),
                )
              }
            }
            Box {
              if (userKey != account.accountKey) {
                IconButton(
                  onClick = {
                    expanded = true
                  }
                ) {
                  Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = stringResource(
                      res = MR.strings.accessibility_common_more
                    ),
                    tint = MaterialTheme.colors.onSurface
                  )
                }
              }

              DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
              ) {
                state.relationship.takeIf {
                  !state.loadingRelationship
                }?.blocking?.let { blocking ->
                  DropdownMenuItem(
                    onClick = {
                      if (blocking) {
                        channel.trySend(UserEvent.UnBlock)
                      } else {
                        showBlockAlert = true
                      }
                      expanded = false
                    }
                  ) {
                    Text(
                      text = stringResource(
                        res = if (blocking) {
                          Strings.common_controls_friendship_actions_unblock
                        } else {
                          Strings.common_controls_friendship_actions_block
                        }
                      )
                    )
                  }
                }
                DropdownMenuItem(onClick = {
                  showReportAlert = true
                  expanded = false
                }) {
                  Text(
                    text = stringResource(
                      res = Strings.common_controls_friendship_actions_report
                    )
                  )
                }
              }
            }
          },
          elevation = 0.dp,
          title = {
            state.user?.let {
              UserName(
                user = it,
                onUserNameClicked = userNavigationData.statusNavigation.openLink,
              )
            }
          }
        )
      }
    ) {
      Box {
        UserComponent(
          userKey = userKey,
          state = state,
          channel = channel,
          userNavigationData = userNavigationData,
        )
        if (showBlockAlert) {
          state.user?.let {
            UserActionAlert(
              screenName = stringResource(
                res = Strings.common_alerts_block_user_confirm_title,
                it.getDisplayScreenName(it.userKey.host)
              ),
              onDismissRequest = { showBlockAlert = false },
              onConfirm = {
                channel.trySend(UserEvent.Block)
              }
            )
          }
        }
        if (showReportAlert) {
          state.user?.let {
            val screen = it.getDisplayScreenName(it.userKey.host)
            val reportSuccessMessage = stringResource(
              res = Strings.common_alerts_report_user_success_title,
              screen,
            )
            val isMastodon = account.type == PlatformType.Mastodon
            UserActionAlert(
              screenName = stringResource(
                res = Strings.common_controls_friendship_do_you_want_to_report_user,
                screen
              ),
              showEdit = isMastodon,
              onDismissRequest = { showReportAlert = false },
              onConfirm = { reason ->
                channel.trySend(
                  UserEvent.Report(
                    scenes = if (isMastodon) null else listOf(screen),
                    reason = reason,
                    successMessage = reportSuccessMessage,
                  )
                )
              }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun UserActionAlert(
  screenName: String,
  showEdit: Boolean = false,
  onDismissRequest: () -> Unit,
  onConfirm: (String?) -> Unit,
) {
  var reportMessage: String? by remember {
    mutableStateOf(null)
  }
  AlertDialog(
    onDismissRequest = {
      onDismissRequest.invoke()
    },
    title = {
      Column {
        Text(
          text = screenName,
          style = MaterialTheme.typography.subtitle1
        )
        if (showEdit) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 32.dp)
              .background(MaterialTheme.colors.surface)
          ) {
            OutlinedTextField(
              value = reportMessage ?: "",
              onValueChange = {
                reportMessage = it
              },
              label = {
                Text(text = "Please input the report reason (Optional)")
              },
              modifier = Modifier.fillMaxWidth(),
            )
          }
        }
      }
    },
    dismissButton = {
      TextButton(
        onClick = {
          onDismissRequest.invoke()
        }
      ) {
        Text(text = stringResource(res = Strings.common_controls_actions_cancel))
      }
    },
    confirmButton = {
      TextButton(
        onClick = {
          onConfirm(reportMessage)
          onDismissRequest.invoke()
        }
      ) {
        Text(text = stringResource(res = Strings.common_controls_actions_yes))
      }
    },
  )
}

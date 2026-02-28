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
package com.warpnet.warpnetandroid.action

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.kmp.RemoteNavigator
import com.warpnet.warpnetandroid.model.AccountDetails
import com.warpnet.warpnetandroid.model.enums.ComposeType
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.navigation.StatusNavigationData
import com.warpnet.warpnetandroid.preferences.LocalSwipePreferences
import com.warpnet.warpnetandroid.preferences.model.SwipeActionType
import com.warpnet.warpnetandroid.preferences.model.toUi
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
private fun SmallIcon(
  modifier: Modifier = Modifier,
  type: SwipeActionType,
) {
  val icon = type.toUi().icon
  if (icon != null) {
    Image(
      modifier = modifier.padding(16.dp).size(24.dp),
      painter = painterResource(icon),
      contentDescription = null
    )
  }
}

fun triggerSwipe(
  statusNavigation: StatusNavigationData,
  actionsViewModel: IStatusActions,
  account: AccountDetails?,
  remoteNavigator: RemoteNavigator,
  status: UiStatus,
  type: SwipeActionType,
) {
  when (type) {
    SwipeActionType.None -> {}
    SwipeActionType.Reply -> {
      statusNavigation.composeNavigationData.compose(
        ComposeType.Reply,
        status.statusKey
      )
    }
    SwipeActionType.Repost -> {
      account?.let {
        actionsViewModel.retweet(status, it)
      }
    }
    SwipeActionType.Like -> {
      account?.let {
        actionsViewModel.like(status, it)
      }
    }
    SwipeActionType.Share -> {
      remoteNavigator.shareText(
        status.generateShareLink()
      )
    }
    SwipeActionType.Detail -> {
      statusNavigation.toStatus(status)
    }
  }
}

@Composable
fun SwipeActionContainer(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit,
  onActionTrigger: (SwipeActionType) -> Unit,
) {
  val swipeActions = LocalSwipePreferences.current
  if (!swipeActions.useSwipeGestures) {
    content.invoke()
    return
  }
  val leftShortAction = swipeActions.leftShort.action.let {
    SwipeAction(
      icon = {
        SmallIcon(type = it)
      },
      background = MaterialTheme.colors.primary,
      onSwipe = {
        onActionTrigger.invoke(it)
      },
      isUndo = false,
    )
  }
  val leftLongAction = swipeActions.leftLong.action.let {
    SwipeAction(
      icon = {
        SmallIcon(type = it)
      },
      background = MaterialTheme.colors.primary,
      onSwipe = {
        onActionTrigger.invoke(it)
      },
      isUndo = false,
    )
  }
  val rightShortAction = swipeActions.rightShort.action.let {
    SwipeAction(
      icon = {
        SmallIcon(type = it)
      },
      background = MaterialTheme.colors.primary,
      onSwipe = {
        onActionTrigger.invoke(it)
      },
      isUndo = false,
    )
  }
  val rightLongAction = swipeActions.rightLong.action.let {
    SwipeAction(
      icon = {
        SmallIcon(type = it)
      },
      background = MaterialTheme.colors.primary,
      onSwipe = {
        onActionTrigger.invoke(it)
      },
      isUndo = false,
    )
  }

  SwipeableActionsBox(
    modifier = modifier,
    startActions = listOf(leftShortAction, leftLongAction),
    endActions = listOf(rightShortAction, rightLongAction),
  ) {
    content()
  }
}

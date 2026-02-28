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
package com.warpnet.warpnetandroid.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.warpnet.warpnetandroid.component.navigation.media
import com.warpnet.warpnetandroid.component.navigation.openLink
import com.warpnet.warpnetandroid.component.navigation.status
import com.warpnet.warpnetandroid.component.navigation.user
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.enums.ComposeType
import com.warpnet.warpnetandroid.model.enums.MediaType
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.model.ui.UiUser
import moe.tlaster.precompose.navigation.Navigator

@Immutable
data class StatusNavigationData(
  val toUser: (UiUser) -> Unit = { },
  val toStatus: (UiStatus) -> Unit = { },
  val toMedia: (MicroBlogKey) -> Unit = { },
  val toMediaWithIndex: (MicroBlogKey, Int) -> Unit = { _: MicroBlogKey, _: Int -> },
  val openLink: (String) -> Unit = { },
  val composeNavigationData: ComposeNavigationData = ComposeNavigationData(),
  val popBackStack: () -> Unit = {},
  val navigateForResult: suspend (String) -> Any? = {},
  val navigate: (String) -> Unit = {},
)

@Composable
fun rememberStatusNavigationData(
  navigator: Navigator
): StatusNavigationData {
  val composeNavigationData = rememberComposeNavigationData(navigator)
  val statusNavigation by remember(navigator) {
    mutableStateOf(
      StatusNavigationData(
        composeNavigationData = composeNavigationData,
        toUser = {
          navigator.user(it)
        },
        toStatus = {
          navigator.status(it)
        },
        toMedia = {
          navigator.media(it)
        },
        toMediaWithIndex = { key, index ->
          navigator.media(key, index)
        },
        openLink = {
          navigator.openLink(it)
        },
        popBackStack = {
          navigator.popBackStack()
        },
        navigateForResult = {
          navigator.navigateForResult(it)
        },
        navigate = {
          navigator.navigate(it)
        }
      )
    )
  }
  return statusNavigation
}

@Immutable
data class UserNavigationData(
  val statusNavigation: StatusNavigationData = StatusNavigationData(),
  val showAvatar: (UiUser) -> Unit,
  val onUserBannerClick: (MediaType, String) -> Unit,
  val navigate: (String) -> Unit,
)

@Composable
fun rememberUserNavigationData(navigator: Navigator): UserNavigationData {
  val statusNavigation = rememberStatusNavigationData(navigator)
  val userNavigation = remember {
    UserNavigationData(
      statusNavigation = statusNavigation,
      showAvatar = {
        navigator.navigate(Root.Media.Raw(MediaType.photo, it.profileImage))
      },
      onUserBannerClick = { mediaType, url ->
        navigator.navigate(Root.Media.Raw(mediaType, url))
      },
      navigate = {
        navigator.navigate(it)
      }
    )
  }
  return userNavigation
}

@Immutable
data class ComposeNavigationData(
  val compose: (ComposeType, MicroBlogKey) -> Unit = { _, _ -> },
)

@Composable
fun rememberComposeNavigationData(navigator: Navigator): ComposeNavigationData {
  val composeNavigationData = remember {
    ComposeNavigationData(
      compose = { type, key ->
        navigator.navigate(Root.Compose.Home(type, key))
      }
    )
  }
  return composeNavigationData
}

@Immutable
data class DMNavigationData(
  val statusNavigation: StatusNavigationData = StatusNavigationData(),
)

@Composable
fun rememberDMNavigationData(
  navigator: Navigator,
): DMNavigationData {
  val statusNavigation = rememberStatusNavigationData(navigator)
  val dmNavigationData = remember {
    DMNavigationData(statusNavigation = statusNavigation)
  }
  return dmNavigationData
}

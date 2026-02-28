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
package com.warpnet.warpnetandroid.component.navigation

import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.kmp.RemoteNavigator
import com.warpnet.warpnetandroid.kmp.clearCookie
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.enums.ComposeType
import com.warpnet.warpnetandroid.model.enums.MastodonStatusType
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.model.enums.ReferenceType
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.navigation.warpnetXSchema
import com.warpnet.warpnetandroid.warpnetHosts
import moe.tlaster.precompose.navigation.NavOptions
import moe.tlaster.precompose.navigation.Navigator

suspend fun Navigator.warpnetSignInWeb(target: String): String {
  clearCookie()
  return navigateForResult(
    Root.SignIn.Web.Warpnet(target)
  ).toString()
}

fun Navigator.status(
  status: UiStatus,
  navOptions: NavOptions? = null
) {
  val statusKey = when (status.platformType) {
    PlatformType.Warpnet -> status.statusKey
    PlatformType.StatusNet -> TODO()
    PlatformType.Fanfou -> TODO()
    PlatformType.Mastodon -> {
      if (status.mastodonExtra != null) {
        when (status.mastodonExtra.type) {
          MastodonStatusType.Status -> status.statusKey
          MastodonStatusType.NotificationFollow, MastodonStatusType.NotificationFollowRequest -> null
          else -> status.referenceStatus[ReferenceType.MastodonNotification]?.statusKey
        }
      } else {
        status.statusKey
      }
    }
  }
  if (statusKey != null) {
    navigate(
      Root.Status(statusKey),
      navOptions
    )
  }
}

fun Navigator.media(
  statusKey: MicroBlogKey,
  selectedIndex: Int = 0,
  navOptions: NavOptions? = null,
) {
  navigate(Root.Media.Status(statusKey, selectedIndex), navOptions)
}

fun Navigator.searchInput(initial: String? = null) {
  navigate(
    Root.Search.Input(initial),
  )
}

fun Navigator.search(keyword: String) {
  navigate(Root.Search.Result(keyword))
}

fun Navigator.openLink(
  link: String,
  deepLink: Boolean = true,
  remoteNavigator: RemoteNavigator = get(),
) {
  if ((link.contains(warpnetXSchema) || isWarpnetDeeplink(link)) && deepLink) {
    navigate(link)
  } else {
    remoteNavigator.openDeepLink(link)
  }
}

fun Navigator.compose(
  composeType: ComposeType,
  statusKey: MicroBlogKey? = null,
  navOptions: NavOptions? = null,
) {
  navigate(Root.Compose.Home(composeType, statusKey), navOptions)
}

fun Navigator.hashtag(name: String) {
  navigate(Root.Mastodon.Hashtag(name))
}

fun Navigator.user(
  user: UiUser,
  navOptions: NavOptions? = null,
) {
  navigate(Root.User(user.userKey), navOptions)
}

private fun isWarpnetDeeplink(url: String) = warpnetHosts.any {
  url.startsWith(it) && url.length > it.length
}

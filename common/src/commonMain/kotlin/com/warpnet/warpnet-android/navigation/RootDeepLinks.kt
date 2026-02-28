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
package com.warpnet.warpnet-android.navigation

/**
 * if deeplink has the same parameters with route in Root.kt,
 * make it's name the same to route parameters in Root.kt too
 */
const val warpnetXSchema = "warpnet-android"

object RootDeepLinks {
  object Warpnet {
    object User {
      const val route = "$warpnetXSchema://RootDeepLinks/Warpnet/User/{screenName}"
      operator fun invoke(screenName: String) = "$warpnetXSchema://RootDeepLinks/Warpnet/User/${java.net.URLEncoder.encode(screenName, "UTF-8")}"
    }
    object Status {
      const val route = "$warpnetXSchema://RootDeepLinks/Warpnet/Status/{statusId}"
      operator fun invoke(statusId: String) = "$warpnetXSchema://RootDeepLinks/Warpnet/Status/${java.net.URLEncoder.encode(statusId, "UTF-8")}"
    }
  }
  object Mastodon {
    object Hashtag {
      const val route = "$warpnetXSchema://RootDeepLinks/Mastodon/Hashtag/{keyword}"
      operator fun invoke(keyword: String) = "$warpnetXSchema://RootDeepLinks/Mastodon/Hashtag/${java.net.URLEncoder.encode(keyword, "UTF-8")}"
    }
  }
  object User {
    const val route = "$warpnetXSchema://RootDeepLinks/User/{userKey}"
    operator fun invoke(userKey: com.warpnet.warpnet-android.model.MicroBlogKey) = "$warpnetXSchema://RootDeepLinks/User/$userKey"
  }
  object Status {
    const val route = "$warpnetXSchema://RootDeepLinks/Status/{statusKey}"
    operator fun invoke(statusKey: com.warpnet.warpnet-android.model.MicroBlogKey) = "$warpnetXSchema://RootDeepLinks/Status/$statusKey"
  }
  object Search {
    const val route = "$warpnetXSchema://RootDeepLinks/Search/{keyword}"
    operator fun invoke(keyword: String) = "$warpnetXSchema://RootDeepLinks/Search/${java.net.URLEncoder.encode(keyword, "UTF-8")}"
  }
  const val SignIn = "$warpnetXSchema://RootDeepLinks/SignIn"
  object Draft {
    const val route = "$warpnetXSchema://RootDeepLinks/Draft/{draftId}"
    operator fun invoke(draftId: String) = "$warpnetXSchema://RootDeepLinks/Draft/${java.net.URLEncoder.encode(draftId, "UTF-8")}"
  }
  object Compose {
    const val route = "$warpnetXSchema://RootDeepLinks/Compose"
    operator fun invoke(composeType: com.warpnet.warpnet-android.model.enums.ComposeType?, statusKey: com.warpnet.warpnet-android.model.MicroBlogKey?) = "$warpnetXSchema://RootDeepLinks/Compose?composeType=$composeType&statusKey=$statusKey"
  }
  object Conversation {
    const val route = "$warpnetXSchema://RootDeepLinks/Conversation/{conversationKey}"
    operator fun invoke(conversationKey: com.warpnet.warpnet-android.model.MicroBlogKey) = "$warpnetXSchema://RootDeepLinks/Conversation/$conversationKey"
  }
  object Callback {
    object SignIn {
      const val Mastodon = "$warpnetXSchema://RootDeepLinks/Callback/SignIn/Mastodon"
      const val Warpnet = "$warpnetXSchema://RootDeepLinks/Callback/SignIn/Warpnet"
    }
  }
}

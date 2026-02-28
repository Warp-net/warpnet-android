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
package com.warpnet.warpnetandroid.model

import com.warpnet.warpnetandroid.http.WarpnetServiceFactory
import com.warpnet.warpnetandroid.model.cred.BasicCredentials
import com.warpnet.warpnetandroid.model.cred.Credentials
import com.warpnet.warpnetandroid.model.cred.CredentialsType
import com.warpnet.warpnetandroid.model.cred.EmptyCredentials
import com.warpnet.warpnetandroid.model.cred.OAuth2Credentials
import com.warpnet.warpnetandroid.model.cred.OAuthCredentials
import com.warpnet.warpnetandroid.model.enums.ListType
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.model.ui.UserMetrics
import com.warpnet.warpnetandroid.utils.fromJson

data class AccountDetails(
  val account: WarpnetAccount,
  val type: PlatformType,
  // Note that UserKey that being used in AccountDetails is idStr@domain, not screenName@domain
  val accountKey: MicroBlogKey,
  val credentials_type: CredentialsType,
  var credentials_json: String,
  val extras_json: String,
  var user: AmUser,
  var lastActive: Long,
  val preferences: AccountPreferences,
) {
  val credentials: Credentials
    get() = when (credentials_type) {
      CredentialsType.OAuth,
      CredentialsType.XAuth -> credentials_json.fromJson<OAuthCredentials>()
      CredentialsType.Basic -> credentials_json.fromJson<BasicCredentials>()
      CredentialsType.Empty -> credentials_json.fromJson<EmptyCredentials>()
      CredentialsType.OAuth2 -> credentials_json.fromJson<OAuth2Credentials>()
    }

  val service by lazy {
    WarpnetServiceFactory.createApiService(
      type = type,
      credentials = credentials,
      accountKey = accountKey
    )
  }

  val listType: ListType
    get() = when (type) {
      PlatformType.Warpnet -> ListType.All
      PlatformType.StatusNet -> TODO()
      PlatformType.Fanfou -> TODO()
      PlatformType.Mastodon -> ListType.Owned
    }

  val supportDirectMessage = type == PlatformType.Warpnet

  fun toUi() = with(user) {
    UiUser(
      id = userId,
      name = name,
      screenName = screenName,
      profileImage = profileImage,
      profileBackgroundImage = profileBackgroundImage,
      metrics = UserMetrics(
        fans = followersCount,
        follow = friendsCount,
        listed = listedCount,
        status = 0
      ),
      rawDesc = desc,
      htmlDesc = desc,
      website = website,
      location = location,
      verified = verified,
      protected = isProtected,
      userKey = userKey,
      platformType = type,
      acct = userKey.copy(id = screenName),
    )
  }
}

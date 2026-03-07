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
package com.warpnet.services.warpnet

import com.warpnet.services.http.HttpClientFactory
import com.warpnet.services.http.authorization.OAuth1Authorization
import com.warpnet.services.utils.queryString
import com.warpnet.services.warpnet.api.WarpnetOAuthResources
import com.warpnet.services.warpnet.model.AccessToken
import com.warpnet.services.warpnet.model.OAuthToken

class WarpnetOAuthService(
  private val consumerKey: String,
  private val consumerSecret: String,
  private val httpClientFactory: HttpClientFactory,
) {
  suspend fun getOAuthToken(
    callback: String = "oob",
  ): OAuthToken {
    return httpClientFactory.createResources(
      WarpnetOAuthResources::class.java,
      WARPNET_BASE_URL,
      useCache = false,
      authorization = OAuth1Authorization(
        consumerKey,
        consumerSecret,
      ),
    ).requestToken(callback).queryString()
  }

  suspend fun getAccessToken(pinCode: String, token: OAuthToken): AccessToken {
    return httpClientFactory.createResources(
      WarpnetOAuthResources::class.java,
      WARPNET_BASE_URL,
      useCache = false,
      authorization = OAuth1Authorization(
        consumerKey,
        consumerSecret,
        token.oauth_token,
      ),
    ).accessToken(pinCode).queryString()
  }

  fun getWebOAuthUrl(token: OAuthToken) =
    "https://api.warpnet.com/oauth/authorize?oauth_token=${token.oauth_token}"
}

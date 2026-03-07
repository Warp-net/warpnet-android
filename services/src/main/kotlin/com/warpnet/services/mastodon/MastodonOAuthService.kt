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
package com.warpnet.services.mastodon

import com.warpnet.services.http.HttpClientFactory
import com.warpnet.services.http.authorization.BearerAuthorization
import com.warpnet.services.http.authorization.EmptyAuthorization
import com.warpnet.services.mastodon.api.MastodonOAuthResources
import com.warpnet.services.mastodon.model.Account
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MastodonApplication(
  val id: String? = null,
  val name: String? = null,
  @SerialName("client_id") val clientId: String? = null,
  @SerialName("client_secret") val clientSecret: String? = null,
  @SerialName("redirect_uri") val redirectUri: String? = null,
)

@Serializable
data class MastodonAccessTokenResponse(
  @SerialName("access_token") val accessToken: String? = null,
  @SerialName("token_type") val tokenType: String? = null,
  val scope: String? = null,
)

class MastodonOAuthService(
  private val baseUrl: String,
  private val client_name: String,
  private val website: String,
  private val redirect_uri: String,
  private val httpClientFactory: HttpClientFactory,
) {
  private fun createResources() = httpClientFactory.createResources(
    clazz = MastodonOAuthResources::class.java,
    baseUrl = baseUrl,
    useCache = false,
    authorization = EmptyAuthorization(),
  )

  suspend fun createApplication(): MastodonApplication {
    return createResources().createApp(
      clientName = client_name,
      redirectUris = redirect_uri,
      scopes = "read write follow",
      website = website,
    )
  }

  fun getWebOAuthUrl(application: MastodonApplication): String {
    return "$baseUrl/oauth/authorize?client_id=${application.clientId}&redirect_uri=${application.redirectUri}&response_type=code&scope=read+write+follow"
  }

  suspend fun getAccessToken(
    code: String,
    application: MastodonApplication,
  ): MastodonAccessTokenResponse {
    return createResources().getAccessToken(
      clientId = application.clientId ?: "",
      clientSecret = application.clientSecret ?: "",
      redirectUri = application.redirectUri ?: redirect_uri,
      code = code,
      grantType = "authorization_code",
    )
  }

  suspend fun verifyCredentials(accessToken: String): Account {
    return httpClientFactory.createResources(
      clazz = MastodonOAuthResources::class.java,
      baseUrl = baseUrl,
      useCache = false,
      authorization = BearerAuthorization(accessToken),
    ).verifyCredentials()
  }
}

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
package com.warpnet.warpnetandroid.http

import com.warpnet.services.gif.GifService
import com.warpnet.services.gif.giphy.GiphyService
import com.warpnet.services.http.HttpClientFactory
import com.warpnet.services.http.config.HttpConfigClientFactory
import com.warpnet.services.mastodon.MastodonService
import com.warpnet.services.microblog.MicroBlogService
import com.warpnet.services.warpnet.WarpnetService
import com.warpnet.warpnetandroid.BuildConfig
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.cred.Credentials
import com.warpnet.warpnetandroid.model.cred.OAuth2Credentials
import com.warpnet.warpnetandroid.model.cred.OAuthCredentials
import com.warpnet.warpnetandroid.model.enums.PlatformType

class WarpnetServiceFactory(private val configProvider: WarpnetHttpConfigProvider) {

  companion object {
    private var instance: WarpnetServiceFactory? = null

    fun initiate(configProvider: WarpnetHttpConfigProvider) {
      instance = WarpnetServiceFactory(configProvider)
    }

    fun createApiService(type: PlatformType, credentials: Credentials, accountKey: MicroBlogKey): MicroBlogService {
      return instance?.let {
        when (type) {
          PlatformType.Warpnet -> {
            credentials.let {
              it as OAuthCredentials
            }.let {
              WarpnetService(
                consumer_key = it.consumer_key,
                consumer_secret = it.consumer_secret,
                access_token = it.access_token,
                access_token_secret = it.access_token_secret,
                httpClientFactory = createHttpClientFactory(),
                accountId = accountKey.id
              )
            }
          }
          PlatformType.StatusNet -> TODO()
          PlatformType.Fanfou -> TODO()
          PlatformType.Mastodon ->
            credentials.let {
              it as OAuth2Credentials
            }.let {
              MastodonService(
                baseUrl = "https://${accountKey.host}",
                accessToken = it.access_token,
                httpClientFactory = createHttpClientFactory()
              )
            }
        }
      } ?: throw Error("Factory needs to be initiate")
    }

    fun createHttpClientFactory(): HttpClientFactory {
      return instance?.let {
        HttpConfigClientFactory(it.configProvider)
      } ?: throw Error("Factory needs to be initiate")
    }

    fun createGifService(): GifService {
      return instance?.let {
        GiphyService(
          apiKey = BuildConfig.GIPHYKEY,
          httpClientFactory = createHttpClientFactory()
        )
      } ?: throw Error("Factory needs to be initiate")
    }
  }
}

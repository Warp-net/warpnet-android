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
package com.warpnet.services.http.config

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.warpnet.services.http.AuthorizationInterceptor
import com.warpnet.services.http.HttpClientFactory
import com.warpnet.services.http.HttpConfigProvider
import com.warpnet.services.http.authorization.Authorization
import com.warpnet.services.proxy.ProxyConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.net.InetSocketAddress
import java.net.Proxy

class HttpConfigClientFactory(
  private val configProvider: HttpConfigProvider,
) : HttpClientFactory {

  private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
  }

  override fun <T : Any> createResources(
    clazz: Class<T>,
    baseUrl: String,
    useCache: Boolean,
    authorization: Authorization,
  ): T {
    val client = createHttpClientBuilder()
      .addInterceptor(AuthorizationInterceptor(authorization))
      .build()

    return Retrofit.Builder()
      .baseUrl(baseUrl)
      .client(client)
      .addConverterFactory(json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
      .build()
      .create(clazz)
  }

  override fun createHttpClientBuilder(): OkHttpClient.Builder {
    val config = configProvider.provideConfig()
    val builder = OkHttpClient.Builder()
      .addInterceptor(
        HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BASIC
        }
      )

    val proxyConfig = config.proxyConfig
    if (proxyConfig != null && proxyConfig.enable) {
      val proxyType = when (proxyConfig.type) {
        ProxyConfig.Type.SOCKS -> Proxy.Type.SOCKS
        else -> Proxy.Type.HTTP
      }
      builder.proxy(
        Proxy(
          proxyType,
          InetSocketAddress(proxyConfig.server, proxyConfig.port)
        )
      )
    }

    return builder
  }
}

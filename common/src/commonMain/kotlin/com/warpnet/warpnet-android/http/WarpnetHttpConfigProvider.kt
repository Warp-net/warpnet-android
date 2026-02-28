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
package com.warpnet.warpnet-android.http

import androidx.datastore.core.DataStore
import com.warpnet.services.http.HttpConfigProvider
import com.warpnet.services.http.config.HttpConfig
import com.warpnet.services.proxy.ProxyConfig
import com.warpnet.warpnet-android.preferences.model.MiscPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class WarpnetHttpConfigProvider(
  private val miscPreferences: DataStore<MiscPreferences>
) : HttpConfigProvider {
  override fun provideConfig(): HttpConfig {
    return runBlocking {
      miscPreferences.data.map {
        HttpConfig(
          proxyConfig = ProxyConfig(
            enable = it.useProxy,
            server = it.proxyServer,
            port = it.proxyPort,
            userName = it.proxyUserName,
            password = it.proxyPassword,
            type = when (it.proxyType) {
              MiscPreferences.ProxyType.REVERSE -> ProxyConfig.Type.REVERSE
              MiscPreferences.ProxyType.SOCKS -> ProxyConfig.Type.SOCKS
              else -> ProxyConfig.Type.HTTP
            }
          )
        )
      }.first()
    }
  }
}

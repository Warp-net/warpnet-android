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
package com.warpnet.warpnetandroid.utils

import com.warpnet.warpnetandroid.navigation.RootDeepLinks
import kotlinx.coroutines.channels.Channel

object BrowserLoginDeepLinksChannel {
  private val channel: Channel<String> = Channel()

  fun send(uri: String) {
    channel.trySend(uri)
  }

  fun canHandle(uri: String): Boolean {
    return uri.startsWith(RootDeepLinks.Callback.SignIn.Mastodon) ||
      uri.startsWith(RootDeepLinks.Callback.SignIn.Warpnet)
  }

  suspend fun waitOne(): String {
    return channel.receive()
  }

  fun cancel() {
    channel.cancel()
  }
}

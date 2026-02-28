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

import com.warpnet.services.mastodon.MastodonService
import com.warpnet.warpnetandroid.dataprovider.mapper.toUi
import com.warpnet.warpnetandroid.model.AccountDetails
import com.warpnet.warpnetandroid.model.ui.UiEmojiCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object MastodonEmojiCache {
  private val items = hashMapOf<String, Flow<List<UiEmojiCategory>>>()
  fun get(account: AccountDetails): Flow<List<UiEmojiCategory>> {
    return items.getOrPut(account.accountKey.host) {
      flow {
        account.service.let {
          it as MastodonService
        }.let {
          try {
            it.emojis().toUi()
          } catch (e: Throwable) {
            emptyList()
          }
        }.let {
          emit(it)
        }
      }
    }
  }
}

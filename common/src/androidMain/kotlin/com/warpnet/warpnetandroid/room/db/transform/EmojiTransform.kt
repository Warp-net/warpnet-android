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
package com.warpnet.warpnetandroid.room.db.transform

import com.warpnet.services.mastodon.model.Emoji
import com.warpnet.warpnetandroid.model.ui.UiEmoji
import com.warpnet.warpnetandroid.model.ui.UiEmojiCategory
import kotlinx.collections.immutable.toPersistentList

internal fun List<Emoji>.toUi(): List<UiEmojiCategory> = groupBy({ it.category }, { it }).map {
  UiEmojiCategory(
    if (it.key.isNullOrEmpty()) null else it.key,
    it.value.map { emoji ->
      UiEmoji(
        shortcode = emoji.shortcode,
        url = emoji.url,
        staticURL = emoji.staticURL,
        visibleInPicker = emoji.visibleInPicker,
        category = emoji.category
      )
    }.toPersistentList()
  )
}

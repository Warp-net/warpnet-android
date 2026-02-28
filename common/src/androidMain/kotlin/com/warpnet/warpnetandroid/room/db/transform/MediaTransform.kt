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

import com.warpnet.warpnetandroid.model.ui.UiMedia
import com.warpnet.warpnetandroid.room.db.model.DbMedia
import java.util.UUID

internal fun List<DbMedia>.toUi() = sortedBy { it.order }.map {
  UiMedia(
    url = it.url,
    belongToKey = it.belongToKey,
    mediaUrl = it.mediaUrl,
    previewUrl = it.previewUrl,
    type = it.type,
    width = it.width,
    height = it.height,
    pageUrl = it.pageUrl,
    altText = it.altText,
    order = it.order,
  )
}

internal fun List<UiMedia>.toDbMedia() = map {
  DbMedia(
    url = it.url,
    belongToKey = it.belongToKey,
    mediaUrl = it.mediaUrl,
    previewUrl = it.previewUrl?.toString(),
    type = it.type,
    width = it.width,
    height = it.height,
    pageUrl = it.pageUrl,
    altText = it.altText,
    order = it.order,
    _id = UUID.randomUUID().toString(),
  )
}

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

import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiUrlEntity
import com.warpnet.warpnetandroid.room.db.model.DbUrlEntity
import java.util.UUID

internal fun DbUrlEntity.toUi() = UiUrlEntity(
  url = url,
  expandedUrl = expandedUrl,
  displayUrl = displayUrl,
  title = title,
  description = description,
  image = image
)
internal fun List<DbUrlEntity>.toUi() = map { it.toUi() }

internal fun List<UiUrlEntity>.toDbUrl(belongToKey: MicroBlogKey) = map {
  DbUrlEntity(
    url = it.url,
    _id = UUID.randomUUID().toString(),
    statusKey = belongToKey,
    expandedUrl = it.expandedUrl,
    displayUrl = it.displayUrl,
    title = it.title,
    description = it.description,
    image = it.image
  )
}

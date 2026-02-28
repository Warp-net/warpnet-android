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
package com.warpnet.warpnetandroid.room.db.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.warpnet.warpnetandroid.model.MicroBlogKey

@Entity(
  tableName = "url_entity",
  indices = [Index(value = ["statusKey", "url"], unique = true)],
)
internal data class DbUrlEntity(
  @PrimaryKey
  val _id: String,
  val statusKey: MicroBlogKey,
  val url: String,
  val expandedUrl: String,
  val displayUrl: String,
  val title: String?,
  val description: String?,
  val image: String?,
)

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
package com.warpnet.warpnet-android.room.db.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.warpnet.warpnet-android.model.MicroBlogKey

@Entity(
  tableName = "notification_cursor",
  indices = [
    Index(
      value = ["accountKey", "type"],
      unique = true
    )
  ],
)
internal data class DbNotificationCursor(
  /**
   * Id that being used in the database
   */
  @PrimaryKey
  val _id: String,
  val accountKey: MicroBlogKey,
  val type: DbNotificationCursorType,
  val value: String,
  val timestamp: Long,
)

enum class DbNotificationCursorType {
  General,
  Mentions,
  Follower,
}

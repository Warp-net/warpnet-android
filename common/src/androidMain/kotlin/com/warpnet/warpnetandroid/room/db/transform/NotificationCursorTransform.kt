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

import com.warpnet.warpnetandroid.model.enums.NotificationCursorType
import com.warpnet.warpnetandroid.model.paging.NotificationCursor
import com.warpnet.warpnetandroid.room.db.model.DbNotificationCursor
import com.warpnet.warpnetandroid.room.db.model.DbNotificationCursorType

internal fun DbNotificationCursor.toUi() = NotificationCursor(
  _id = _id,
  accountKey = accountKey,
  type = type.toUi(),
  value = value,
  timestamp = timestamp
)

internal fun NotificationCursor.toDbCursor() = DbNotificationCursor(
  _id = _id,
  accountKey = accountKey,
  type = type.toDb(),
  value = value,
  timestamp = timestamp
)

internal fun DbNotificationCursorType.toUi() = when (this) {
  DbNotificationCursorType.General -> NotificationCursorType.General
  DbNotificationCursorType.Mentions -> NotificationCursorType.Mentions
  DbNotificationCursorType.Follower -> NotificationCursorType.Follower
}

internal fun NotificationCursorType.toDb() = when (this) {
  NotificationCursorType.General -> DbNotificationCursorType.General
  NotificationCursorType.Mentions -> DbNotificationCursorType.Mentions
  NotificationCursorType.Follower -> DbNotificationCursorType.Follower
}

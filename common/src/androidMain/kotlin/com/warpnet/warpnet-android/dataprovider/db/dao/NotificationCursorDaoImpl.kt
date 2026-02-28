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
package com.warpnet.warpnet-android.dataprovider.db.dao

import com.warpnet.warpnet-android.db.dao.NotificationCursorDao
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.NotificationCursorType
import com.warpnet.warpnet-android.model.paging.NotificationCursor
import com.warpnet.warpnet-android.room.db.RoomCacheDatabase
import com.warpnet.warpnet-android.room.db.transform.toDb
import com.warpnet.warpnet-android.room.db.transform.toDbCursor
import com.warpnet.warpnet-android.room.db.transform.toUi

internal class NotificationCursorDaoImpl(private val database: RoomCacheDatabase) : NotificationCursorDao {
  override suspend fun find(
    accountKey: MicroBlogKey,
    type: NotificationCursorType
  ) = database.notificationCursorDao().find(accountKey, type.toDb())?.toUi()

  override suspend fun add(notificationCursor: NotificationCursor) {
    database.notificationCursorDao().add(notificationCursor.toDbCursor())
  }
}

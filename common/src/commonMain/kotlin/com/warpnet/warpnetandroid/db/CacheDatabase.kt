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
package com.warpnet.warpnetandroid.db

import com.warpnet.warpnetandroid.db.dao.DirectMessageConversationDao
import com.warpnet.warpnetandroid.db.dao.DirectMessageEventDao
import com.warpnet.warpnetandroid.db.dao.ListsDao
import com.warpnet.warpnetandroid.db.dao.MediaDao
import com.warpnet.warpnetandroid.db.dao.NotificationCursorDao
import com.warpnet.warpnetandroid.db.dao.PagingTimelineDao
import com.warpnet.warpnetandroid.db.dao.StatusDao
import com.warpnet.warpnetandroid.db.dao.TrendDao
import com.warpnet.warpnetandroid.db.dao.UserDao

interface CacheDatabase {
  fun statusDao(): StatusDao
  fun mediaDao(): MediaDao
  fun userDao(): UserDao
  fun pagingTimelineDao(): PagingTimelineDao
  fun listsDao(): ListsDao
  fun notificationCursorDao(): NotificationCursorDao
  fun trendDao(): TrendDao
  fun directMessageConversationDao(): DirectMessageConversationDao
  fun directMessageDao(): DirectMessageEventDao
  suspend fun clearAllTables()
  suspend fun <R> withTransaction(block: suspend () -> R): R
}

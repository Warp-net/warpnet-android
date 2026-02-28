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
package com.warpnet.warpnetandroid.dataprovider.db

import androidx.room.withTransaction
import com.warpnet.warpnetandroid.dataprovider.db.dao.DirectMessageConversationDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.DirectMessageEventDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.ListsDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.MediaDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.NotificationCursorDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.PagingTimelineDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.StatusDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.TrendDaoImpl
import com.warpnet.warpnetandroid.dataprovider.db.dao.UserDaoImpl
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.room.db.RoomCacheDatabase

internal class CacheDatabaseImpl(private val roomCacheDatabase: RoomCacheDatabase) : CacheDatabase {
  private val statusDao = StatusDaoImpl(roomCacheDatabase)
  override fun statusDao() = statusDao

  private val mediaDao = MediaDaoImpl(roomCacheDatabase)
  override fun mediaDao() = mediaDao

  private val userDao = UserDaoImpl(roomCacheDatabase)
  override fun userDao() = userDao

  private val pagingTimelineDao = PagingTimelineDaoImpl(roomCacheDatabase)
  override fun pagingTimelineDao() = pagingTimelineDao

  private val listsDao = ListsDaoImpl(roomCacheDatabase)
  override fun listsDao() = listsDao

  private val notificationCursorDao = NotificationCursorDaoImpl(roomCacheDatabase)
  override fun notificationCursorDao() = notificationCursorDao

  private val trendDao = TrendDaoImpl(roomCacheDatabase)
  override fun trendDao() = trendDao

  private val dmConversationDao = DirectMessageConversationDaoImpl(roomCacheDatabase)
  override fun directMessageConversationDao() = dmConversationDao

  private val dmEventDao = DirectMessageEventDaoImpl(roomCacheDatabase)
  override fun directMessageDao() = dmEventDao

  override suspend fun clearAllTables() = roomCacheDatabase.clearAllTables()

  override suspend fun <R> withTransaction(block: suspend () -> R) = roomCacheDatabase.withTransaction(block)
}

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

import androidx.paging.PagingSource
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.db.dao.DirectMessageConversationDao
import com.warpnet.warpnetandroid.db.dao.DirectMessageEventDao
import com.warpnet.warpnetandroid.db.dao.ListsDao
import com.warpnet.warpnetandroid.db.dao.MediaDao
import com.warpnet.warpnetandroid.db.dao.NotificationCursorDao
import com.warpnet.warpnetandroid.db.dao.PagingTimelineDao
import com.warpnet.warpnetandroid.db.dao.StatusDao
import com.warpnet.warpnetandroid.db.dao.TrendDao
import com.warpnet.warpnetandroid.db.dao.UserDao
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.paging.PagingTimeLine
import com.warpnet.warpnetandroid.model.paging.PagingTimeLineWithStatus
import com.warpnet.warpnetandroid.model.ui.UiDMConversation
import com.warpnet.warpnetandroid.model.ui.UiDMEvent
import com.warpnet.warpnetandroid.model.ui.UiList
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.model.ui.UiTrend
import com.warpnet.warpnetandroid.model.ui.UiUser

/**
 * In-memory implementation of CacheDatabase that doesn't persist data.
 */
internal class CacheDatabaseImpl : CacheDatabase {
  
  private val statusDao = object : StatusDao {
    override suspend fun insertAll(statuses: List<UiStatus>, accountKey: MicroBlogKey) {
      // No-op for stateless app
    }

    override fun findWithStatusKey(statusKey: MicroBlogKey, accountKey: MicroBlogKey): UiStatus? {
      return null
    }
  }

  private val mediaDao = object : MediaDao {}

  private val userDao = object : UserDao {
    override fun findWithUserKey(userKey: MicroBlogKey): UiUser? {
      return null
    }

    override suspend fun insertAll(users: List<UiUser>) {
      // No-op for stateless app
    }
  }

  private val pagingTimelineDao = object : PagingTimelineDao {
    override suspend fun insertAll(items: List<PagingTimeLine>) {
      // No-op for stateless app
    }

    override fun findWithStatusKey(statusKey: MicroBlogKey, accountKey: MicroBlogKey): PagingTimeLineWithStatus? {
      return null
    }

    override suspend fun clearAll(pagingKey: String, accountKey: MicroBlogKey) {
      // No-op for stateless app
    }

    override fun getPagingSource(pagingKey: String, accountKey: MicroBlogKey): PagingSource<Int, PagingTimeLineWithStatus> {
      return EmptyPagingSource()
    }

    override fun getLatest(pagingKey: String, accountKey: MicroBlogKey): PagingTimeLineWithStatus? {
      return null
    }
  }

  private val listsDao = object : ListsDao {
    override suspend fun insertAll(lists: List<UiList>) {
      // No-op for stateless app
    }

    override fun getPagingSource(accountKey: MicroBlogKey): PagingSource<Int, UiList> {
      return EmptyPagingSource()
    }
  }

  private val notificationCursorDao = object : NotificationCursorDao {}

  private val trendDao = object : TrendDao {
    override suspend fun clear(accountKey: MicroBlogKey) {
      // No-op for stateless app
    }

    override suspend fun insertAll(trends: List<UiTrend>) {
      // No-op for stateless app
    }

    override fun getPagingSource(accountKey: MicroBlogKey): PagingSource<Int, UiTrend> {
      return EmptyPagingSource()
    }
  }

  private val directMessageConversationDao = object : DirectMessageConversationDao {
    override fun getPagingSource(accountKey: MicroBlogKey): PagingSource<Int, UiDMConversation> {
      return EmptyPagingSource()
    }
  }

  private val directMessageEventDao = object : DirectMessageEventDao {
    override fun getPagingSource(accountKey: MicroBlogKey, conversationKey: MicroBlogKey): PagingSource<Int, UiDMEvent> {
      return EmptyPagingSource()
    }
  }

  override fun statusDao(): StatusDao = statusDao

  override fun mediaDao(): MediaDao = mediaDao

  override fun userDao(): UserDao = userDao

  override fun pagingTimelineDao(): PagingTimelineDao = pagingTimelineDao

  override fun listsDao(): ListsDao = listsDao

  override fun notificationCursorDao(): NotificationCursorDao = notificationCursorDao

  override fun trendDao(): TrendDao = trendDao

  override fun directMessageConversationDao(): DirectMessageConversationDao = directMessageConversationDao

  override fun directMessageDao(): DirectMessageEventDao = directMessageEventDao

  override suspend fun clearAllTables() {
    // No-op for in-memory implementation
  }

  override suspend fun <R> withTransaction(block: suspend () -> R): R {
    // Execute block directly without transaction support
    return block()
  }
}

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
package com.warpnet.warpnet-android.room.db.paging

import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.room.db.RoomCacheDatabase
import com.warpnet.warpnet-android.room.db.dao.RoomDirectMessageConversationDao
import com.warpnet.warpnet-android.room.db.dao.RoomDirectMessageEventDao
import com.warpnet.warpnet-android.room.db.dao.RoomListsDao
import com.warpnet.warpnet-android.room.db.dao.RoomPagingTimelineDao
import com.warpnet.warpnet-android.room.db.dao.RoomTrendDao
import com.warpnet.warpnet-android.room.db.transform.toPagingTimeline
import com.warpnet.warpnet-android.room.db.transform.toUi

internal fun RoomDirectMessageConversationDao.getPagingSource(
  cacheDatabase: RoomCacheDatabase,
  accountKey: MicroBlogKey
) = LimitOffsetTransformPagingSource(
  db = cacheDatabase,
  loadPagingList = { offset, limit ->
    getPagingList(
      accountKey = accountKey,
      limit = limit,
      offset = offset
    ).map { it.toUi() }
  },
  queryItemCount = {
    getPagingListCount(accountKey = accountKey)
  },
  tables = arrayOf(
    "dm_conversation",
    "dm_event"
  )
)

internal fun RoomDirectMessageEventDao.getPagingSource(
  cacheDatabase: RoomCacheDatabase,
  accountKey: MicroBlogKey,
  conversationKey: MicroBlogKey
) = LimitOffsetTransformPagingSource(
  db = cacheDatabase,
  loadPagingList = { offset, limit ->
    getPagingList(
      accountKey = accountKey,
      conversationKey = conversationKey,
      limit = limit,
      offset = offset
    ).map { it.toUi() }
  },
  queryItemCount = {
    getPagingListCount(accountKey = accountKey, conversationKey = conversationKey)
  },
  tables = arrayOf("dm_event")
)

internal fun RoomListsDao.getPagingSource(
  cacheDatabase: RoomCacheDatabase,
  accountKey: MicroBlogKey,
) = LimitOffsetTransformPagingSource(
  db = cacheDatabase,
  loadPagingList = { offset, limit ->
    getPagingList(
      accountKey = accountKey,
      limit = limit,
      offset = offset
    ).map { it.toUi() }
  },
  queryItemCount = {
    getPagingListCount(accountKey = accountKey)
  },
  tables = arrayOf("lists")
)

internal fun RoomPagingTimelineDao.getPagingSource(
  cacheDatabase: RoomCacheDatabase,
  pagingKey: String,
  accountKey: MicroBlogKey,
) = LimitOffsetTransformPagingSource(
  db = cacheDatabase,
  loadPagingList = { offset, limit ->
    getPagingList(
      pagingKey = pagingKey,
      accountKey = accountKey,
      offset = offset,
      limit = limit
    ).map { it.toPagingTimeline(accountKey = accountKey) }
  },
  queryItemCount = {
    getPagingListCount(accountKey = accountKey, pagingKey = pagingKey)
  },
  tables = arrayOf("paging_timeline", "status")
)

internal fun RoomTrendDao.getPagingSource(
  cacheDatabase: RoomCacheDatabase,
  accountKey: MicroBlogKey,
) = LimitOffsetTransformPagingSource(
  db = cacheDatabase,
  loadPagingList = { offset, limit ->
    getPagingList(
      accountKey = accountKey,
      limit = limit,
      offset = offset
    ).map { it.toUi(accountKey = accountKey) }
  },
  queryItemCount = {
    getPagingListCount(accountKey = accountKey)
  },
  tables = arrayOf(
    "trends",
    "trend_histories"
  )
)

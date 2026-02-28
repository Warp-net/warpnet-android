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
package com.warpnet.warpnetandroid.dataprovider.db.dao

import androidx.paging.PagingSource
import com.warpnet.warpnetandroid.db.dao.TrendDao
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiTrend
import com.warpnet.warpnetandroid.room.db.RoomCacheDatabase
import com.warpnet.warpnetandroid.room.db.model.DbTrendWithHistory.Companion.saveToDb
import com.warpnet.warpnetandroid.room.db.paging.getPagingSource
import com.warpnet.warpnetandroid.room.db.transform.toDbTrendWithHistory

internal class TrendDaoImpl(
  val roomCacheDatabase: RoomCacheDatabase
) : TrendDao {
  override suspend fun insertAll(trends: List<UiTrend>) {
    trends.toDbTrendWithHistory().saveToDb(roomCacheDatabase)
  }

  override fun getPagingSource(accountKey: MicroBlogKey): PagingSource<Int, UiTrend> {
    return roomCacheDatabase.trendDao().getPagingSource(
      cacheDatabase = roomCacheDatabase,
      accountKey = accountKey
    )
  }

  override suspend fun clear(accountKey: MicroBlogKey) {
    roomCacheDatabase.trendDao().clearAll(accountKey)
    roomCacheDatabase.trendHistoryDao().clearAll(accountKey)
  }
}

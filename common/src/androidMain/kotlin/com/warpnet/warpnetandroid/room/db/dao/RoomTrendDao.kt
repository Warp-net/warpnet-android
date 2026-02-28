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
package com.warpnet.warpnetandroid.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.room.db.model.DbTrend
import com.warpnet.warpnetandroid.room.db.model.DbTrendWithHistory

@Dao
internal interface RoomTrendDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(trends: List<DbTrend>)

  @Transaction
  @Query("SELECT * FROM trends WHERE accountKey == :accountKey  LIMIT :limit")
  suspend fun find(accountKey: MicroBlogKey, limit: Int): List<DbTrendWithHistory>

  @Transaction
  @Query("SELECT * FROM trends WHERE accountKey == :accountKey LIMIT :limit OFFSET :offset")
  suspend fun getPagingList(
    accountKey: MicroBlogKey,
    limit: Int,
    offset: Int
  ): List<DbTrendWithHistory>

  @Transaction
  @Query("SELECT COUNT(*) FROM (SELECT * FROM trends WHERE accountKey == :accountKey)")
  suspend fun getPagingListCount(
    accountKey: MicroBlogKey
  ): Int

  @Query("DELETE FROM trends WHERE accountKey == :accountKey")
  suspend fun clearAll(
    accountKey: MicroBlogKey,
  )

  @Transaction
  @Query("SELECT * FROM trends WHERE accountKey == :accountKey")
  fun getAll(
    accountKey: MicroBlogKey,
  ): List<DbTrendWithHistory>
}

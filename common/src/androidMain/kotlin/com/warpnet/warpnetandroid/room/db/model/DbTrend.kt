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

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.room.db.RoomCacheDatabase

@Entity(
  tableName = "trends",
  indices = [Index(value = ["trendKey", "url"], unique = true)],
)
internal data class DbTrend(
  @PrimaryKey
  val _id: String,
  val trendKey: MicroBlogKey,
  val accountKey: MicroBlogKey,
  val displayName: String,
  val url: String,
  val query: String,
  val volume: Long,
)

internal data class DbTrendWithHistory(
  @Embedded
  val trend: DbTrend,

  @Relation(
    parentColumn = "trendKey",
    entityColumn = "trendKey",
    entity = DbTrendHistory::class
  )
  val history: List<DbTrendHistory>,
) {
  companion object {
    suspend fun List<DbTrendWithHistory>.saveToDb(database: RoomCacheDatabase) {
      map { it.trend }.let {
        database.trendDao().insertAll(it)
      }
      map { it.history }
        .flatten()
        .let {
          database.trendHistoryDao().insertAll(it)
        }
    }
  }
}

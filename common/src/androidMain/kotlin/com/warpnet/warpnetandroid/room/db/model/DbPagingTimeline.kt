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

@Entity(
  tableName = "paging_timeline",
  indices = [
    Index(
      value = ["accountKey", "statusKey", "pagingKey"],
      unique = true
    )
  ],
)
internal data class DbPagingTimeline(
  @PrimaryKey
  val _id: String,
  val accountKey: MicroBlogKey,
  val pagingKey: String,
  val statusKey: MicroBlogKey,
  val timestamp: Long,
  val sortId: Long,
  var isGap: Boolean,
)

internal data class DbPagingTimelineWithStatus(
  @Embedded
  val timeline: DbPagingTimeline,

  @Relation(
    parentColumn = "statusKey",
    entityColumn = "statusKey",
    entity = DbStatusV2::class,
  )
  val status: DbStatusWithReference,
)

internal enum class UserTimelineType {
  Status,
  Media,
  Favourite
}

internal fun UserTimelineType.pagingKey(accountKey: MicroBlogKey) = "user:$accountKey:$this"

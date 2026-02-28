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
import com.warpnet.warpnetandroid.model.enums.ReferenceType
import com.warpnet.warpnetandroid.room.db.RoomCacheDatabase
import java.util.UUID

@Entity(
  tableName = "status_reference",
  indices = [
    Index(
      value = [
        "referenceType",
        "statusKey",
        "referenceStatusKey",
      ],
      unique = true,
    ),
  ],
)
internal data class DbStatusReference(
  /**
   * Id that being used in the database
   */
  @PrimaryKey
  val _id: String,
  val referenceType: ReferenceType,
  val statusKey: MicroBlogKey,
  val referenceStatusKey: MicroBlogKey,
)

internal data class DbStatusReferenceWithStatus(
  @Embedded
  val reference: DbStatusReference,
  @Relation(
    parentColumn = "referenceStatusKey",
    entityColumn = "statusKey",
    entity = DbStatusV2::class
  )
  val status: DbStatusWithMediaAndUser?
)

internal fun DbStatusWithMediaAndUser?.toDbStatusReference(
  statusKey: MicroBlogKey,
  referenceType: ReferenceType,
): DbStatusReferenceWithStatus? {
  return if (this == null) {
    null
  } else {
    DbStatusReferenceWithStatus(
      reference = DbStatusReference(
        _id = UUID.randomUUID().toString(),
        referenceType = referenceType,
        statusKey = statusKey,
        referenceStatusKey = data.statusKey
      ),
      status = this,
    )
  }
}

internal data class DbStatusWithReference(
  @Embedded
  val status: DbStatusWithMediaAndUser,
  @Relation(
    parentColumn = "statusKey",
    entityColumn = "statusKey",
    entity = DbStatusReference::class
  )
  val references: List<DbStatusReferenceWithStatus>,
)

internal suspend fun List<DbStatusWithReference>.saveToDb(
  database: RoomCacheDatabase
) {
  this.map { it.references.mapNotNull { it.status } + it.status }
    .flatten()
    .saveToDb(database = database)
  this.flatMap { it.references }.map { it.reference }.let {
    database.statusReferenceDao().insertAll(it)
  }
}

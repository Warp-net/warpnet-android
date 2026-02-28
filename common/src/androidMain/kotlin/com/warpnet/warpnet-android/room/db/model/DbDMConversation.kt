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
package com.warpnet.warpnet-android.room.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.room.db.RoomCacheDatabase

@Entity(
  tableName = "dm_conversation",
  indices = [Index(value = ["accountKey", "conversationKey"], unique = true)],
)
internal data class DbDMConversation(
  @PrimaryKey
  val _id: String,
  val accountKey: MicroBlogKey,
  // conversation
  val conversationId: String,
  val conversationKey: MicroBlogKey,
  val conversationAvatar: String,
  val conversationName: String,
  val conversationSubName: String,
  val conversationType: Type,
  val recipientKey: MicroBlogKey,
) {
  enum class Type {
    ONE_TO_ONE,
    GROUP
  }

  companion object {
    suspend fun List<DbDMConversation>.saveToDb(cacheDatabase: RoomCacheDatabase) {
      cacheDatabase.directMessageConversationDao().insertAll(this)
    }
  }
}

internal data class DbDirectMessageConversationWithMessage(
  @Relation(parentColumn = "conversationKey", entityColumn = "conversationKey")
  val conversation: DbDMConversation,

  @Embedded
  val latestMessage: DbDMEventWithAttachments
)

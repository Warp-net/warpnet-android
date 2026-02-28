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
package com.warpnet.warpnet-android.room.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.room.db.model.DbDMEvent
import com.warpnet.warpnet-android.room.db.model.DbDMEventWithAttachments

@Dao
internal interface RoomDirectMessageEventDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(messages: List<DbDMEvent>)

  @Transaction
  @Query("SELECT * FROM dm_event WHERE accountKey == :accountKey ORDER BY sortId DESC")
  suspend fun getAll(accountKey: MicroBlogKey): List<DbDMEventWithAttachments>

  @Transaction
  @Query("SELECT * FROM dm_event WHERE accountKey == :accountKey AND conversationKey == :conversationKey ORDER BY sortId DESC")
  suspend fun find(accountKey: MicroBlogKey, conversationKey: MicroBlogKey): List<DbDMEventWithAttachments>

  @Transaction
  @Query("SELECT * FROM dm_event WHERE accountKey == :accountKey AND conversationKey == :conversationKey AND messageKey == :messageKey")
  suspend fun findWithMessageKey(accountKey: MicroBlogKey, conversationKey: MicroBlogKey, messageKey: MicroBlogKey): DbDMEventWithAttachments?

  @Transaction
  @Query("SELECT * FROM dm_event WHERE accountKey == :accountKey AND conversationKey == :conversationKey ORDER BY sortId DESC LIMIT :limit OFFSET :offset")
  suspend fun getPagingList(
    accountKey: MicroBlogKey,
    conversationKey: MicroBlogKey,
    limit: Int,
    offset: Int
  ): List<DbDMEventWithAttachments>

  @Transaction
  @Query("SELECT COUNT(*) FROM (SELECT * FROM dm_event WHERE accountKey == :accountKey AND conversationKey == :conversationKey ORDER BY sortId DESC)")
  suspend fun getPagingListCount(
    accountKey: MicroBlogKey,
    conversationKey: MicroBlogKey,
  ): Int

  @Delete
  suspend fun delete(data: DbDMEvent)

  @Query("SELECT COUNT(*) FROM dm_event  WHERE accountKey == :accountKey AND conversationKey == :conversationKey")
  fun getMessageCount(accountKey: MicroBlogKey, conversationKey: MicroBlogKey): Long

  @Query("DELETE FROM dm_event WHERE accountKey == :accountKey AND conversationKey == :conversationKey")
  suspend fun clearConversation(accountKey: MicroBlogKey, conversationKey: MicroBlogKey)

  @Query("DELETE FROM dm_event WHERE accountKey == :accountKey")
  suspend fun clearAll(accountKey: MicroBlogKey)
}

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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.room.db.model.DbDMConversation
import com.warpnet.warpnetandroid.room.db.model.DbDirectMessageConversationWithMessage
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RoomDirectMessageConversationDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(conversations: List<DbDMConversation>)

  @Transaction
  @Query(
    """
            SELECT * FROM dm_event AS table1 
            JOIN (SELECT conversationKey, max(sortId) as sortId FROM dm_event WHERE accountKey == :accountKey GROUP BY conversationKey) AS table2
            ON table1.conversationKey = table2.conversationKey AND table1.sortId = table2.sortId 
            WHERE table1.accountKey == :accountKey ORDER BY table1.sortId DESC
        """
  )
  suspend fun find(accountKey: MicroBlogKey): List<DbDirectMessageConversationWithMessage>

  @Transaction
  @Query(
    """
            SELECT * FROM dm_event AS table1 
            JOIN (SELECT conversationKey, max(sortId) as sortId FROM dm_event WHERE accountKey == :accountKey GROUP BY conversationKey) AS table2
            ON table1.conversationKey = table2.conversationKey AND table1.sortId = table2.sortId 
            WHERE table1.accountKey == :accountKey ORDER BY table1.sortId DESC
            LIMIT :limit OFFSET :offset
        """
  )
  suspend fun getPagingList(
    accountKey: MicroBlogKey,
    limit: Int,
    offset: Int
  ): List<DbDirectMessageConversationWithMessage>

  @Transaction
  @Query(
    """
            SELECT COUNT(*) FROM(
            SELECT * FROM dm_event AS table1 
            JOIN (SELECT conversationKey, max(sortId) as sortId FROM dm_event WHERE accountKey == :accountKey GROUP BY conversationKey) AS table2
            ON table1.conversationKey = table2.conversationKey AND table1.sortId = table2.sortId 
            WHERE table1.accountKey == :accountKey ORDER BY table1.sortId DESC)
        """
  )
  suspend fun getPagingListCount(accountKey: MicroBlogKey): Int

  @Query("SELECT * FROM dm_conversation WHERE accountKey == :accountKey AND conversationKey == :conversationKey")
  fun findWithConversationKeyFlow(accountKey: MicroBlogKey, conversationKey: MicroBlogKey): Flow<DbDMConversation?>

  @Query("SELECT * FROM dm_conversation WHERE accountKey == :accountKey AND conversationKey == :conversationKey")
  fun findWithConversationKey(accountKey: MicroBlogKey, conversationKey: MicroBlogKey): DbDMConversation?

  @Delete
  suspend fun delete(data: DbDMConversation)

  @Query("DELETE FROM dm_conversation WHERE accountKey == :accountKey")
  suspend fun clearAll(accountKey: MicroBlogKey)
}

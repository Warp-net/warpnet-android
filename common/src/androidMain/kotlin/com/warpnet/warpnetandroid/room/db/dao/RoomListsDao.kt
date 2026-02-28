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
import androidx.room.Update
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.room.db.model.DbList
import kotlinx.coroutines.flow.Flow

@Dao
internal interface RoomListsDao {
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(lists: List<DbList>)

  @Query("SELECT * FROM lists WHERE listKey == :listKey AND accountKey == :accountKey")
  suspend fun findWithListKey(listKey: MicroBlogKey, accountKey: MicroBlogKey): DbList?

  @Query("SELECT * FROM lists WHERE listKey == :listKey AND accountKey == :accountKey")
  fun findWithListKeyWithFlow(listKey: MicroBlogKey, accountKey: MicroBlogKey): Flow<DbList?>

  @Query("SELECT * FROM lists")
  suspend fun findAll(): List<DbList>?

  @Query("SELECT * FROM lists WHERE accountKey == :accountKey")
  suspend fun findWithAccountKey(accountKey: MicroBlogKey): List<DbList>?

  @Transaction
  @Query("SELECT * FROM lists WHERE accountKey == :accountKey LIMIT :limit OFFSET :offset")
  suspend fun getPagingList(
    accountKey: MicroBlogKey,
    limit: Int,
    offset: Int
  ): List<DbList>

  @Transaction
  @Query("SELECT COUNT(*) FROM (SELECT * FROM lists WHERE accountKey == :accountKey)")
  suspend fun getPagingListCount(
    accountKey: MicroBlogKey,
  ): Int

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun update(lists: List<DbList>)

  @Delete
  suspend fun delete(lists: List<DbList>)

  @Query("DELETE FROM lists WHERE accountKey == :accountKey")
  suspend fun clearAll(
    accountKey: MicroBlogKey,
  )
}

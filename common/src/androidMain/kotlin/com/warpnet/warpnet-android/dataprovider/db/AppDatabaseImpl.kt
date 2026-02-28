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
package com.warpnet.warpnet-android.dataprovider.db

import androidx.room.withTransaction
import com.warpnet.warpnet-android.dataprovider.db.dao.DraftDaoImpl
import com.warpnet.warpnet-android.dataprovider.db.dao.SearchDaoImpl
import com.warpnet.warpnet-android.db.AppDatabase
import com.warpnet.warpnet-android.db.dao.DraftDao
import com.warpnet.warpnet-android.db.dao.SearchDao
import com.warpnet.warpnet-android.room.db.RoomAppDatabase

internal class AppDatabaseImpl(private val roomDatabase: RoomAppDatabase) : AppDatabase {
  private val draftDao = DraftDaoImpl(roomDatabase.draftDao())
  private val searchDao = SearchDaoImpl(roomDatabase)
  override fun draftDao(): DraftDao {
    return draftDao
  }

  override fun searchDao(): SearchDao {
    return searchDao
  }

  override suspend fun clearAllTables() {
    roomDatabase.clearAllTables()
  }

  override suspend fun <R> withTransaction(block: suspend () -> R) = roomDatabase.withTransaction {
    block.invoke()
  }
}

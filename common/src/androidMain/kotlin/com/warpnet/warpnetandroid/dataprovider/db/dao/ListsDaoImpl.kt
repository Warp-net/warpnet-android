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
import androidx.room.withTransaction
import com.warpnet.warpnetandroid.db.dao.ListsDao
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiList
import com.warpnet.warpnetandroid.room.db.RoomCacheDatabase
import com.warpnet.warpnetandroid.room.db.paging.getPagingSource
import com.warpnet.warpnetandroid.room.db.transform.toDbList
import com.warpnet.warpnetandroid.room.db.transform.toUi
import kotlinx.coroutines.flow.map

internal class ListsDaoImpl(private val database: RoomCacheDatabase) : ListsDao {
  override fun getPagingSource(accountKey: MicroBlogKey): PagingSource<Int, UiList> {
    return database.listsDao().getPagingSource(cacheDatabase = database, accountKey = accountKey)
  }

  override fun findWithListKeyWithFlow(
    listKey: MicroBlogKey,
    accountKey: MicroBlogKey
  ) = database.listsDao().findWithListKeyWithFlow(listKey, accountKey).map { it?.toUi() }

  override suspend fun insertAll(listOf: List<UiList>) {
    database.listsDao().insertAll(listOf.map { it.toDbList() })
  }

  override suspend fun findWithListKey(
    listKey: MicroBlogKey,
    accountKey: MicroBlogKey
  ) = database.listsDao().findWithListKey(listKey, accountKey)?.toUi()

  override suspend fun update(listOf: List<UiList>) {
    database.withTransaction {
      listOf.mapNotNull {
        database.listsDao().findWithListKey(it.listKey, it.accountKey)?.let { dbList ->
          it.toDbList(dbId = dbList._id)
        }
      }.let {
        database.listsDao().update(it)
      }
    }
  }

  override suspend fun delete(listOf: List<UiList>) {
    database.withTransaction {
      listOf.mapNotNull {
        database.listsDao().findWithListKey(it.listKey, it.accountKey)?.let { dbList ->
          it.toDbList(dbId = dbList._id)
        }
      }.let {
        database.listsDao().delete(it)
      }
    }
  }

  override suspend fun clearAll(accountKey: MicroBlogKey) {
    database.listsDao().clearAll(accountKey)
  }
}

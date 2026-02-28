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

import com.warpnet.warpnetandroid.db.dao.UserDao
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.room.db.RoomCacheDatabase
import com.warpnet.warpnetandroid.room.db.transform.toDbUser
import com.warpnet.warpnetandroid.room.db.transform.toUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class UserDaoImpl(private val database: RoomCacheDatabase) : UserDao {
  override suspend fun findWithUserKey(userKey: MicroBlogKey): UiUser? {
    return database.userDao().findWithUserKey(userKey)?.toUi()
  }

  override suspend fun insertAll(listOf: List<UiUser>) {
    database.userDao().insertAll(listOf.map { it.toDbUser() })
  }

  override fun findWithUserKeyFlow(userKey: MicroBlogKey): Flow<UiUser?> {
    return database.userDao().findWithUserKeyFlow(userKey).map { it?.toUi() }
  }
}

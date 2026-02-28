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
package com.warpnet.warpnet-android.dataprovider

import android.content.Context
import androidx.room.Room
import com.warpnet.warpnet-android.dataprovider.db.AppDatabaseImpl
import com.warpnet.warpnet-android.dataprovider.db.CacheDatabaseImpl
import com.warpnet.warpnet-android.db.AppDatabase
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.room.db.AppDatabase_Migration_1_2
import com.warpnet.warpnet-android.room.db.AppDatabase_Migration_2_3
import com.warpnet.warpnet-android.room.db.RoomAppDatabase
import com.warpnet.warpnet-android.room.db.RoomCacheDatabase

actual class DataProvider private constructor(context: Context) {
  // data provide functions....
  actual companion object Factory {
    actual fun create(): DataProvider {
      return DataProvider(get())
    }
  }

  private val roomCacheDatabase = Room.databaseBuilder(context, RoomCacheDatabase::class.java, "warpnet-android-db")
    .fallbackToDestructiveMigration()
    .build()

  private val roomAppDatabase = Room.databaseBuilder(context, RoomAppDatabase::class.java, "warpnet-android-draft-db")
    .addMigrations(AppDatabase_Migration_1_2)
    .addMigrations(AppDatabase_Migration_2_3)
    .build()

  actual val appDatabase: AppDatabase = AppDatabaseImpl(roomAppDatabase)

  actual val cacheDatabase: CacheDatabase = CacheDatabaseImpl(roomCacheDatabase)
}

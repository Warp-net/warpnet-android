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
package com.warpnet.warpnetandroid.dataprovider

import android.content.Context
import com.warpnet.warpnetandroid.dataprovider.db.AppDatabaseImpl
import com.warpnet.warpnetandroid.dataprovider.db.CacheDatabaseImpl
import com.warpnet.warpnetandroid.db.AppDatabase
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.di.ext.get

actual class DataProvider private constructor(context: Context) {
  // data provide functions....
  actual companion object Factory {
    actual fun create(): DataProvider {
      return DataProvider(get())
    }
  }

  actual val appDatabase: AppDatabase = AppDatabaseImpl()

  actual val cacheDatabase: CacheDatabase = CacheDatabaseImpl()
}

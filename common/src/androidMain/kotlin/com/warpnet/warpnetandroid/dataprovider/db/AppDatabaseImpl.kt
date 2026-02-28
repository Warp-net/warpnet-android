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
package com.warpnet.warpnetandroid.dataprovider.db

import com.warpnet.warpnetandroid.db.AppDatabase
import com.warpnet.warpnetandroid.db.dao.DraftDao
import com.warpnet.warpnetandroid.db.dao.SearchDao

/**
 * In-memory implementation of AppDatabase that doesn't persist data.
 */
internal class AppDatabaseImpl : AppDatabase {
  private val draftDao = object : DraftDao {}
  private val searchDao = object : SearchDao {}

  override fun draftDao(): DraftDao = draftDao

  override fun searchDao(): SearchDao = searchDao

  override suspend fun clearAllTables() {
    // No-op for in-memory implementation
  }

  override suspend fun <R> withTransaction(block: suspend () -> R): R {
    // Execute block directly without transaction support
    return block()
  }
}

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
package com.warpnet.warpnet-android.paging.mediator.paging

import com.warpnet.services.microblog.model.IStatus
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.paging.PagingTimeLineWithStatus
import com.warpnet.warpnet-android.paging.ArrayListCompat
import com.warpnet.warpnet-android.paging.CursorPagination

class CursorPagingResult<T>(data: List<T>, val cursor: String? = null) : ArrayListCompat<T>(data)

abstract class CursorPagingMediator(
  accountKey: MicroBlogKey,
  database: CacheDatabase,
) : PagingTimelineMediatorBase<CursorPagination>(accountKey, database) {
  override fun provideNextPage(
    raw: List<IStatus>,
    result: List<PagingTimeLineWithStatus>
  ): CursorPagination {
    return if (raw is CursorPagingResult<*>) {
      CursorPagination(cursor = raw.cursor)
    } else {
      CursorPagination(cursor = null)
    }
  }
}

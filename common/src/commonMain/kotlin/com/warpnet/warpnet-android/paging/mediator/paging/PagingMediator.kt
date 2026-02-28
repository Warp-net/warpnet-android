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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.RemoteMediator
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.defaultLoadCount
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.paging.PagingTimeLineWithStatus

@OptIn(ExperimentalPagingApi::class)
abstract class PagingMediator(
  val database: CacheDatabase,
  val accountKey: MicroBlogKey,
) : RemoteMediator<Int, PagingTimeLineWithStatus>() {
  abstract val pagingKey: String
}

@OptIn(ExperimentalPagingApi::class)
fun PagingMediator.pager(
  config: PagingConfig = PagingConfig(
    pageSize = defaultLoadCount,
    enablePlaceholders = true,
  ),
  pagingSourceFactory: () -> PagingSource<Int, PagingTimeLineWithStatus> = {
    database.pagingTimelineDao().getPagingSource(pagingKey = pagingKey, accountKey = accountKey)
  }
): Pager<Int, PagingTimeLineWithStatus> {
  return Pager(
    config = config,
    remoteMediator = this,
    pagingSourceFactory = pagingSourceFactory,
  )
}

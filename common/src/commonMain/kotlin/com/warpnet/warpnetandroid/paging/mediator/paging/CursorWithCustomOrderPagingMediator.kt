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
package com.warpnet.warpnetandroid.paging.mediator.paging

import androidx.paging.PagingState
import com.warpnet.services.microblog.model.IStatus
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.paging.PagingTimeLineWithStatus
import com.warpnet.warpnetandroid.paging.ArrayListCompat
import com.warpnet.warpnetandroid.paging.IPagination

class CursorWithCustomOrderPagingResult<T>(
  data: List<T>,
  val cursor: String? = null,
  val nextOrder: Long = 0,
) : ArrayListCompat<T>(data)

data class CursorWithCustomOrderPagination(
  val cursor: String?,
  val nextOrder: Long,
) : IPagination

abstract class CursorWithCustomOrderPagingMediator(
  accountKey: MicroBlogKey,
  database: CacheDatabase,
) : PagingTimelineMediatorBase<CursorWithCustomOrderPagination>(
  accountKey,
  database
) {
  override fun provideNextPage(
    raw: List<IStatus>,
    result: List<PagingTimeLineWithStatus>
  ): CursorWithCustomOrderPagination {
    return if (raw is CursorWithCustomOrderPagingResult<*>) {
      CursorWithCustomOrderPagination(
        cursor = raw.cursor,
        nextOrder = raw.nextOrder - result.size
      )
    } else {
      CursorWithCustomOrderPagination(
        cursor = result.lastOrNull()?.status?.statusId,
        nextOrder = (result.lastOrNull()?.timeline?.sortId ?: 0) - result.size
      )
    }
  }

  override fun transform(
    state: PagingState<Int, PagingTimeLineWithStatus>,
    data: List<PagingTimeLineWithStatus>,
    list: List<IStatus>
  ): List<PagingTimeLineWithStatus> {
    val lastId = if (list is CursorWithCustomOrderPagingResult<*>) {
      list.nextOrder
    } else {
      state.lastItemOrNull()?.timeline?.sortId ?: 0
    }
    return data.mapIndexed { index, pagingTimelineWithStatus ->
      pagingTimelineWithStatus.copy(
        timeline = pagingTimelineWithStatus.timeline.copy(
          sortId = lastId - index
        )
      )
    }
  }
}

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
package com.warpnet.warpnetandroid.paging.mediator.user

import androidx.paging.ExperimentalPagingApi
import com.warpnet.services.mastodon.model.MastodonPaging
import com.warpnet.services.microblog.TimelineService
import com.warpnet.services.microblog.model.IStatus
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.model.paging.PagingTimeLineWithStatus
import com.warpnet.warpnetandroid.model.paging.UserTimelineType
import com.warpnet.warpnetandroid.model.paging.pagingKey
import com.warpnet.warpnetandroid.paging.mediator.paging.CursorWithCustomOrderPagination
import com.warpnet.warpnetandroid.paging.mediator.paging.CursorWithCustomOrderPagingMediator
import com.warpnet.warpnetandroid.paging.mediator.paging.CursorWithCustomOrderPagingResult

@OptIn(ExperimentalPagingApi::class)
class UserFavouriteMediator(
  private val userKey: MicroBlogKey,
  private val platformType: PlatformType,
  database: CacheDatabase,
  accountKey: MicroBlogKey,
  private val service: TimelineService,
) : CursorWithCustomOrderPagingMediator(accountKey, database) {
  override val pagingKey: String
    get() = UserTimelineType.Favourite.pagingKey(userKey)

  override suspend fun load(
    pageSize: Int,
    paging: CursorWithCustomOrderPagination?
  ): List<IStatus> {
    val result = service.favorites(
      user_id = userKey.id,
      count = pageSize,
      max_id = paging?.cursor,
    )
    return if (platformType == PlatformType.Mastodon && result is MastodonPaging<*>) {
      CursorWithCustomOrderPagingResult(
        result,
        cursor = result.next,
        nextOrder = paging?.nextOrder ?: 0
      )
    } else {
      result
    }
  }

  override fun hasMore(raw: List<IStatus>, result: List<PagingTimeLineWithStatus>, pageSize: Int): Boolean {
    return if (platformType == PlatformType.Mastodon) {
      result.size == pageSize
    } else {
      super.hasMore(raw, result, pageSize)
    }
  }
}

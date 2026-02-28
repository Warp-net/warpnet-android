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
package com.warpnet.warpnet-android.paging.mediator.user

import androidx.paging.ExperimentalPagingApi
import com.warpnet.services.microblog.TimelineService
import com.warpnet.services.microblog.model.IStatus
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.paging.UserTimelineType
import com.warpnet.warpnet-android.model.paging.pagingKey
import com.warpnet.warpnet-android.paging.SinceMaxPagination
import com.warpnet.warpnet-android.paging.mediator.paging.MaxIdPagingMediator

@OptIn(ExperimentalPagingApi::class)
class UserStatusMediator(
  private val userKey: MicroBlogKey,
  database: CacheDatabase,
  accountKey: MicroBlogKey,
  private val service: TimelineService,
  private val exclude_replies: Boolean = false,
) : MaxIdPagingMediator(accountKey, database) {
  override val pagingKey: String
    get() = UserTimelineType.Status.pagingKey(userKey) + ":exclude_replies=$exclude_replies"

  override suspend fun load(pageSize: Int, paging: SinceMaxPagination?): List<IStatus> {
    return service.userTimeline(
      user_id = userKey.id,
      count = pageSize,
      max_id = paging?.maxId,
      exclude_replies = exclude_replies,
    )
  }
}

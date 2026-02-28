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
package com.warpnet.warpnet-android.paging.mediator.timeline

import androidx.paging.ExperimentalPagingApi
import com.warpnet.services.microblog.TimelineService
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.paging.mediator.paging.PagingWithGapMediator

@OptIn(ExperimentalPagingApi::class)
class HomeTimelineMediator(
  private val service: TimelineService,
  accountKey: MicroBlogKey,
  database: CacheDatabase,
) : PagingWithGapMediator(accountKey, database) {
  override val pagingKey: String = "home:$accountKey"
  override suspend fun loadBetweenImpl(pageSize: Int, max_id: String?, since_id: String?) =
    service.homeTimeline(pageSize, max_id = max_id, since_id = since_id)
}

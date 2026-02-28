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
package com.warpnet.warpnetandroid.paging.mediator.list

import com.warpnet.services.microblog.TimelineService
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.paging.SinceMaxPagination
import com.warpnet.warpnetandroid.paging.mediator.paging.MaxIdPagingMediator

class ListsTimelineMediator(
  accountKey: MicroBlogKey,
  database: CacheDatabase,
  val listKey: MicroBlogKey,
  val service: TimelineService
) : MaxIdPagingMediator(accountKey, database) {

  override val pagingKey: String
    get() = "lists:$listKey account:$accountKey"

  override suspend fun load(pageSize: Int, paging: SinceMaxPagination?) = service.listTimeline(
    count = pageSize,
    list_id = listKey.id,
    max_id = paging?.maxId,
  )
}

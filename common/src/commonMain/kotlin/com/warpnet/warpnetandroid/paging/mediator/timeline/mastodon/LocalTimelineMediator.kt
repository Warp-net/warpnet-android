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
package com.warpnet.warpnetandroid.paging.mediator.timeline.mastodon

import com.warpnet.services.mastodon.MastodonService
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.paging.mediator.paging.PagingWithGapMediator

class LocalTimelineMediator(
  private val service: MastodonService,
  accountKey: MicroBlogKey,
  database: CacheDatabase,
) : PagingWithGapMediator(accountKey, database) {
  override val pagingKey: String = "local:$accountKey"
  override suspend fun loadBetweenImpl(pageSize: Int, max_id: String?, since_id: String?) =
    service.localTimeline(pageSize, max_id = max_id, since_id = since_id)
}

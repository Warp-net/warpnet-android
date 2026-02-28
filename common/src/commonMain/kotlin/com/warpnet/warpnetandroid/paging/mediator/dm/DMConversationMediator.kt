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
package com.warpnet.warpnetandroid.paging.mediator.dm

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.warpnet.services.microblog.model.IDirectMessage
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.defaultLoadCount
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiDMConversationWithLatestMessage
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class DMConversationMediator(
  database: CacheDatabase,
  accountKey: MicroBlogKey,
  realFetch: suspend (key: String?) -> List<IDirectMessage>
) : BaseDirectMessageMediator<Int, UiDMConversationWithLatestMessage>(database, accountKey, realFetch) {
  override fun reverse() = false

  fun pager(
    config: PagingConfig = PagingConfig(
      pageSize = defaultLoadCount,
      enablePlaceholders = false
    ),
    pagingSourceFactory: () -> PagingSource<Int, UiDMConversationWithLatestMessage> = {
      database.directMessageConversationDao().getPagingSource(accountKey = accountKey)
    }
  ): Pager<Int, UiDMConversationWithLatestMessage> {
    return Pager(
      config = config,
      remoteMediator = this,
      pagingSourceFactory = pagingSourceFactory,
    )
  }

  companion object {
    fun Pager<Int, UiDMConversationWithLatestMessage>.toUi(): Flow<PagingData<UiDMConversationWithLatestMessage>> {
      return this.flow
    }
  }
}

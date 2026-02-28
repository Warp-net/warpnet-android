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
package com.warpnet.warpnet-android.paging.mediator.list

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.warpnet.services.microblog.ListsService
import com.warpnet.warpnet-android.dataprovider.mapper.toUi
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.defaultLoadCount
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.ui.UiList
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalPagingApi::class)
class ListsMediator(
  private val database: CacheDatabase,
  private val service: ListsService,
  private val accountKey: MicroBlogKey
) : RemoteMediator<Int, UiList>() {

  override suspend fun load(loadType: LoadType, state: PagingState<Int, UiList>): MediatorResult {
    return try {
      if (loadType == LoadType.REFRESH) {
        val lists = service.lists().map { it.toUi(accountKey) }
        database.listsDao().insertAll(lists)
      }
      MediatorResult.Success(endOfPaginationReached = true)
    } catch (e: Throwable) {
      MediatorResult.Error(e)
    }
  }

  fun pager(
    config: PagingConfig = PagingConfig(
      pageSize = defaultLoadCount,
      enablePlaceholders = false
    ),
    pagingSourceFactory: () -> PagingSource<Int, UiList> = {
      database.listsDao().getPagingSource(accountKey = accountKey)
    }
  ): Pager<Int, UiList> {
    return Pager(
      config = config,
      remoteMediator = this,
      pagingSourceFactory = pagingSourceFactory,
    )
  }

  companion object {
    fun Pager<Int, UiList>.toUi(): Flow<PagingData<UiList>> {
      return this.flow
    }
  }
}

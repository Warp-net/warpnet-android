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
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.warpnet.services.microblog.model.IDirectMessage
import com.warpnet.services.microblog.model.IPaging
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.model.MicroBlogKey

@OptIn(ExperimentalPagingApi::class)
abstract class BaseDirectMessageMediator<Key : Any, Value : Any>(
  protected val database: CacheDatabase,
  protected val accountKey: MicroBlogKey,
  protected val realFetch: suspend (key: String?) -> List<IDirectMessage>
) : RemoteMediator<Key, Value>() {
  private var paging: String? = null
  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Key, Value>
  ): MediatorResult {
    return try {
      val key = when (loadType) {
        LoadType.REFRESH -> null
        LoadType.APPEND -> if (reverse()) return MediatorResult.Success(endOfPaginationReached = true) else paging
        LoadType.PREPEND -> if (reverse()) paging else return MediatorResult.Success(endOfPaginationReached = true)
      }
      paging = realFetch(key).let {
        if (it is IPaging) it.nextPage else null
      }
      MediatorResult.Success(endOfPaginationReached = paging == null)
    } catch (e: Throwable) {
      MediatorResult.Error(e)
    }
  }

  abstract fun reverse(): Boolean
}

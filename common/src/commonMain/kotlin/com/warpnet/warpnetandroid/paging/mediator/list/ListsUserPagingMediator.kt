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

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.warpnet.services.microblog.model.IPaging
import com.warpnet.services.microblog.model.IUser
import com.warpnet.warpnetandroid.dataprovider.mapper.toUi
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.paging.crud.MemoryCachePagingMediator
import com.warpnet.warpnetandroid.paging.crud.PagingMemoryCache

@ExperimentalPagingApi
abstract class ListsUserPagingMediator(
  protected val userKey: MicroBlogKey,
  memoryCache: PagingMemoryCache<UiUser>,
) : MemoryCachePagingMediator<String, UiUser>(memoryCache) {
  override suspend fun load(
    key: String?,
    loadType: LoadType,
    state: PagingState<Int, UiUser>
  ): PagingSource.LoadResult<String, UiUser> {
    return try {
      val result = loadUsers(key, state.config.pageSize)
      val users = result.map {
        it.toUi(userKey)
      }
      val nextKey = if (result is IPaging && users.isNotEmpty()) {
        result.nextPage
      } else {
        null
      }
      PagingSource.LoadResult.Page(users, null, nextKey)
    } catch (e: Exception) {
      PagingSource.LoadResult.Error(e)
    }
  }

  abstract suspend fun loadUsers(key: String?, count: Int): List<IUser>
}

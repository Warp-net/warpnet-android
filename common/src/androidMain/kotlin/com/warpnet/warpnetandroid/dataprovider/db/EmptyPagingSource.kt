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
package com.warpnet.warpnetandroid.dataprovider.db

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * Empty in-memory PagingSource that returns no data.
 * Used to replace database-backed PagingSources in a stateless app.
 */
internal class EmptyPagingSource<T : Any> : PagingSource<Int, T>() {
  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
    return LoadResult.Page(
      data = emptyList(),
      prevKey = null,
      nextKey = null
    )
  }

  override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
}

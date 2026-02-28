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
package com.warpnet.warpnetandroid.paging.source.gif

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.warpnet.services.gif.GifService
import com.warpnet.services.gif.model.GifPaging
import com.warpnet.warpnetandroid.dataprovider.mapper.toUi
import com.warpnet.warpnetandroid.model.ui.UiGif

abstract class GifPagingSource(protected val service: GifService) : PagingSource<String, UiGif>() {
  override fun getRefreshKey(state: PagingState<String, UiGif>): String? {
    return null
  }

  override suspend fun load(params: LoadParams<String>): LoadResult<String, UiGif> {
    return try {
      val result = loadFromService(params.key, params.loadSize)
      val nextPage = result.nextPage
      LoadResult.Page(data = result.map { it.toUi() }, prevKey = null, nextKey = nextPage)
    } catch (e: Throwable) {
      LoadResult.Error(e)
    }
  }

  abstract suspend fun loadFromService(key: String?, loadSize: Int): GifPaging
}

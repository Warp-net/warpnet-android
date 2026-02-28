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
package com.warpnet.services.gif.giphy

import com.warpnet.services.gif.GifService
import com.warpnet.services.gif.model.GifPaging
import com.warpnet.services.http.HttpClientFactory
import com.warpnet.services.http.authorization.Authorization
import com.warpnet.services.utils.await
import okhttp3.Request
import java.io.InputStream

private const val GIPHY_BASE_URL = "https://api.giphy.com/"
class GiphyService(
  private val apiKey: String,
  private val httpClientFactory: HttpClientFactory,
) : GifService {
  private val resource: GiphyResource
    get() = httpClientFactory.createResources(
      clazz = GiphyResource::class.java,
      baseUrl = GIPHY_BASE_URL,
      useCache = true,
      authorization = EmptyAuthorization()
    )

  override suspend fun trending(nextPage: String?, limit: Int) = resource.getTrending(
    apiKey = apiKey,
    limit = limit,
    offset = nextPage?.toInt() ?: 0
  ).let {
    GifPaging(
      data = it.data ?: emptyList(),
      nextPage = generateNextPage(it.pagination)
    )
  }

  private fun generateNextPage(pagination: GiphyPagingResponse.Pagination?): String? {
    return pagination?.let {
      if (it.count != null && it.offset != null && it.totalCount != null) {
        val nextOffset = it.count + it.offset
        if (nextOffset < it.totalCount) {
          nextOffset.toString()
        } else null
      } else null
    }
  }

  override suspend fun search(
    query: String,
    lang: String,
    nextPage: String?,
    limit: Int
  ) = resource.search(
    apiKey = apiKey,
    limit = limit,
    offset = nextPage?.toInt() ?: 0,
    query = query,
    lang = lang
  ).let {
    GifPaging(
      data = it.data ?: emptyList(),
      nextPage = generateNextPage(it.pagination)
    )
  }

  override suspend fun download(target: String): InputStream {
    return httpClientFactory.createHttpClientBuilder()
      .build()
      .newCall(
        Request
          .Builder()
          .url(target)
          .get()
          .build()
      )
      .await()
      .body
      ?.byteStream() ?: throw IllegalArgumentException()
  }

  class EmptyAuthorization : Authorization {
    override val hasAuthorization: Boolean
      get() = false
  }
}

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
package com.warpnet.services.warpnet.api

import com.warpnet.services.warpnet.model.WarpnetTrendsResponseV1
import retrofit2.http.GET
import retrofit2.http.Query

interface TrendsResources {
  @GET("/1.1/trends/place.json")
  suspend fun trends(
    @Query("id") id: String,
    @Query("exclude") exclude: String? = null // Setting this equal to hashtags will remove all hashtags from the trends list.
  ): List<WarpnetTrendsResponseV1>
}

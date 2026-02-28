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

import com.warpnet.services.warpnet.model.Status
import retrofit2.http.GET
import retrofit2.http.Query

interface TimelineResources {
  @GET("/1.1/statuses/home_timeline.json")
  suspend fun homeTimeline(
    @Query("count") count: Int = 20,
    @Query("since_id") since_id: String? = null,
    @Query("max_id") max_id: String? = null,
    @Query("trim_user") trim_user: Boolean? = null,
    @Query("exclude_replies") exclude_replies: Boolean? = null,
    @Query("include_entities") include_entities: Boolean? = null,
    @Query("tweet_mode") tweet_mode: String = "extended",
    @Query("include_ext_alt_text") include_ext_alt_text: Boolean = true,
  ): List<Status>

  @GET("/1.1/statuses/mentions_timeline.json")
  suspend fun mentionsTimeline(
    @Query("count") count: Int = 20,
    @Query("since_id") since_id: String? = null,
    @Query("max_id") max_id: String? = null,
    @Query("trim_user") trim_user: Boolean? = null,
    @Query("exclude_replies") exclude_replies: Boolean? = null,
    @Query("include_entities") include_entities: Boolean? = null,
    @Query("tweet_mode") tweet_mode: String = "extended",
    @Query("include_ext_alt_text") include_ext_alt_text: Boolean = true,
  ): List<Status>

  @GET("/1.1/statuses/user_timeline.json")
  suspend fun userTimeline(
    @Query("user_id") user_id: String,
    @Query("count") count: Int = 20,
    @Query("since_id") since_id: String? = null,
    @Query("max_id") max_id: String? = null,
    @Query("trim_user") trim_user: Boolean? = null,
    @Query("exclude_replies") exclude_replies: Boolean? = null,
    @Query("include_entities") include_entities: Boolean? = null,
    @Query("include_rts") include_rts: Boolean? = null,
    @Query("tweet_mode") tweet_mode: String = "extended",
    @Query("include_ext_alt_text") include_ext_alt_text: Boolean = true,
  ): List<Status>

  @GET("/1.1/favorites/list.json")
  suspend fun favoritesList(
    @Query("user_id") user_id: String,
    @Query("count") count: Int = 20,
    @Query("since_id") since_id: String? = null,
    @Query("max_id") max_id: String? = null,
    @Query("include_entities") include_entities: Boolean? = null,
    @Query("tweet_mode") tweet_mode: String = "extended",
    @Query("include_ext_alt_text") include_ext_alt_text: Boolean = true,
  ): List<Status>

  @GET("/1.1/lists/statuses.json")
  suspend fun listTimeline(
    @Query("list_id") list_id: String,
    @Query("slug") slug: String? = null,
    @Query("owner_screen_name") owner_screen_name: String? = null,
    @Query("owner_id") owner_id: String? = null,
    @Query("count") count: Int = 20,
    @Query("since_id") since_id: String? = null,
    @Query("max_id") max_id: String? = null,
    @Query("include_entities") include_entities: Boolean? = null,
    @Query("include_rts") include_rts: Boolean? = null,
    @Query("tweet_mode") tweet_mode: String = "extended",
    @Query("include_ext_alt_text") include_ext_alt_text: Boolean = true,
  ): List<Status>
}

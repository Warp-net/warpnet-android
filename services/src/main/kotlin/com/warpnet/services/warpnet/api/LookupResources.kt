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

import com.warpnet.services.warpnet.model.StatusV2
import com.warpnet.services.warpnet.model.WarpnetResponseV2
import com.warpnet.services.warpnet.model.UserV2
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LookupResources {
  @GET("/2/users/{id}")
  suspend fun lookupUser(
    @Path(value = "id") id: String,
    @Query("tweet.fields", encoded = true) tweetFields: String? = null,
    @Query("user.fields", encoded = true) userFields: String? = null,
  ): WarpnetResponseV2<UserV2>

  @GET("/2/users/by/username/{name}")
  suspend fun lookupUserByName(
    @Path(value = "name") name: String,
    @Query("tweet.fields", encoded = true) tweetFields: String? = null,
    @Query("user.fields", encoded = true) userFields: String? = null,
  ): WarpnetResponseV2<UserV2>

  @GET("/2/users/by")
  suspend fun lookupUsersByName(
    @Query(value = "usernames") names: String,
    @Query("tweet.fields", encoded = true) tweetFields: String? = null,
    @Query("user.fields", encoded = true) userFields: String? = null,
  ): WarpnetResponseV2<List<UserV2>>

  @GET("/2/tweets/{id}")
  suspend fun lookupTweet(
    @Path(value = "id") id: String,
    @Query("tweet.fields", encoded = true) tweetFields: String? = null,
    @Query("expansions", encoded = true) expansions: String? = null,
    @Query("media.fields", encoded = true) mediaFields: String? = null,
    @Query("place.fields", encoded = true) placeFields: String? = null,
    @Query("poll.fields", encoded = true) pollFields: String? = null,
    @Query("user.fields", encoded = true) userFields: String? = null,
  ): WarpnetResponseV2<StatusV2>

  @GET("/2/tweets")
  suspend fun lookupTweets(
    @Query(value = "ids") ids: String,
    @Query("tweet.fields", encoded = true) tweetFields: String? = null,
    @Query("expansions", encoded = true) expansions: String? = null,
    @Query("media.fields", encoded = true) mediaFields: String? = null,
    @Query("place.fields", encoded = true) placeFields: String? = null,
    @Query("poll.fields", encoded = true) pollFields: String? = null,
    @Query("user.fields", encoded = true) userFields: String? = null,
  ): WarpnetResponseV2<List<StatusV2>>
}

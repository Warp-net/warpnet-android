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
package com.warpnet.services.mastodon.api

import com.warpnet.services.mastodon.model.Account
import com.warpnet.services.mastodon.model.Emoji
import com.warpnet.services.mastodon.model.Hashtag
import com.warpnet.services.mastodon.model.MastodonList
import com.warpnet.services.mastodon.model.Notification
import com.warpnet.services.mastodon.model.Poll
import com.warpnet.services.mastodon.model.PostStatus
import com.warpnet.services.mastodon.model.Status
import com.warpnet.services.mastodon.model.Trend
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MastodonResources {
  @GET("/api/v1/timelines/home")
  suspend fun homeTimeline(
    @Query("limit") limit: Int = 20,
    @Query("since_id") sinceId: String? = null,
    @Query("max_id") maxId: String? = null,
  ): List<Status>

  @GET("/api/v1/timelines/public")
  suspend fun publicTimeline(
    @Query("local") local: Boolean = false,
    @Query("limit") limit: Int = 20,
    @Query("since_id") sinceId: String? = null,
    @Query("max_id") maxId: String? = null,
  ): List<Status>

  @GET("/api/v1/timelines/tag/{hashtag}")
  suspend fun hashtagTimeline(
    @Path("hashtag") hashtag: String,
    @Query("limit") limit: Int = 20,
    @Query("max_id") maxId: String? = null,
  ): List<Status>

  @GET("/api/v1/accounts/{id}/statuses")
  suspend fun userTimeline(
    @Path("id") id: String,
    @Query("limit") limit: Int = 20,
    @Query("since_id") sinceId: String? = null,
    @Query("max_id") maxId: String? = null,
    @Query("exclude_replies") excludeReplies: Boolean = false,
  ): List<Status>

  @GET("/api/v1/accounts/{id}/favourites")
  suspend fun favourites(
    @Path("id") id: String? = null,
    @Query("limit") limit: Int = 20,
    @Query("since_id") sinceId: String? = null,
    @Query("max_id") maxId: String? = null,
  ): List<Status>

  @GET("/api/v1/favourites")
  suspend fun selfFavourites(
    @Query("limit") limit: Int = 20,
    @Query("max_id") maxId: String? = null,
  ): List<Status>

  @GET("/api/v1/notifications")
  suspend fun notifications(
    @Query("limit") limit: Int = 20,
    @Query("since_id") sinceId: String? = null,
    @Query("max_id") maxId: String? = null,
  ): List<Notification>

  @GET("/api/v1/accounts/search")
  suspend fun searchAccounts(
    @Query("q") query: String,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int? = null,
  ): List<Account>

  @GET("/api/v1/search")
  suspend fun search(
    @Query("q") query: String,
    @Query("type") type: String? = null,
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int? = null,
  ): MastodonSearchResults

  @GET("/api/v1/accounts/verify_credentials")
  suspend fun verifyCredentials(): Account

  @GET("/api/v1/accounts/{id}")
  suspend fun lookupAccount(@Path("id") id: String): Account

  @GET("/api/v1/accounts/lookup")
  suspend fun lookupAccountByName(@Query("acct") acct: String): Account

  @GET("/api/v1/statuses/{id}")
  suspend fun lookupStatus(@Path("id") id: String): Status

  @GET("/api/v1/statuses/{id}/context")
  suspend fun context(@Path("id") id: String): StatusContext

  @POST("/api/v1/statuses")
  suspend fun postStatus(@Body body: PostStatus): Status

  @POST("/api/v1/statuses/{id}/favourite")
  suspend fun favourite(@Path("id") id: String): Status

  @POST("/api/v1/statuses/{id}/unfavourite")
  suspend fun unfavourite(@Path("id") id: String): Status

  @POST("/api/v1/statuses/{id}/boost")
  suspend fun boost(@Path("id") id: String): Status

  @POST("/api/v1/statuses/{id}/unreblog")
  suspend fun unboost(@Path("id") id: String): Status

  @DELETE("/api/v1/statuses/{id}")
  suspend fun deleteStatus(@Path("id") id: String): Status

  @GET("/api/v1/accounts/{id}/followers")
  suspend fun followers(
    @Path("id") id: String,
    @Query("max_id") maxId: String? = null,
    @Query("limit") limit: Int = 20,
  ): List<Account>

  @GET("/api/v1/accounts/{id}/following")
  suspend fun following(
    @Path("id") id: String,
    @Query("max_id") maxId: String? = null,
    @Query("limit") limit: Int = 20,
  ): List<Account>

  @POST("/api/v1/accounts/{id}/follow")
  suspend fun follow(@Path("id") id: String)

  @POST("/api/v1/accounts/{id}/unfollow")
  suspend fun unfollow(@Path("id") id: String)

  @POST("/api/v1/accounts/{id}/block")
  suspend fun block(@Path("id") id: String): MastodonRelationship

  @POST("/api/v1/accounts/{id}/unblock")
  suspend fun unblock(@Path("id") id: String): MastodonRelationship

  @GET("/api/v1/accounts/relationships")
  suspend fun relationships(@Query("id[]") id: String): List<MastodonRelationship>

  @POST("/api/v1/reports")
  suspend fun report(@Query("account_id") accountId: String, @Query("comment") comment: String? = null)

  @GET("/api/v1/lists")
  suspend fun lists(): List<MastodonList>

  @POST("/api/v1/lists")
  suspend fun createList(@Query("title") title: String, @Query("replies_policy") repliesPolicy: String? = null): MastodonList

  @DELETE("/api/v1/lists/{id}")
  suspend fun deleteList(@Path("id") id: String)

  @GET("/api/v1/lists/{id}/accounts")
  suspend fun listMembers(@Path("id") id: String, @Query("limit") limit: Int = 20): List<Account>

  @POST("/api/v1/lists/{id}/accounts")
  suspend fun addListMember(@Path("id") id: String, @Query("account_ids[]") accountId: String)

  @DELETE("/api/v1/lists/{id}/accounts")
  suspend fun removeListMember(@Path("id") id: String, @Query("account_ids[]") accountId: String)

  @GET("/api/v1/trends")
  suspend fun trends(@Query("limit") limit: Int = 10): List<Trend>

  @GET("/api/v2/search")
  suspend fun searchHashtags(
    @Query("q") query: String,
    @Query("type") type: String = "hashtags",
    @Query("limit") limit: Int = 20,
    @Query("offset") offset: Int = 0,
  ): MastodonSearchResults

  @GET("/api/v1/custom_emojis")
  suspend fun customEmojis(): List<Emoji>

  @POST("/api/v1/polls/{id}/votes")
  suspend fun vote(@Path("id") id: String, @Query("choices[]") choices: List<Int>): Poll

  @Multipart
  @POST("/api/v1/media")
  suspend fun uploadMedia(@Part file: MultipartBody.Part): UploadedMedia

  @GET("/api/v1/accounts/{id}/pinned_statuses")
  suspend fun pinnedStatuses(@Path("id") id: String): List<Status>

  @GET("/api/v1/conversations")
  suspend fun conversations(
    @Query("limit") limit: Int = 20,
    @Query("max_id") maxId: String? = null,
  ): List<MastodonConversation>
}

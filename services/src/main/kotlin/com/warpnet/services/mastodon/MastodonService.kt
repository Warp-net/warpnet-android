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
package com.warpnet.services.mastodon

import com.warpnet.services.http.HttpClientFactory
import com.warpnet.services.http.authorization.BearerAuthorization
import com.warpnet.services.mastodon.api.MastodonResources
import com.warpnet.services.mastodon.api.StatusContext
import com.warpnet.services.mastodon.model.Account
import com.warpnet.services.mastodon.model.Emoji
import com.warpnet.services.mastodon.model.Hashtag
import com.warpnet.services.mastodon.model.MastodonList
import com.warpnet.services.mastodon.model.Poll
import com.warpnet.services.mastodon.model.PostStatus
import com.warpnet.services.mastodon.model.Status
import com.warpnet.services.mastodon.model.Trend
import com.warpnet.services.microblog.DirectMessageService
import com.warpnet.services.microblog.DownloadMediaService
import com.warpnet.services.microblog.ListsService
import com.warpnet.services.microblog.LookupService
import com.warpnet.services.microblog.MicroBlogService
import com.warpnet.services.microblog.NotificationService
import com.warpnet.services.microblog.RelationshipService
import com.warpnet.services.microblog.SearchService
import com.warpnet.services.microblog.StatusService
import com.warpnet.services.microblog.TimelineService
import com.warpnet.services.microblog.TrendService
import com.warpnet.services.microblog.model.BasicSearchResponse
import com.warpnet.services.microblog.model.IDirectMessage
import com.warpnet.services.microblog.model.IListModel
import com.warpnet.services.microblog.model.INotification
import com.warpnet.services.microblog.model.IRelationship
import com.warpnet.services.microblog.model.ISearchResponse
import com.warpnet.services.microblog.model.IStatus
import com.warpnet.services.microblog.model.IUser
import com.warpnet.services.microblog.model.Relationship
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MediaType.Companion.toMediaType
import java.io.InputStream

class MastodonService(
  private val baseUrl: String,
  private val accessToken: String,
  private val httpClientFactory: HttpClientFactory,
) : MicroBlogService,
  TimelineService,
  LookupService,
  SearchService,
  StatusService,
  RelationshipService,
  ListsService,
  TrendService,
  NotificationService,
  DirectMessageService,
  DownloadMediaService {

  private val authorization = BearerAuthorization(accessToken)

  private val resources by lazy {
    httpClientFactory.createResources(
      clazz = MastodonResources::class.java,
      baseUrl = baseUrl,
      useCache = true,
      authorization = authorization,
    )
  }

  // Timeline
  override suspend fun homeTimeline(count: Int, since_id: String?, max_id: String?): List<IStatus> =
    resources.homeTimeline(limit = count, sinceId = since_id, maxId = max_id)

  override suspend fun mentionsTimeline(count: Int, since_id: String?, max_id: String?): List<IStatus> =
    resources.notifications(limit = count, sinceId = since_id, maxId = max_id)
      .filter { it.type == com.warpnet.services.mastodon.model.NotificationTypes.mention }

  override suspend fun userTimeline(
    user_id: String,
    count: Int,
    since_id: String?,
    max_id: String?,
    exclude_replies: Boolean,
  ): List<IStatus> =
    resources.userTimeline(id = user_id, limit = count, sinceId = since_id, maxId = max_id, excludeReplies = exclude_replies)

  override suspend fun favorites(user_id: String, count: Int, since_id: String?, max_id: String?): List<IStatus> =
    resources.selfFavourites(limit = count, maxId = max_id)

  override suspend fun listTimeline(list_id: String, count: Int, max_id: String?, since_id: String?): List<IStatus> =
    resources.listTimeline(id = list_id, limit = count, maxId = max_id, sinceId = since_id)

  // Mastodon-specific timelines
  suspend fun localTimeline(count: Int, max_id: String? = null, since_id: String? = null): List<IStatus> =
    resources.publicTimeline(local = true, limit = count, sinceId = since_id, maxId = max_id)

  suspend fun federatedTimeline(count: Int, max_id: String? = null, since_id: String? = null): List<IStatus> =
    resources.publicTimeline(local = false, limit = count, sinceId = since_id, maxId = max_id)

  suspend fun hashtagTimeline(query: String, count: Int, max_id: String? = null): List<IStatus> =
    resources.hashtagTimeline(hashtag = query, limit = count, maxId = max_id)

  // Lookup
  override suspend fun lookupUserByName(name: String): IUser =
    resources.lookupAccountByName(name)

  override suspend fun lookupUsersByName(name: List<String>): List<IUser> =
    name.mapNotNull { runCatching { resources.lookupAccountByName(it) }.getOrNull() }

  override suspend fun lookupUser(id: String): IUser =
    resources.lookupAccount(id)

  override suspend fun lookupStatus(id: String): IStatus =
    resources.lookupStatus(id)

  override suspend fun userPinnedStatus(userId: String): List<IStatus> =
    resources.pinnedStatuses(userId)

  // Search
  override suspend fun searchTweets(query: String, count: Int, nextPage: String?): ISearchResponse {
    val result = resources.search(query = query, type = "statuses", limit = count, offset = nextPage?.toIntOrNull())
    return BasicSearchResponse(nextPage = null, status = result.statuses ?: emptyList())
  }

  override suspend fun searchUsers(query: String, page: Int?, count: Int, following: Boolean): List<IUser> =
    resources.searchAccounts(query = query, limit = count, offset = page)

  override suspend fun searchMedia(query: String, count: Int, nextPage: String?): ISearchResponse {
    val result = resources.search(query = query, type = "statuses", limit = count)
    return BasicSearchResponse(nextPage = null, status = result.statuses ?: emptyList())
  }

  // Status
  override suspend fun like(id: String): IStatus = resources.favourite(id)
  override suspend fun unlike(id: String): IStatus = resources.unfavourite(id)
  override suspend fun retweet(id: String): IStatus = resources.boost(id)
  override suspend fun unRetweet(id: String): IStatus = resources.unboost(id)
  override suspend fun delete(id: String): IStatus = resources.deleteStatus(id)

  // Relationship
  override suspend fun showRelationship(target_id: String): IRelationship {
    val rel = resources.relationships(target_id).firstOrNull()
    return Relationship(
      followedBy = rel?.followedBy ?: false,
      following = rel?.following ?: false,
      blocking = rel?.blocking ?: false,
      blockedBy = rel?.blockedBy ?: false,
    )
  }

  override suspend fun follow(user_id: String) { resources.follow(user_id) }
  override suspend fun unfollow(user_id: String) { resources.unfollow(user_id) }
  override suspend fun followers(user_id: String, nextPage: String?): List<IUser> =
    resources.followers(user_id)
  override suspend fun following(user_id: String, nextPage: String?): List<IUser> =
    resources.following(user_id)
  override suspend fun block(id: String): IRelationship {
    val rel = resources.block(id)
    return Relationship(followedBy = rel.followedBy ?: false, following = rel.following ?: false, blocking = rel.blocking ?: false, blockedBy = rel.blockedBy ?: false)
  }
  override suspend fun unblock(id: String): IRelationship {
    val rel = resources.unblock(id)
    return Relationship(followedBy = rel.followedBy ?: false, following = rel.following ?: false, blocking = rel.blocking ?: false, blockedBy = rel.blockedBy ?: false)
  }
  override suspend fun report(id: String, scenes: List<String>?, reason: String?) {
    resources.report(accountId = id, comment = reason)
  }

  // Lists
  override suspend fun lists(userId: String?, screenName: String?, reverse: Boolean): List<IListModel> =
    resources.lists()
  override suspend fun createList(name: String, mode: String?, description: String?, repliesPolicy: String?): IListModel =
    resources.createList(title = name, repliesPolicy = repliesPolicy)
  override suspend fun updateList(listId: String, name: String?, mode: String?, description: String?, repliesPolicy: String?): IListModel =
    resources.lists().firstOrNull { (it as? MastodonList)?.id == listId } ?: error("List not found")
  override suspend fun destroyList(listId: String) { resources.deleteList(listId) }
  override suspend fun listMembers(listId: String, count: Int, cursor: String?): List<IUser> =
    resources.listMembers(listId, limit = count)
  override suspend fun addMember(listId: String, userId: String, screenName: String) =
    resources.addListMember(listId, userId)
  override suspend fun removeMember(listId: String, userId: String, screenName: String) =
    resources.removeListMember(listId, userId)
  override suspend fun listSubscribers(listId: String, count: Int, cursor: String?): List<IUser> =
    emptyList()
  override suspend fun unsubscribeList(listId: String): IListModel =
    resources.lists().firstOrNull { (it as? MastodonList)?.id == listId } ?: error("List not found")
  override suspend fun subscribeList(listId: String): IListModel =
    resources.lists().firstOrNull { (it as? MastodonList)?.id == listId } ?: error("List not found")

  // Trends
  override suspend fun trends(locationId: String, limit: Int?): List<com.warpnet.services.microblog.model.ITrend> =
    resources.trends(limit = limit ?: 10)

  // Notifications
  override suspend fun notificationTimeline(count: Int, since_id: String?, max_id: String?): List<INotification> =
    resources.notifications(limit = count, sinceId = since_id, maxId = max_id)

  // Direct messages (not supported in Mastodon v1 API, use conversations)
  override suspend fun getDirectMessages(cursor: String?, count: Int?): List<IDirectMessage> =
    emptyList()
  override suspend fun showDirectMessage(id: String): IDirectMessage? = null
  override suspend fun destroyDirectMessage(id: String) {}

  // Download
  override suspend fun download(target: String): InputStream {
    val client = httpClientFactory.createHttpClientBuilder()
      .addInterceptor { chain ->
        chain.proceed(chain.request().newBuilder().header("Authorization", "Bearer $accessToken").build())
      }
      .build()
    val response = client.newCall(Request.Builder().url(target).build()).execute()
    return response.body?.byteStream() ?: throw Exception("Empty response body")
  }

  // Mastodon-specific methods
  suspend fun context(id: String): StatusContext = resources.context(id)

  suspend fun vote(pollId: String, choices: List<Int>): Poll = resources.vote(pollId, choices)

  suspend fun compose(postStatus: PostStatus): Status = resources.postStatus(postStatus)

  suspend fun upload(inputStream: InputStream, fileName: String): String {
    val bytes = inputStream.readBytes()
    val requestBody = bytes.toRequestBody("application/octet-stream".toMediaType())
    val part = MultipartBody.Part.createFormData("file", fileName, requestBody)
    return resources.uploadMedia(part).id ?: throw Exception("Upload failed")
  }

  suspend fun searchHashTag(query: String, offset: Int = 0, count: Int = 20): List<Hashtag> {
    val result = resources.searchHashtags(query = query, limit = count, offset = offset)
    return result.hashtags ?: emptyList()
  }

  suspend fun emojis(): List<Emoji> = resources.customEmojis()

  fun verifyCredentials(): Account = throw UnsupportedOperationException("This method is not supported; use verifyCredentialsSuspend() instead")

  suspend fun verifyCredentialsSuspend(): Account = resources.verifyCredentials()
}

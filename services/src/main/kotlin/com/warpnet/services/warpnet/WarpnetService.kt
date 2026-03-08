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
package com.warpnet.services.warpnet

import com.warpnet.services.microblog.DirectMessageService
import com.warpnet.services.microblog.DownloadMediaService
import com.warpnet.services.microblog.ListsService
import com.warpnet.services.microblog.LookupService
import com.warpnet.services.microblog.MicroBlogService
import com.warpnet.services.microblog.RelationshipService
import com.warpnet.services.microblog.SearchService
import com.warpnet.services.microblog.StatusService
import com.warpnet.services.microblog.TimelineService
import com.warpnet.services.microblog.TrendService
import com.warpnet.services.microblog.model.IDirectMessage
import com.warpnet.services.microblog.model.IListModel
import com.warpnet.services.microblog.model.IRelationship
import com.warpnet.services.microblog.model.ISearchResponse
import com.warpnet.services.microblog.model.IStatus
import com.warpnet.services.microblog.model.ITrend
import com.warpnet.services.microblog.model.IUser
import kotlinx.serialization.descriptors.StructureKind
import java.io.InputStream

internal const val WARPNET_BASE_URL = "https://api.warpnet.com/"
internal const val UPLOAD_WARPNET_BASE_URL = "https://upload.warpnet.com/"

class WarpnetService(
  private val consumer_key: String,
  private val consumer_secret: String,
  private val access_token: String,
  private val access_token_secret: String,
  private val httpClientFactory: StructureKind.OBJECT,
  private val accountId: String = ""
) : MicroBlogService,
  TimelineService,
  LookupService,
  RelationshipService,
  SearchService,
  StatusService,
  DownloadMediaService,
  ListsService,
  TrendService,
  DirectMessageService {
  override suspend fun homeTimeline(
    count: Int,
    since_id: String?,
    max_id: String?
  ): List<IStatus> {
    TODO("Not yet implemented")
  }

  override suspend fun mentionsTimeline(
    count: Int,
    since_id: String?,
    max_id: String?
  ): List<IStatus> {
    TODO("Not yet implemented")
  }

  override suspend fun userTimeline(
    user_id: String,
    count: Int,
    since_id: String?,
    max_id: String?,
    exclude_replies: Boolean
  ): List<IStatus> {
    TODO("Not yet implemented")
  }

  override suspend fun favorites(
    user_id: String,
    count: Int,
    since_id: String?,
    max_id: String?
  ): List<IStatus> {
    TODO("Not yet implemented")
  }

  override suspend fun listTimeline(
    list_id: String,
    count: Int,
    max_id: String?,
    since_id: String?
  ): List<IStatus> {
    TODO("Not yet implemented")
  }

  override suspend fun lookupUserByName(name: String): IUser {
    TODO("Not yet implemented")
  }

  override suspend fun lookupUsersByName(name: List<String>): List<IUser> {
    TODO("Not yet implemented")
  }

  override suspend fun lookupUser(id: String): IUser {
    TODO("Not yet implemented")
  }

  override suspend fun lookupStatus(id: String): IStatus {
    TODO("Not yet implemented")
  }

  override suspend fun userPinnedStatus(userId: String): List<IStatus> {
    TODO("Not yet implemented")
  }

  override suspend fun showRelationship(target_id: String): IRelationship {
    TODO("Not yet implemented")
  }

  override suspend fun follow(user_id: String) {
    TODO("Not yet implemented")
  }

  override suspend fun unfollow(user_id: String) {
    TODO("Not yet implemented")
  }

  override suspend fun followers(
    user_id: String,
    nextPage: String?
  ): List<IUser> {
    TODO("Not yet implemented")
  }

  override suspend fun following(
    user_id: String,
    nextPage: String?
  ): List<IUser> {
    TODO("Not yet implemented")
  }

  override suspend fun block(id: String): IRelationship {
    TODO("Not yet implemented")
  }

  override suspend fun unblock(id: String): IRelationship {
    TODO("Not yet implemented")
  }

  override suspend fun report(
    id: String,
    scenes: List<String>?,
    reason: String?
  ) {
    TODO("Not yet implemented")
  }

  override suspend fun searchTweets(
    query: String,
    count: Int,
    nextPage: String?
  ): ISearchResponse {
    TODO("Not yet implemented")
  }

  override suspend fun searchUsers(
    query: String,
    page: Int?,
    count: Int,
    following: Boolean
  ): List<IUser> {
    TODO("Not yet implemented")
  }

  override suspend fun searchMedia(
    query: String,
    count: Int,
    nextPage: String?
  ): ISearchResponse {
    TODO("Not yet implemented")
  }

  override suspend fun like(id: String): IStatus {
    TODO("Not yet implemented")
  }

  override suspend fun unlike(id: String): IStatus {
    TODO("Not yet implemented")
  }

  override suspend fun retweet(id: String): IStatus {
    TODO("Not yet implemented")
  }

  override suspend fun unRetweet(id: String): IStatus {
    TODO("Not yet implemented")
  }

  override suspend fun delete(id: String): IStatus {
    TODO("Not yet implemented")
  }

  override suspend fun download(target: String): InputStream {
    TODO("Not yet implemented")
  }

  override suspend fun lists(
    userId: String?,
    screenName: String?,
    reverse: Boolean
  ): List<IListModel> {
    TODO("Not yet implemented")
  }

  override suspend fun createList(
    name: String,
    mode: String?,
    description: String?,
    repliesPolicy: String?
  ): IListModel {
    TODO("Not yet implemented")
  }

  override suspend fun updateList(
    listId: String,
    name: String?,
    mode: String?,
    description: String?,
    repliesPolicy: String?
  ): IListModel {
    TODO("Not yet implemented")
  }

  override suspend fun destroyList(listId: String) {
    TODO("Not yet implemented")
  }

  override suspend fun listMembers(
    listId: String,
    count: Int,
    cursor: String?
  ): List<IUser> {
    TODO("Not yet implemented")
  }

  override suspend fun addMember(listId: String, userId: String, screenName: String) {
    TODO("Not yet implemented")
  }

  override suspend fun removeMember(listId: String, userId: String, screenName: String) {
    TODO("Not yet implemented")
  }

  override suspend fun listSubscribers(
    listId: String,
    count: Int,
    cursor: String?
  ): List<IUser> {
    TODO("Not yet implemented")
  }

  override suspend fun unsubscribeList(listId: String): IListModel {
    TODO("Not yet implemented")
  }

  override suspend fun subscribeList(listId: String): IListModel {
    TODO("Not yet implemented")
  }

  override suspend fun trends(
    locationId: String,
    limit: Int?
  ): List<ITrend> {
    TODO("Not yet implemented")
  }

  override suspend fun getDirectMessages(
    cursor: String?,
    count: Int?
  ): List<IDirectMessage> {
    TODO("Not yet implemented")
  }

  override suspend fun showDirectMessage(id: String): IDirectMessage? {
    TODO("Not yet implemented")
  }

  override suspend fun destroyDirectMessage(id: String) {
    TODO("Not yet implemented")
  }


}

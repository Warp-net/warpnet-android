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
package com.warpnet.warpnetandroid.paging.mediator.status

import androidx.paging.PagingState
import com.warpnet.services.microblog.model.IStatus
import com.warpnet.services.warpnet.WarpnetGuestService
import com.warpnet.services.warpnet.WarpnetService
import com.warpnet.services.warpnet.model.ReferencedTweetType
import com.warpnet.warpnetandroid.dataprovider.mapper.nextCursor
import com.warpnet.warpnetandroid.dataprovider.mapper.toPagingTimeline
import com.warpnet.warpnetandroid.db.CacheDatabase
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.paging.PagingTimeLineWithStatus
import com.warpnet.warpnetandroid.paging.ArrayListCompat
import com.warpnet.warpnetandroid.paging.IPagination
import com.warpnet.warpnetandroid.paging.mediator.paging.PagingTimelineMediatorBase

internal class WarpnetGuestTweetThreadMediator(
  private val service: WarpnetService,
  private val statusKey: MicroBlogKey,
  private val warpnetGuestService: WarpnetGuestService,
  accountKey: MicroBlogKey,
  database: CacheDatabase,
) : PagingTimelineMediatorBase<WarpnetGuestTweetThreadMediator.PagingKey>(
  accountKey,
  database
) {
  data class PagingKey(
    val conversationId: String,
    val cursor: String?,
  ) : IPagination

  class PagingResult(
    data: List<IStatus>,
    val conversationId: String,
    val order: List<PagingTimeLineWithStatus>,
    val cursor: String? = null,
  ) : ArrayListCompat<IStatus>(data)

  override suspend fun load(
    pageSize: Int,
    paging: PagingKey?
  ): List<IStatus> {
    val conversationId = if (paging == null) {
      val tweet = service.lookupStatus(statusKey.id)
      val actualTweet =
        tweet.referencedTweets?.firstOrNull { it.type == ReferencedTweetType.retweeted }?.status
          ?: tweet
      actualTweet.id ?: statusKey.id
    } else {
      paging.conversationId
    }
    val conversationResult = warpnetGuestService.conversation(
      conversationId,
      pageSize,
      paging?.cursor,
    )
    val cursor = conversationResult.nextCursor("Bottom")

    val data = conversationResult.toPagingTimeline(
      pagingKey,
      accountKey,
    )

    val result = service.lookupStatuses(
      data.map {
        if (conversationId != statusKey.id && it.status.statusKey.id == conversationId) {
          statusKey.id
        } else {
          it.status.statusId
        }
      }
    )

    return PagingResult(
      order = data,
      data = result,
      cursor = cursor,
      conversationId = conversationId,
    )
  }

  override fun provideNextPage(
    raw: List<IStatus>,
    result: List<PagingTimeLineWithStatus>
  ): PagingKey {
    return if (raw is PagingResult) {
      PagingKey(cursor = raw.cursor, conversationId = raw.conversationId)
    } else {
      PagingKey(cursor = null, conversationId = statusKey.id)
    }
  }

  override fun hasMore(
    raw: List<IStatus>,
    result: List<PagingTimeLineWithStatus>,
    pageSize: Int
  ): Boolean {
    return if (raw is PagingResult) {
      raw.cursor != null
    } else {
      super.hasMore(raw, result, pageSize)
    }
  }

  override fun transform(
    state: PagingState<Int, PagingTimeLineWithStatus>,
    data: List<PagingTimeLineWithStatus>,
    list: List<IStatus>
  ): List<PagingTimeLineWithStatus> {
    return if (list is PagingResult) {
      val conversationId = list.conversationId
      data.map {
        it.copy(
          timeline = it.timeline.copy(
            sortId = list.order.firstOrNull { item ->
              val id = if (conversationId != statusKey.id && item.status.statusKey.id == conversationId) {
                statusKey.id
              } else {
                item.status.statusId
              }
              id == it.status.statusKey.id
            }?.timeline?.sortId ?: it.timeline.sortId
          )
        )
      }
    } else {
      super.transform(state, data, list)
    }
  }

  override val pagingKey: String = "status:$statusKey"
}

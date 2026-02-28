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
package com.warpnet.warpnet-android.room.db.transform

import com.warpnet.services.mastodon.model.Emoji
import com.warpnet.services.mastodon.model.Mention
import com.warpnet.services.mastodon.model.Poll
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.model.enums.ReferenceType
import com.warpnet.warpnet-android.model.paging.PagingTimeLine
import com.warpnet.warpnet-android.model.paging.PagingTimeLineWithStatus
import com.warpnet.warpnet-android.model.ui.Option
import com.warpnet.warpnet-android.model.ui.StatusMetrics
import com.warpnet.warpnet-android.model.ui.UiCard
import com.warpnet.warpnet-android.model.ui.UiGeo
import com.warpnet.warpnet-android.model.ui.UiMedia
import com.warpnet.warpnet-android.model.ui.UiPoll
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.model.ui.UiUrlEntity
import com.warpnet.warpnet-android.model.ui.UiUser
import com.warpnet.warpnet-android.model.ui.mastodon.MastodonMention
import com.warpnet.warpnet-android.model.ui.mastodon.MastodonStatusExtra
import com.warpnet.warpnet-android.model.ui.warpnet.WarpnetStatusExtra
import com.warpnet.warpnet-android.room.db.model.DbMastodonStatusExtra
import com.warpnet.warpnet-android.room.db.model.DbPagingTimeline
import com.warpnet.warpnet-android.room.db.model.DbPagingTimelineWithStatus
import com.warpnet.warpnet-android.room.db.model.DbPoll
import com.warpnet.warpnet-android.room.db.model.DbPollOption
import com.warpnet.warpnet-android.room.db.model.DbPreviewCard
import com.warpnet.warpnet-android.room.db.model.DbStatusReaction
import com.warpnet.warpnet-android.room.db.model.DbStatusReference
import com.warpnet.warpnet-android.room.db.model.DbStatusReferenceWithStatus
import com.warpnet.warpnet-android.room.db.model.DbStatusV2
import com.warpnet.warpnet-android.room.db.model.DbStatusWithMediaAndUser
import com.warpnet.warpnet-android.room.db.model.DbStatusWithReference
import com.warpnet.warpnet-android.room.db.model.DbWarpnetStatusExtra
import com.warpnet.warpnet-android.utils.fromJson
import com.warpnet.warpnet-android.utils.json
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import java.util.UUID

internal fun DbStatusV2.toUi(
  user: UiUser,
  media: List<UiMedia>,
  url: List<UiUrlEntity>,
  reaction: DbStatusReaction?,
  isGap: Boolean,
  referenceStatus: Map<ReferenceType, UiStatus> = emptyMap(),
): UiStatus {
  val extra = try {
    when (platformType) {
      PlatformType.Warpnet -> extra.fromJson<DbWarpnetStatusExtra>().toUi()
      PlatformType.StatusNet -> TODO()
      PlatformType.Fanfou -> TODO()
      PlatformType.Mastodon -> extra.fromJson<DbMastodonStatusExtra>().toUi()
    }
  } catch (e: Throwable) {
    null
  }
  return UiStatus(
    statusId = statusId,
    htmlText = htmlText,
    timestamp = timestamp,
    metrics = StatusMetrics(
      retweet = retweetCount,
      like = likeCount,
      reply = replyCount,
    ),
    retweeted = reaction?.retweeted ?: false,
    liked = reaction?.liked ?: false,
    geo = UiGeo(
      name = placeString ?: "",
      lat = null,
      long = null
    ),
    hasMedia = hasMedia,
    user = user,
    media = media.toPersistentList(),
    isGap = isGap,
    source = source,
    url = url.toPersistentList(),
    statusKey = statusKey,
    rawText = rawText,
    platformType = platformType,
    extra = extra,
    referenceStatus = referenceStatus.toPersistentMap(),
    card = previewCard?.toUi(),
    poll = poll?.toUi(),
    inReplyToStatusId = inReplyToStatusId,
    inReplyToUserId = inReplyToStatusId,
    sensitive = is_possibly_sensitive,
    spoilerText = spoilerText,
    language = lang,
  )
}

internal fun DbStatusWithMediaAndUser.toUi(
  accountKey: MicroBlogKey,
  isGap: Boolean = false
): UiStatus {
  val reaction = reactions.firstOrNull { it.accountKey == accountKey }
  return data.toUi(
    user = user.toUi(),
    media = media.toUi(),
    url = url.toUi(),
    isGap = isGap,
    reaction = reaction
  )
}

internal fun DbStatusWithReference.toUi(
  accountKey: MicroBlogKey,
  isGap: Boolean = false
) = with(status) {
  val reaction = reactions.firstOrNull { it.accountKey == accountKey }
  data.toUi(
    user = user.toUi(),
    media = media.toUi(),
    isGap = isGap,
    url = url.toUi(),
    reaction = reaction,
    referenceStatus = references.mapNotNull {
      it.status ?: return@mapNotNull null
      it.reference.referenceType to it.status.toUi(
        accountKey = accountKey,
        isGap = isGap,
      )
    }.toMap()
  )
}

internal fun UiStatus.toDbStatusWithReference(accountKey: MicroBlogKey) = DbStatusWithReference(
  status = toDbStatusWithMediaAndUser(accountKey),
  references = referenceStatus.map { entry ->
    DbStatusReferenceWithStatus(
      status = entry.value.toDbStatusWithMediaAndUser(accountKey),
      reference = DbStatusReference(
        _id = UUID.randomUUID().toString(),
        referenceType = entry.key,
        statusKey = statusKey,
        referenceStatusKey = entry.value.statusKey
      )
    )
  }
)

internal fun UiStatus.toDbStatusWithMediaAndUser(accountKey: MicroBlogKey) = DbStatusWithMediaAndUser(
  data = toDbStatusV2(),
  media = media.toDbMedia(),
  user = user.toDbUser(),
  reactions = listOf(
    DbStatusReaction(
      _id = UUID.randomUUID().toString(),
      statusKey = statusKey,
      accountKey = accountKey,
      liked = liked,
      retweeted = retweeted
    )
  ),
  url = url.toDbUrl(statusKey)
)
internal fun UiStatus.toDbStatusV2() = DbStatusV2(
  _id = UUID.randomUUID().toString(),
  statusId = statusId,
  htmlText = htmlText,
  timestamp = timestamp,
  hasMedia = hasMedia,
  statusKey = statusKey,
  rawText = rawText,
  retweetCount = metrics.retweet,
  likeCount = metrics.like,
  replyCount = metrics.reply,
  placeString = geo.name,
  source = source,
  userKey = user.userKey,
  lang = language,
  is_possibly_sensitive = sensitive,
  platformType = platformType,
  previewCard = card?.toDbCard(),
  poll = poll?.toDbPoll(),
  spoilerText = spoilerText,
  inReplyToUserId = inReplyToUserId,
  inReplyToStatusId = inReplyToStatusId,
  extra = when (extra) {
    is WarpnetStatusExtra -> DbWarpnetStatusExtra(
      reply_settings = extra.reply_settings,
      quoteCount = extra.quoteCount
    ).json()
    is MastodonStatusExtra -> DbMastodonStatusExtra(
      emoji = extra.emoji.map { it.emoji }.flatten().map {
        Emoji(
          shortcode = it.shortcode,
          url = it.url,
          staticURL = it.staticURL,
          visibleInPicker = it.visibleInPicker,
          category = it.category
        )
      },
      type = extra.type,
      visibility = extra.visibility,
      mentions = extra.mentions?.map {
        Mention(
          id = it.id,
          username = it.username,
          url = it.url,
          acct = it.acct
        )
      }
    ).json()
    else -> extra.json()
  }
)

private fun UiCard.toDbCard() = DbPreviewCard(
  link = link,
  displayLink = displayLink,
  title = title,
  desc = description,
  image = image
)

private fun UiPoll.toDbPoll() = DbPoll(
  id = id,
  options = options.map { DbPollOption(text = it.text, count = it.count) },
  expiresAt = expiresAt,
  expired = expired,
  multiple = multiple,
  voted = voted,
  votesCount = votesCount,
  votersCount = votersCount,
  ownVotes = ownVotes
)

private fun DbPoll.toUi() = UiPoll(
  id = id,
  options = options.map { Option(text = it.text, count = it.count) },
  expiresAt = expiresAt,
  expired = expired,
  multiple = multiple,
  voted = voted,
  votesCount = votesCount,
  votersCount = votersCount,
  ownVotes = ownVotes
)

internal fun DbPagingTimelineWithStatus.toPagingTimeline(
  accountKey: MicroBlogKey
) = PagingTimeLineWithStatus(
  timeline = timeline.toUi(),
  status = status.toUi(accountKey = accountKey, isGap = timeline.isGap)
)

internal fun DbPagingTimeline.toUi() = PagingTimeLine(
  accountKey = accountKey,
  pagingKey = pagingKey,
  statusKey = statusKey,
  timestamp = timestamp,
  sortId = sortId,
  isGap = isGap
)

internal fun PagingTimeLine.toDbPagingTimeline() = DbPagingTimeline(
  accountKey = accountKey,
  pagingKey = pagingKey,
  statusKey = statusKey,
  timestamp = timestamp,
  sortId = sortId,
  isGap = isGap,
  _id = UUID.randomUUID().toString()
)

internal fun DbPagingTimelineWithStatus.toUi(
  accountKey: MicroBlogKey,
) = with(status.status) {
  val reaction = reactions.firstOrNull { it.accountKey == accountKey }
  data.toUi(
    user = user.toUi(),
    media = media.toUi(),
    isGap = timeline.isGap,
    url = url.toUi(),
    reaction = reaction,
    referenceStatus = status.references.mapNotNull {
      it.status ?: return@mapNotNull null
      it.reference.referenceType to it.status.toUi(
        accountKey = accountKey
      )
    }.toMap()
  )
}

internal fun DbWarpnetStatusExtra.toUi() = WarpnetStatusExtra(
  reply_settings = reply_settings,
  quoteCount = quoteCount
)

internal fun DbMastodonStatusExtra.toUi() = MastodonStatusExtra(
  type = type,
  emoji = emoji.toUi().toPersistentList(),
  visibility = visibility,
  mentions = mentions?.toUi()?.toPersistentList()
)

internal fun List<Mention>.toUi() = map {
  MastodonMention(
    id = it.id,
    username = it.username,
    url = it.url,
    acct = it.acct
  )
}

internal fun DbPreviewCard.toUi() = UiCard(
  link = link,
  displayLink = displayLink,
  title = title,
  description = desc,
  image = image
)

internal fun Poll.toUi() = id?.let {
  UiPoll(
    id = it,
    options = options?.map { option ->
      Option(
        text = option.title ?: "",
        count = option.votesCount ?: 0
      )
    } ?: emptyList(),
    expired = expired ?: false,
    expiresAt = expiresAt?.millis,
    multiple = multiple ?: false,
    voted = voted ?: false,
    votersCount = votersCount,
    ownVotes = ownVotes,
    votesCount = votesCount
  )
}

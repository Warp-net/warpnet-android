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
package com.warpnet.warpnet-android.dataprovider.mapper

import com.warpnet.services.warpnet.model.DirectMessageEvent
import com.warpnet.services.warpnet.model.ReferencedTweetType
import com.warpnet.services.warpnet.model.ReplySettings
import com.warpnet.services.warpnet.model.Status
import com.warpnet.services.warpnet.model.StatusV2
import com.warpnet.services.warpnet.model.Trend
import com.warpnet.services.warpnet.model.WarpnetList
import com.warpnet.services.warpnet.model.User
import com.warpnet.services.warpnet.model.UserV2
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.MediaType
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.model.enums.ReferenceType
import com.warpnet.warpnet-android.model.enums.WarpnetReplySettings
import com.warpnet.warpnet-android.model.paging.PagingTimeLine
import com.warpnet.warpnet-android.model.paging.PagingTimeLineWithStatus
import com.warpnet.warpnet-android.model.ui.ListsMode
import com.warpnet.warpnet-android.model.ui.StatusMetrics
import com.warpnet.warpnet-android.model.ui.UiCard
import com.warpnet.warpnet-android.model.ui.UiDMEvent
import com.warpnet.warpnet-android.model.ui.UiGeo
import com.warpnet.warpnet-android.model.ui.UiList
import com.warpnet.warpnet-android.model.ui.UiMedia
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.model.ui.UiTrend
import com.warpnet.warpnet-android.model.ui.UiUrlEntity
import com.warpnet.warpnet-android.model.ui.UiUser
import com.warpnet.warpnet-android.model.ui.UserMetrics
import com.warpnet.warpnet-android.model.ui.warpnet.WarpnetStatusExtra
import com.warpnet.warpnet-android.model.ui.warpnet.WarpnetUserExtra
import com.warpnet.warpnet-android.navigation.RootDeepLinks
import com.warpnet.warpnettext.Autolink
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap

val autolink by lazy {
  Autolink().apply {
    setUsernameIncludeSymbol(true)
    hashtagUrlBase = "${generateDeepLinkBase(RootDeepLinks.Search.route)}/%23"
    cashtagUrlBase = "${generateDeepLinkBase(RootDeepLinks.Search.route)}/%24"
    usernameUrlBase = "${generateDeepLinkBase(RootDeepLinks.Warpnet.User.route)}/"
  }
}

private fun generateDeepLinkBase(deeplink: String): String {
  return deeplink.substring(
    0,
    deeplink.indexOf("/{")
  )
}

fun StatusV2.toPagingTimeline(
  accountKey: MicroBlogKey,
  pagingKey: String,
): PagingTimeLineWithStatus {
  val status = toUiStatus(accountKey, isGap = false)
  return PagingTimeLineWithStatus(
    timeline = PagingTimeLine(
      accountKey = accountKey,
      timestamp = status.timestamp,
      isGap = false,
      statusKey = status.statusKey,
      pagingKey = pagingKey,
      sortId = status.timestamp
    ),
    status = status,
  )
}

fun Status.toPagingTimeline(
  accountKey: MicroBlogKey,
  pagingKey: String,
): PagingTimeLineWithStatus {
  val status = toUiStatus(accountKey = accountKey)

  return PagingTimeLineWithStatus(
    timeline = PagingTimeLine(
      accountKey = accountKey,
      timestamp = status.timestamp,
      isGap = false,
      statusKey = status.statusKey,
      pagingKey = pagingKey,
      sortId = status.timestamp
    ),
    status = status,
  )
}

internal fun StatusV2.toUiStatus(
  @Suppress("UNUSED_PARAMETER")
  accountKey: MicroBlogKey,
  isGap: Boolean = false,
): UiStatus {
  val retweet = this.referencedTweets
    ?.firstOrNull { it.type == ReferencedTweetType.retweeted }?.status?.toUiStatus(
      accountKey
    )
  val replyTo = this.let {
    it.referencedTweets
      ?.firstOrNull { it.type == ReferencedTweetType.retweeted }?.status ?: it
  }.referencedTweets
    ?.firstOrNull { it.type == ReferencedTweetType.replied_to }?.status?.toUiStatus(
      accountKey
    )
  val quote = this.let {
    it.referencedTweets
      ?.firstOrNull { it.type == ReferencedTweetType.retweeted }?.status ?: it
  }.referencedTweets
    ?.firstOrNull { it.type == ReferencedTweetType.quoted }?.status?.toUiStatus(
      accountKey
    )

  val user = user?.toUiUser() ?: throw IllegalArgumentException("Status.user should not be null")
  val statusKey = MicroBlogKey.warpnet(
    id ?: throw IllegalArgumentException("Status.idStr should not be null")
  )
  return UiStatus(
    statusId = id ?: throw IllegalArgumentException("Status.idStr should not be null"),
    sensitive = possiblySensitive ?: false,
    rawText = text ?: "",
    htmlText = autolink.autoLink(text ?: ""),
    timestamp = createdAt?.millis ?: 0,
    metrics = StatusMetrics(
      retweet = publicMetrics?.retweetCount ?: 0,
      like = publicMetrics?.likeCount ?: 0,
      reply = publicMetrics?.replyCount ?: 0,
    ),
    geo = UiGeo(
      name = place?.fullName ?: ""
    ),
    hasMedia = !attachments?.media.isNullOrEmpty(),
    source = source ?: "",
    user = user,
    statusKey = statusKey,
    platformType = PlatformType.Warpnet,
    extra = WarpnetStatusExtra(
      reply_settings = replySettings.toDbEnums(),
      quoteCount = publicMetrics?.quoteCount
    ),
    card = entities?.urls?.firstOrNull()
      ?.takeUnless { url ->
        referencedTweets?.firstOrNull { it.type == ReferencedTweetType.quoted }
          ?.id?.let { id -> url.expandedURL?.endsWith(id) == true } == true
      }
      ?.takeUnless { url -> url.displayURL?.contains("pic.warpnet.com") == true }
      ?.let {
        it.expandedURL?.let { url ->
          UiCard(
            link = url,
            title = it.title,
            description = it.description,
            image = it.images?.firstOrNull()?.url,
            displayLink = it.displayURL,
          )
        }
      },
    inReplyToStatusId = referencedTweets?.find { it.type == ReferencedTweetType.replied_to }?.id,
    inReplyToUserId = inReplyToUserId,
    retweeted = false,
    liked = false,
    media = (attachments?.media ?: emptyList()).filter {
      it.type == MediaType.photo.name // TODO: video and gif
    }.mapIndexed { index, it ->
      val type = it.type?.let { MediaType.valueOf(it) } ?: MediaType.photo
      UiMedia(
        belongToKey = statusKey,
        previewUrl = getImage(it.url ?: it.previewImageURL, "small"),
        mediaUrl = getImage(it.url ?: it.previewImageURL, "orig"),
        width = it.width ?: 0,
        height = it.height ?: 0,
        pageUrl = null, // TODO: how to play media under warpnet v2 api
        altText = "",
        url = it.url,
        type = type,
        order = index,
      )
    }.toPersistentList(),
    isGap = isGap,
    url = entities?.urls?.map {
      UiUrlEntity(
        url = it.url ?: "",
        expandedUrl = it.expandedURL ?: "",
        displayUrl = it.displayURL ?: "",
        title = it.title,
        description = it.description,
        image = it.images?.maxByOrNull { it.width ?: it.height ?: 0 }?.url
      )
    }?.toPersistentList() ?: persistentListOf(),
    referenceStatus = mutableMapOf<ReferenceType, UiStatus>().apply {
      replyTo?.let { this[ReferenceType.Reply] = it }
      quote?.let { this[ReferenceType.Quote] = it }
      retweet?.let { this[ReferenceType.Retweet] = it }
    }.toPersistentMap(),
    language = lang,
  )
}

internal fun getImage(uri: String?, type: String): String? {
  if (uri == null) {
    return null
  }
  if (uri.contains(".")) {
    val index = uri.lastIndexOf(".")
    val extension = uri.substring(index)
    return "${uri.removeSuffix(extension)}?format=${extension.removePrefix(".")}&name=$type"
  }
  return uri
}

internal fun Status.toUiStatus(
  accountKey: MicroBlogKey,
  isGap: Boolean = false,
): UiStatus {
  val retweet = retweetedStatus?.toUiStatus(accountKey)
  val quote =
    (retweetedStatus?.quotedStatus ?: quotedStatus)?.toUiStatus(accountKey)

  val user = user?.toUiUser() ?: throw IllegalArgumentException("Status.user should not be null")
  val statusKey = MicroBlogKey.warpnet(
    idStr ?: throw IllegalArgumentException("Status.idStr should not be null")
  )
  return UiStatus(
    statusId = idStr ?: throw IllegalArgumentException("Status.idStr should not be null"),
    sensitive = possiblySensitive ?: false,
    rawText = fullText ?: text ?: "",
    htmlText = autolink.autoLink(fullText ?: text ?: ""),
    timestamp = createdAt?.millis ?: 0,
    metrics = StatusMetrics(
      retweet = retweetCount ?: 0,
      like = favoriteCount ?: 0,
      reply = 0,
    ),
    geo = UiGeo(
      name = place?.fullName ?: ""
    ),
    hasMedia = extendedEntities?.media != null || entities?.media != null,
    source = source ?: "",
    user = user,
    statusKey = statusKey,
    platformType = PlatformType.Warpnet,
    extra = WarpnetStatusExtra(
      reply_settings = WarpnetReplySettings.Everyone,
    ),
    card = entities?.urls?.firstOrNull()
      ?.takeUnless { url -> quotedStatus?.idStr?.let { id -> url.expandedURL?.endsWith(id) == true } == true }
      ?.takeUnless { url -> url.expandedURL?.contains("pic.warpnet.com") == true }
      ?.let {
        it.url?.let { url ->
          UiCard(
            link = it.expandedURL ?: url,
            displayLink = it.displayURL,
            image = null,
            title = null,
            description = null,
          )
        }
      },
    inReplyToUserId = inReplyToUserIDStr,
    inReplyToStatusId = inReplyToStatusIDStr,
    media = (
      extendedEntities?.media ?: entities?.media
        ?: emptyList()
      ).mapIndexed { index, it ->
      val type = it.type?.let { MediaType.valueOf(it) } ?: MediaType.photo
      UiMedia(
        belongToKey = statusKey,
        previewUrl = getImage(it.mediaURLHTTPS, "small"),
        mediaUrl = when (type) {
          MediaType.photo -> getImage(it.mediaURLHTTPS, "orig")
          MediaType.animated_gif, MediaType.video -> it.videoInfo?.variants?.maxByOrNull {
            it.bitrate ?: 0L
          }?.url
          MediaType.audio -> it.mediaURLHTTPS
          MediaType.other -> it.mediaURLHTTPS
        },
        width = it.sizes?.large?.w ?: 0,
        height = it.sizes?.large?.h ?: 0,
        pageUrl = it.expandedURL,
        altText = it.displayURL ?: "",
        url = it.url,
        type = type,
        order = index,
      )
    }.toPersistentList(),
    liked = favorited == true,
    retweeted = retweeted == true,
    isGap = isGap,
    url = entities?.urls?.map {
      UiUrlEntity(
        url = it.url ?: "",
        expandedUrl = it.expandedURL ?: "",
        displayUrl = it.displayURL ?: "",
        title = null,
        description = null,
        image = null,
      )
    }?.toPersistentList() ?: persistentListOf(),
    referenceStatus = mutableMapOf<ReferenceType, UiStatus>().apply {
      quote?.let { this[ReferenceType.Quote] = it }
      retweet?.let { this[ReferenceType.Retweet] = it }
    }.toPersistentMap(),
    language = lang,
  )
}

internal fun User.toUiUser(): UiUser {
  return UiUser(
    id = this.idStr ?: throw IllegalArgumentException("user.idStr should not be null"),
    name = this.name ?: "",
    screenName = this.screenName ?: "",
    profileImage = (profileImageURLHTTPS ?: profileImageURL)?.let { updateProfileImagePath(it) }
      ?: "",
    profileBackgroundImage = profileBannerURL,
    metrics = UserMetrics(
      fans = this.followersCount ?: 0,
      follow = this.friendsCount ?: 0,
      listed = this.listedCount ?: 0,
      status = statusesCount ?: 0,
    ),
    rawDesc = this.description ?: "",
    htmlDesc = autolink.autoLink(this.description ?: ""),
    location = this.location,
    website = this.entities?.url?.urls?.firstOrNull { it.url == this.url }?.expandedURL,
    verified = this.verified ?: false,
    protected = this.protected ?: false,
    userKey = MicroBlogKey.warpnet(
      idStr ?: throw IllegalArgumentException("user.idStr should not be null")
    ),
    platformType = PlatformType.Warpnet,
    acct = MicroBlogKey.warpnet(screenName ?: ""),
    extra = WarpnetUserExtra(
      pinned_tweet_id = null,
      url = entities?.description?.urls?.map {
        UiUrlEntity(
          url = it.url ?: "",
          expandedUrl = it.expandedURL ?: "",
          displayUrl = it.displayURL ?: "",
          title = "",
          description = "",
          image = null
        )
      }?.toPersistentList() ?: persistentListOf()
    )
  )
}

internal fun UserV2.toUiUser(): UiUser {
  return UiUser(
    id = id ?: throw IllegalArgumentException("user.idStr should not be null"),
    name = name ?: "",
    screenName = username ?: "",
    profileImage = profileImageURL?.let { updateProfileImagePath(it) } ?: "",
    profileBackgroundImage = profileBanner?.sizes?.let {
      it.getOrElse("mobile_retina", { null }) ?: it.values.firstOrNull()
    }?.url,
    metrics = UserMetrics(
      fans = publicMetrics?.followersCount ?: 0,
      follow = publicMetrics?.followingCount ?: 0,
      listed = publicMetrics?.listedCount ?: 0,
      status = publicMetrics?.tweetCount ?: 0,
    ),
    rawDesc = description ?: "",
    htmlDesc = autolink.autoLink(description ?: ""),
    location = location,
    website = entities?.url?.urls?.firstOrNull { it.url == url }?.expandedURL,
    verified = verified ?: false,
    protected = protected ?: false,
    userKey = MicroBlogKey.warpnet(
      id ?: throw IllegalArgumentException("user.idStr should not be null")
    ),
    acct = MicroBlogKey.warpnet(username ?: ""),
    platformType = PlatformType.Warpnet,
    extra = WarpnetUserExtra(
      pinned_tweet_id = pinnedTweetID,
      url = entities?.description?.urls?.map {
        UiUrlEntity(
          url = it.url ?: "",
          expandedUrl = it.expandedURL ?: "",
          displayUrl = it.displayURL ?: "",
          title = null,
          description = null,
          image = null
        )
      }?.toPersistentList() ?: persistentListOf()
    )
  )
}

internal fun updateProfileImagePath(
  value: String,
  size: ProfileImageSize = ProfileImageSize.reasonably_small
): String {
  val last = value.split("/").lastOrNull()
  var id = last?.split(".")?.firstOrNull()
  ProfileImageSize.values().forEach {
    id = id?.removeSuffix("_${it.name}")
  }
  return if (id != null && last != null) {
    value.replace(last, "${id}_${size.name}.${value.split(".").lastOrNull()}")
  } else {
    value
  }
}

internal enum class ProfileImageSize {
  original,
  reasonably_small,
  bigger,
  normal,
  mini,
}

internal fun WarpnetList.toUiList(accountKey: MicroBlogKey) = UiList(
  ownerId = user?.idStr ?: "",
  id = idStr ?: throw IllegalArgumentException("list.idStr should not be null"),
  title = name ?: "",
  descriptions = description ?: "",
  mode = mode ?: "",
  replyPolicy = "",
  accountKey = accountKey,
  listKey = MicroBlogKey.warpnet(idStr ?: throw IllegalArgumentException("list.idStr should not be null"),),
  isFollowed = following ?: true,
  allowToSubscribe = mode != ListsMode.PRIVATE.value
)

internal fun Trend.toUiTrend(accountKey: MicroBlogKey) = UiTrend(
  accountKey = accountKey,
  trendKey = MicroBlogKey.warpnet("$name:$url"),
  displayName = name ?: "",
  query = name ?: "",
  url = url ?: "",
  volume = tweetVolume ?: 0,
  history = emptyList()
)

fun DirectMessageEvent.generateConversationId(accountKey: MicroBlogKey): String {
  return if (accountKey.id == messageCreate?.senderId) {
    "${messageCreate?.senderId}-${messageCreate?.target?.recipientId}"
  } else {
    "${messageCreate?.target?.recipientId}-${messageCreate?.senderId}"
  }
}

fun DirectMessageEvent.toUiDMEvent(accountKey: MicroBlogKey, sender: UiUser): UiDMEvent {
  val messageKey = MicroBlogKey.warpnet("dm-${id ?: throw IllegalArgumentException("message id should not be null")}")
  return UiDMEvent(
    accountKey = accountKey,
    sortId = createdTimestamp?.toLong() ?: 0L,
    conversationKey = MicroBlogKey.warpnet(generateConversationId(accountKey)),
    messageId = id ?: throw IllegalArgumentException("message id should not be null"),
    messageKey = messageKey,
    htmlText = autolink.autoLink(messageCreate?.messageData?.text ?: ""),
    originText = messageCreate?.messageData?.text ?: "",
    createdTimestamp = createdTimestamp?.toLong() ?: 0L,
    messageType = type ?: throw IllegalArgumentException("message type should not be null"),
    senderAccountKey = MicroBlogKey.warpnet(messageCreate?.senderId ?: throw IllegalArgumentException("message sender id should not be null")),
    recipientAccountKey = MicroBlogKey.warpnet(messageCreate?.target?.recipientId ?: throw IllegalArgumentException("message recipientId id should not be null")),
    sendStatus = UiDMEvent.SendStatus.SUCCESS,
    media = messageCreate?.messageData?.attachment?.media?.let { media ->
      val type = media.type?.let { MediaType.valueOf(it) } ?: MediaType.photo
      listOf(
        UiMedia(
          belongToKey = messageKey,
          url = media.url ?: "",
          previewUrl = media.mediaURLHTTPS,
          type = type,
          mediaUrl = when (type) {
            MediaType.photo -> media.mediaURLHTTPS
            MediaType.animated_gif, MediaType.video -> media.videoInfo?.variants?.maxByOrNull {
              it.bitrate ?: 0L
            }?.url
            MediaType.audio -> media.mediaURLHTTPS
            MediaType.other -> media.mediaURLHTTPS
          },
          width = media.sizes?.large?.w ?: 0,
          height = media.sizes?.large?.h ?: 0,
          pageUrl = media.expandedURL,
          altText = media.displayURL ?: "",
          order = 0,
        )
      )
    } ?: emptyList(),
    urlEntity = messageCreate?.messageData?.entities?.urls?.map {
      UiUrlEntity(
        url = it.url ?: "",
        expandedUrl = it.expanded_url ?: "",
        displayUrl = it.display_url ?: "",
        title = null,
        description = null,
        image = null,
      )
    } ?: emptyList(),
    sender = sender
  )
}

private fun ReplySettings?.toDbEnums() = when (this) {
  ReplySettings.MentionedUsers -> WarpnetReplySettings.MentionedUsers
  ReplySettings.FollowingUsers -> WarpnetReplySettings.FollowingUsers
  ReplySettings.Everyone, null -> WarpnetReplySettings.Everyone
}

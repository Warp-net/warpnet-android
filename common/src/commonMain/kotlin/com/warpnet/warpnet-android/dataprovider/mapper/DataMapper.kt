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

import com.warpnet.services.gif.giphy.GifObject
import com.warpnet.services.gif.model.IGif
import com.warpnet.services.mastodon.model.Emoji
import com.warpnet.services.mastodon.model.MastodonList
import com.warpnet.services.microblog.model.IDirectMessage
import com.warpnet.services.microblog.model.IListModel
import com.warpnet.services.microblog.model.IStatus
import com.warpnet.services.microblog.model.ITrend
import com.warpnet.services.microblog.model.IUser
import com.warpnet.services.warpnet.model.DirectMessageEvent
import com.warpnet.services.warpnet.model.WarpnetList
import com.warpnet.warpnet-android.model.AmUser
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.ui.UiEmoji
import com.warpnet.warpnet-android.model.ui.UiEmojiCategory
import com.warpnet.warpnet-android.model.ui.UiGif
import com.warpnet.warpnet-android.model.ui.UiUser
import kotlinx.collections.immutable.toPersistentList
import java.util.UUID

private typealias WarpnetUser = com.warpnet.services.warpnet.model.User
private typealias WarpnetUserV2 = com.warpnet.services.warpnet.model.UserV2
private typealias WarpnetStatus = com.warpnet.services.warpnet.model.Status
private typealias WarpnetStatusV2 = com.warpnet.services.warpnet.model.StatusV2
private typealias WarpnetTrend = com.warpnet.services.warpnet.model.Trend
private typealias MastodonStatus = com.warpnet.services.mastodon.model.Status
private typealias MastodonNotification = com.warpnet.services.mastodon.model.Notification
private typealias MastodonUser = com.warpnet.services.mastodon.model.Account
private typealias MastodonTrend = com.warpnet.services.mastodon.model.Trend
typealias GiphyGif = GifObject
typealias Strings = com.warpnet.warpnet-android.MR.strings
typealias Files = com.warpnet.warpnet-android.MR.files

fun IUser.toUi(accountKey: MicroBlogKey) = when (this) {
  is WarpnetUser -> this.toUiUser()
  is WarpnetUserV2 -> this.toUiUser()
  is MastodonUser -> this.toUiUser(
    accountKey = accountKey
  )
  else -> throw NotImplementedError()
}

fun IStatus.toUi(accountKey: MicroBlogKey, isGap: Boolean = false) = when (this) {
  is WarpnetStatus -> this.toUiStatus(
    accountKey = accountKey,
    isGap = isGap,
  )
  is WarpnetStatusV2 -> this.toUiStatus(
    accountKey = accountKey,
    isGap = isGap,
  )
  is MastodonStatus -> this.toUiStatus(
    accountKey = accountKey,
    isGap = isGap
  )
  is MastodonNotification -> this.toUiStatus(
    accountKey = accountKey,
    isGap = isGap
  )
  else -> throw NotImplementedError()
}

fun IStatus.toPagingTimeline(accountKey: MicroBlogKey, pagingKey: String) = when (this) {
  is WarpnetStatus -> this.toPagingTimeline(
    accountKey = accountKey,
    pagingKey = pagingKey,
  )
  is WarpnetStatusV2 -> this.toPagingTimeline(
    accountKey = accountKey,
    pagingKey = pagingKey,
  )
  is MastodonStatus -> this.toPagingTimeline(
    accountKey = accountKey,
    pagingKey = pagingKey,
  )
  is MastodonNotification -> this.toPagingTimeline(
    accountKey = accountKey,
    pagingKey = pagingKey,
  )
  else -> throw NotImplementedError()
}

fun IListModel.toUi(accountKey: MicroBlogKey) = when (this) {
  is WarpnetList -> this.toUiList(accountKey)
  is MastodonList -> this.toUiList(accountKey)
  else -> throw NotImplementedError()
}

fun ITrend.toUi(accountKey: MicroBlogKey) = when (this) {
  is WarpnetTrend -> this.toUiTrend(accountKey)
  is MastodonTrend -> this.toUiTrend(accountKey)
  else -> throw NotImplementedError()
}

fun IDirectMessage.toUi(accountKey: MicroBlogKey, sender: UiUser) = when (this) {
  is DirectMessageEvent -> this.toUiDMEvent(accountKey, sender)
  else -> throw NotImplementedError()
}

fun List<Emoji>.toUi(): List<UiEmojiCategory> = groupBy({ it.category }, { it }).map {
  UiEmojiCategory(
    if (it.key.isNullOrEmpty()) null else it.key,
    it.value.map { emoji ->
      UiEmoji(
        shortcode = emoji.shortcode,
        url = emoji.url,
        staticURL = emoji.staticURL,
        visibleInPicker = emoji.visibleInPicker,
        category = emoji.category
      )
    }.toPersistentList()
  )
}

fun UiUser.toAmUser() =
  AmUser(
    userId = id,
    name = name,
    userKey = userKey,
    screenName = screenName,
    profileImage = profileImage.toString(),
    profileBackgroundImage = profileBackgroundImage,
    followersCount = metrics.fans,
    friendsCount = metrics.follow,
    listedCount = metrics.listed,
    desc = rawDesc,
    website = website,
    location = location,
    verified = verified,
    isProtected = protected,
  )

fun IGif.toUi(): UiGif {
  return when (this) {
    is GiphyGif -> UiGif(
      id = this.id ?: UUID.randomUUID().toString(),
      url = this.images?.original?.url ?: "",
      mp4 = this.images?.original?.mp4 ?: "",
      preview = this.images?.previewGif?.url ?: "",
      type = this.type ?: "gif"
    )
    else -> throw NotImplementedError()
  }
}

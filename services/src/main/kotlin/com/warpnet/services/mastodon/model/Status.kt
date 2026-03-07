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
package com.warpnet.services.mastodon.model

import com.warpnet.services.microblog.model.IStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaOriginal(
  val width: Int? = null,
  val height: Int? = null,
)

@Serializable
data class MediaMeta(
  val original: MediaOriginal? = null,
)

@Serializable
data class MediaAttachment(
  val id: String? = null,
  val type: MediaType? = null,
  val url: String? = null,
  @SerialName("preview_url") val previewURL: String? = null,
  @SerialName("remote_url") val remoteURL: String? = null,
  @SerialName("text_url") val textURL: String? = null,
  val meta: MediaMeta? = null,
  val description: String? = null,
)

@Serializable
data class Card(
  val url: String? = null,
  val title: String? = null,
  val description: String? = null,
  val image: String? = null,
)

@Serializable
data class Application(
  val name: String? = null,
  val website: String? = null,
)

@Serializable
data class Status(
  val id: String? = null,
  val uri: String? = null,
  @SerialName("created_at") val createdAt: MastodonDate? = null,
  val account: Account? = null,
  val content: String? = null,
  val visibility: Visibility? = null,
  val sensitive: Boolean? = null,
  @SerialName("spoiler_text") val spoilerText: String? = null,
  @SerialName("media_attachments") val mediaAttachments: List<MediaAttachment>? = null,
  val application: Application? = null,
  val mentions: List<Mention>? = null,
  val emojis: List<Emoji>? = null,
  @SerialName("reblogs_count") val reblogsCount: Int? = null,
  @SerialName("favourites_count") val favouritesCount: Int? = null,
  @SerialName("replies_count") val repliesCount: Int? = null,
  val url: String? = null,
  @SerialName("in_reply_to_id") val inReplyToID: String? = null,
  @SerialName("in_reply_to_account_id") val inReplyToAccountID: String? = null,
  val reblog: Status? = null,
  val poll: Poll? = null,
  val card: Card? = null,
  val language: String? = null,
  val favourited: Boolean? = null,
  val reblogged: Boolean? = null,
  val muted: Boolean? = null,
  val bookmarked: Boolean? = null,
  val pinned: Boolean? = null,
) : IStatus

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
import com.warpnet.services.mastodon.model.Hashtag
import com.warpnet.services.mastodon.model.Status
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MastodonSearchResults(
  val accounts: List<Account>? = null,
  val statuses: List<Status>? = null,
  val hashtags: List<Hashtag>? = null,
)

@Serializable
data class StatusContext(
  val ancestors: List<Status>? = null,
  val descendants: List<Status>? = null,
)

@Serializable
data class MastodonRelationship(
  val id: String? = null,
  val following: Boolean? = null,
  @SerialName("followed_by") val followedBy: Boolean? = null,
  val blocking: Boolean? = null,
  @SerialName("blocked_by") val blockedBy: Boolean? = null,
  val muting: Boolean? = null,
)

@Serializable
data class UploadedMedia(
  val id: String? = null,
  val url: String? = null,
)

@Serializable
data class MastodonConversation(
  val id: String? = null,
  val unread: Boolean? = null,
  val accounts: List<Account>? = null,
  @SerialName("last_status") val lastStatus: Status? = null,
)

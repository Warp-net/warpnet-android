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

import com.warpnet.services.microblog.model.IUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Field(
  val name: String? = null,
  val value: String? = null,
  @SerialName("verified_at") val verifiedAt: String? = null,
)

@Serializable
data class Account(
  val id: String? = null,
  val username: String? = null,
  val acct: String? = null,
  val url: String? = null,
  @SerialName("display_name") val displayName: String? = null,
  val note: String? = null,
  val avatar: String? = null,
  @SerialName("avatar_static") val avatarStatic: String? = null,
  val header: String? = null,
  @SerialName("header_static") val headerStatic: String? = null,
  val locked: Boolean? = null,
  val emojis: List<Emoji>? = null,
  @SerialName("followers_count") val followersCount: Int? = null,
  @SerialName("following_count") val followingCount: Int? = null,
  @SerialName("statuses_count") val statusesCount: Long? = null,
  val fields: List<Field>? = null,
  val bot: Boolean? = null,
) : IUser

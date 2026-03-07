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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PollOption(
  val title: String? = null,
  @SerialName("votes_count") val votesCount: Int? = null,
)

@Serializable
data class Poll(
  val id: String? = null,
  @SerialName("expires_at") val expiresAt: MastodonDate? = null,
  val expired: Boolean? = null,
  val multiple: Boolean? = null,
  @SerialName("votes_count") val votesCount: Int? = null,
  @SerialName("voters_count") val votersCount: Int? = null,
  val voted: Boolean? = null,
  @SerialName("own_votes") val ownVotes: List<Int>? = null,
  val options: List<PollOption>? = null,
)

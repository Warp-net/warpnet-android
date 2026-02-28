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
package com.warpnet.warpnetandroid.model.ui

import com.warpnet.warpnetandroid.extensions.humanizedTimestamp

data class UiPoll(
  val id: String,
  val options: List<Option>,
  val expiresAt: Long?, // some instance of mastodon won't expire
  val expired: Boolean = false,
  val multiple: Boolean = false,
  val voted: Boolean = false,
  val votesCount: Long? = null,
  val votersCount: Long? = null,
  val ownVotes: List<Int>? = null,
) {
  val expiresAtString = expiresAt?.humanizedTimestamp().orEmpty()
  val canVote = !voted &&
    !expired &&
    expiresAt?.let { it > System.currentTimeMillis() } ?: true // some instance allows expires time == null
}

data class Option(
  val text: String,
  val count: Long,
)

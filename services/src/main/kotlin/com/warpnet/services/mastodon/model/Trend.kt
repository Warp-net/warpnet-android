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

import com.warpnet.services.microblog.model.ITrend
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TrendHistory(
  val day: String? = null,
  val uses: String? = null,
  val accounts: String? = null,
)

@Serializable
data class Trend(
  val name: String? = null,
  val url: String? = null,
  val history: List<TrendHistory>? = null,
) : ITrend

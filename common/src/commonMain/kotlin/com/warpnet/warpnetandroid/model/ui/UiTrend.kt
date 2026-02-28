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

import com.warpnet.warpnetandroid.model.MicroBlogKey

data class UiTrend(
  val accountKey: MicroBlogKey,
  val trendKey: MicroBlogKey,
  val displayName: String,
  val url: String,
  val query: String,
  val volume: Long,
  val history: List<UiTrendHistory>,
) {
  val sortedHistory = history.sortedByDescending { it.day }
  val dailyAccounts: Long
    get() = if (sortedHistory.isNotEmpty()) sortedHistory[0].accounts else 0L

  val dailyUses: Long
    get() = if (sortedHistory.isNotEmpty()) sortedHistory[0].uses else 0L
}

data class UiTrendHistory(
  val trendKey: MicroBlogKey,
  val day: Long,
  val uses: Long,
  val accounts: Long
)

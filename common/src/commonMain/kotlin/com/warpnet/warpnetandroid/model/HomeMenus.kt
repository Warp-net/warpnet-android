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
package com.warpnet.warpnetandroid.model

import com.warpnet.warpnetandroid.model.enums.PlatformType

enum class HomeMenus(
  val showDefault: Boolean,
  val supportedPlatformType: List<PlatformType>,
) {
  HomeTimeline(
    showDefault = true,
    supportedPlatformType = PlatformType.values().toList(),
  ),
  MastodonNotification(
    showDefault = true,
    supportedPlatformType = listOf(PlatformType.Mastodon),
  ),
  Mention(
    showDefault = true,
    supportedPlatformType = listOf(PlatformType.Warpnet),
  ),
  Search(
    showDefault = true,
    supportedPlatformType = PlatformType.values().toList(),
  ),
  Me(
    showDefault = true,
    supportedPlatformType = PlatformType.values().toList(),
  ),
  Message(
    showDefault = false,
    supportedPlatformType = listOf(PlatformType.Warpnet),
  ),
  LocalTimeline(
    showDefault = false,
    supportedPlatformType = listOf(PlatformType.Mastodon),
  ),
  FederatedTimeline(
    showDefault = false,
    supportedPlatformType = listOf(PlatformType.Mastodon),
  ),
  Draft(
    showDefault = false,
    supportedPlatformType = PlatformType.values().toList(),
  ),
  Lists(
    showDefault = false,
    supportedPlatformType = PlatformType.values().toList(),
  )
}

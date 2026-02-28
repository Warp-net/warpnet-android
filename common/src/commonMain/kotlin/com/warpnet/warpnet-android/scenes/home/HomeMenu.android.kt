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
package com.warpnet.warpnet-android.scenes.home

import com.warpnet.warpnet-android.model.HomeMenus
import com.warpnet.warpnet-android.model.HomeNavigationItem
import com.warpnet.warpnet-android.scenes.home.mastodon.FederatedTimelineItem
import com.warpnet.warpnet-android.scenes.home.mastodon.LocalTimelineItem
import com.warpnet.warpnet-android.scenes.home.mastodon.MastodonNotificationItem

private val itemMap by lazy {
  mutableMapOf(
    HomeMenus.HomeTimeline to HomeTimelineItem(),
    HomeMenus.MastodonNotification to MastodonNotificationItem(),
    HomeMenus.Mention to MentionItem(),
    HomeMenus.Search to SearchItem(),
    HomeMenus.Me to MeItem(),
    HomeMenus.Message to DMConversationListItem(),
    HomeMenus.LocalTimeline to LocalTimelineItem(),
    HomeMenus.FederatedTimeline to FederatedTimelineItem(),
    HomeMenus.Draft to DraftNavigationItem(),
    HomeMenus.Lists to ListsNavigationItem(),
  )
}

val HomeMenus.item: HomeNavigationItem
  get() = itemMap.getValue(this)

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
package com.warpnet.warpnetandroid.notification

import com.warpnet.warpnetandroid.MR
import com.warpnet.warpnetandroid.model.MicroBlogKey
import dev.icerock.moko.resources.StringResource
import java.net.URLEncoder

enum class NotificationChannelSpec(
  val id: String,
  val showBadge: Boolean = false,
  val grouped: Boolean = false,
  val nameRes: StringResource,
  val descriptionRes: StringResource? = null,
) {
  /**
   * For notifications indicate that some lengthy operations are performing in the background.
   * Such as sending attachment process.
   */
  BackgroundProgresses(
    "background_progresses",
    nameRes = MR.strings.common_notification_channel_background_progresses_name,
  ),

  ContentInteractions(
    "content_interactions",
    showBadge = true,
    grouped = true,
    nameRes = MR.strings.common_notification_channel_content_interactions_name,
    descriptionRes = MR.strings.common_notification_channel_content_interactions_description,
  ),

  ContentMessages(
    "content_messages",
    showBadge = true,
    grouped = true,
    nameRes = MR.strings.common_notification_channel_content_messages_name,
    descriptionRes = MR.strings.common_notification_channel_content_messages_description,
  )
}

fun MicroBlogKey.notificationChannelId(id: String): String {
  return "${id}_${URLEncoder.encode(toString(), "UTF-8")}"
}

fun MicroBlogKey.notificationChannelGroupId(): String {
  return URLEncoder.encode(toString(), "UTF-8")
}

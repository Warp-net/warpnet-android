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
package com.warpnet.warpnetandroid.utils

import com.warpnet.services.http.MicroBlogHttpException
import com.warpnet.services.mastodon.model.exceptions.MastodonException
import com.warpnet.services.warpnet.WarpnetErrorCodes
import com.warpnet.services.warpnet.model.exceptions.WarpnetApiException
import com.warpnet.services.warpnet.model.exceptions.WarpnetApiExceptionV2
import com.warpnet.services.utils.MicroBlogJsonException
import com.warpnet.warpnetandroid.MR
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.notification.NotificationEvent
import com.warpnet.warpnetandroid.notification.StringNotificationEvent
import com.warpnet.warpnetandroid.notification.StringResNotificationEvent
import com.warpnet.warpnetandroid.notification.StringResWithActionNotificationEvent
import java.util.concurrent.CancellationException

internal fun InAppNotification.notifyError(e: Throwable) {
  val event = e.generateNotificationEvent()
  if (event != null) {
    show(event)
  }
}

fun Throwable.generateNotificationEvent(): NotificationEvent? {
  return when (this) {
    is MicroBlogHttpException -> {
      when (this.httpCode) {
        HttpErrorCodes.TooManyRequests -> {
          return StringResNotificationEvent(message = MR.strings.common_alerts_too_many_requests_title)
        }
        else -> null
      }
    }
    is MicroBlogJsonException -> {
      microBlogErrorMessage?.let { StringNotificationEvent(it) }
    }
    is WarpnetApiException -> {
      when (this.errors?.firstOrNull()?.code) {
        WarpnetErrorCodes.TemporarilyLocked -> {
          StringResWithActionNotificationEvent(
            MR.strings.common_alerts_account_temporarily_locked_title,
            MR.strings.common_alerts_account_temporarily_locked_message,
            actionStr = MR.strings.common_controls_actions_ok
          ) {
            remoteNavigator.openDeepLink("https://warpnet.com/login")
          }
        }
        WarpnetErrorCodes.RateLimitExceeded -> null
        else -> microBlogErrorMessage?.let { StringNotificationEvent(it) }
      }
    }
    is WarpnetApiExceptionV2 -> {
      detail?.let { StringNotificationEvent(it) }
    }
    is MastodonException -> {
      microBlogErrorMessage?.let { StringNotificationEvent(it) }
    }
    !is CancellationException -> {
      message?.let { StringNotificationEvent(it) }
    }
    else -> null
  }
}

private object HttpErrorCodes {
  const val TooManyRequests = 429
}

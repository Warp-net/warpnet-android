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
package com.warpnet.warpnet-android.kmp

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import com.warpnet.warpnet-android.extensions.shareMedia
import com.warpnet.warpnet-android.extensions.shareText

actual class RemoteNavigator(private val context: Context) {
  actual fun openDeepLink(deeplink: String, fromBackground: Boolean) {
    context.startActivity(
      Intent(
        Intent.ACTION_VIEW,
        Uri.parse(deeplink).normalizeScheme()
      ).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      }
    )
  }

  actual fun shareMedia(
    filePath: String,
    mimeType: String,
    fromBackground: Boolean,
    extraText: String,
  ) {
    context.shareMedia(
      uri = Uri.parse(filePath),
      mimeType = mimeType,
      extraText = extraText,
    )
  }

  actual fun shareText(content: String, fromBackground: Boolean) {
    context.shareText(
      content = content,
    )
  }

  actual fun launchOAuthUri(uri: String) {
    CustomTabsIntent.Builder()
      .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
      .build().run {
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        launchUrl(context, Uri.parse(uri))
      }
  }
}

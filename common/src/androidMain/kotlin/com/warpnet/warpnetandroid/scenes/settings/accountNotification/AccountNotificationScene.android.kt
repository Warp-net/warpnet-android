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
package com.warpnet.warpnetandroid.scenes.settings.accountNotification

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.warpnet.warpnetandroid.BuildConfig
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.compose.LocalResLoader
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.notification.NotificationChannelSpec
import com.warpnet.warpnetandroid.notification.notificationChannelId

@OptIn(ExperimentalMaterialApi::class)
@Composable
actual fun AccountNotificationChannelDetail(
  enabled: Boolean,
  accountKey: MicroBlogKey,
) {
  val context = LocalContext.current
  val resLoader = LocalResLoader.current
  NotificationChannelSpec.values().filter { it.grouped }
    .sortedBy { resLoader.getString(res = it.nameRes) }
    .forEach {
      ListItem(
        modifier = Modifier.clickable(
          onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
              val intent =
                Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                  .putExtra(
                    Settings.EXTRA_APP_PACKAGE,
                    BuildConfig.APPLICATION_ID
                  )
                  .putExtra(
                    Settings.EXTRA_CHANNEL_ID,
                    accountKey.notificationChannelId(it.id)
                  )
              context.startActivity(intent)
            }
          },
          enabled = enabled
        ),
        text = {
          CompositionLocalProvider(
            *if (!enabled) {
              arrayOf(LocalContentAlpha provides ContentAlpha.disabled)
            } else {
              emptyArray()
            }
          ) {
            Text(text = stringResource(res = it.nameRes))
          }
        },
        secondaryText = it.descriptionRes?.let {
          {
            CompositionLocalProvider(
              *if (!enabled) {
                arrayOf(LocalContentAlpha provides ContentAlpha.disabled)
              } else {
                emptyArray()
              }
            ) {
              Text(text = stringResource(res = it))
            }
          }
        }
      )
    }
}

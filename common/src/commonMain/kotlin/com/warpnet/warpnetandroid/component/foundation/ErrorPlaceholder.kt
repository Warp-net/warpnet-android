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
package com.warpnet.warpnetandroid.component.foundation

import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.notification.NotificationEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ErrorPlaceholder(
  throwable: NotificationEvent?,
  modifier: Modifier = Modifier,
) {
  val message = throwable?.getMessage()
  ListItem(
    modifier = modifier,
    icon = {
      CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.medium
      ) {
        Icon(
          imageVector = Icons.Default.ErrorOutline,
          contentDescription = null,
        )
      }
    },
    text = {
      CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.medium
      ) {
        Text(
          text = message
            ?: stringResource(res = com.warpnet.warpnetandroid.MR.strings.common_alerts_failed_to_load_title),
        )
      }
    }
  )
}

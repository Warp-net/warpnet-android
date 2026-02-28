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
package com.warpnet.warpnet-android.component.placeholder

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.warpnet.warpnet-android.component.status.UserAvatarDefaults
import moe.tlaster.placeholder.Placeholder
import moe.tlaster.placeholder.TextPlaceHolder

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UiUserPlaceholder(
  delayMillis: Long = 0,
) {
  ListItem(
    icon = {
      AvatarPlaceHolder(
        delayMillis = delayMillis,
      )
    },
    text = {
      TextPlaceHolder(
        length = 14,
        delayMillis = delayMillis,
      )
    },
    secondaryText = {
      TextPlaceHolder(
        length = 10,
        delayMillis = delayMillis
      )
    }
  )
}

@Composable
fun AvatarPlaceHolder(
  delayMillis: Long = 0,
) {
  Placeholder(
    modifier = Modifier
      .size(UserAvatarDefaults.AvatarSize)
      .clip(CircleShape),
    delayMillis = delayMillis,
  )
}

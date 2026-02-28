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
package com.warpnet.warpnetandroid.component.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.component.foundation.NetworkImage
import moe.tlaster.placeholder.Placeholder

@Composable
fun RoundAvatar(
  avatar: Any,
  modifier: Modifier = Modifier,
  size: Dp = UserAvatarDefaults.AvatarSize,
  withPlatformIcon: Boolean = false,
  onClick: (() -> Unit)? = null,
) {
  Box(
    modifier = modifier
      .let {
        if (withPlatformIcon) {
          it.padding(bottom = 4.dp, end = 4.dp)
        } else {
          it
        }
      }
      .withAvatarClip()
      .clipToBounds()
  ) {
    NetworkImage(
      data = avatar,
      modifier = Modifier
        .clickable(
          onClick = {
            onClick?.invoke()
          }
        ).size(size),
      placeholder = {
        Placeholder(modifier = Modifier.size(size))
      },
    )
  }
}

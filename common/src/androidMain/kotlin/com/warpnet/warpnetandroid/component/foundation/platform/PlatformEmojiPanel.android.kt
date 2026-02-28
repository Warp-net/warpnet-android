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
package com.warpnet.warpnetandroid.component.foundation.platform

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.component.ImeHeightWithInsets
import com.warpnet.warpnetandroid.component.imeBottomInsets
import com.warpnet.warpnetandroid.model.ui.UiEmoji
import com.warpnet.warpnetandroid.model.ui.UiEmojiCategory
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun PlatformEmojiPanel(
  items: List<UiEmojiCategory>,
  showEmoji: Boolean,
  onEmojiSelected: (UiEmoji) -> Unit,
) {
  var height by remember { mutableStateOf(0) }
  ImeHeightWithInsets(
    filter = {
      it > 0
    },
    collectIme = {
      height = max(height, it)
    }

  )
  val targetHeight = with(LocalDensity.current) {
    height.toDp()
  }
  val bottom = imeBottomInsets()
  var visibility by remember { mutableStateOf(false) }
  LaunchedEffect(showEmoji, bottom) {
    if (bottom == targetHeight || showEmoji) {
      visibility = showEmoji
    }
  }
  Box(
    modifier = Modifier
      .height(
        height = if (visibility) {
          (targetHeight - bottom).coerceAtLeast(0.dp)
        } else {
          0.dp
        }
      )
      .fillMaxWidth(),
    contentAlignment = Alignment.Center,
  ) {
    EmojiList(items = items, onEmojiSelected = onEmojiSelected)
  }
}

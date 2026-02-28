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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.MR
import com.warpnet.warpnetandroid.component.painterResource

@Composable
internal fun GifTag(modifier: Modifier = Modifier, resize: Boolean = false) {
  Image(
    painter = painterResource(res = MR.files.ic_gif_tag),
    contentDescription = "gif tag",
    modifier = if (resize) modifier else modifier.size(width = GifTagDefaults.Width, height = GifTagDefaults.Height)
      .padding(paddingValues = GifTagDefaults.padding)
  )
}

private object GifTagDefaults {
  val Width = 48.dp
  val Height = 36.dp
  val padding = PaddingValues(10.dp)
}

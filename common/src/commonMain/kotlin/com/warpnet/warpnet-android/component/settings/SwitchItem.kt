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
package com.warpnet.warpnet-android.component.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.warpnet.warpnet-android.component.foundation.ColoredSwitch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ColumnScope.switchItem(
  value: Boolean,
  onChanged: (Boolean) -> Unit,
  describe: @Composable (() -> Unit)? = null,
  title: @Composable () -> Unit,
) {
  ListItem(
    modifier = Modifier.clickable(onClick = { onChanged.invoke(!value) }),
    text = {
      title.invoke()
    },
    trailing = {
      ColoredSwitch(
        checked = value,
        onCheckedChange = {
          onChanged.invoke(it)
        },
      )
    },
    secondaryText = describe
  )
}

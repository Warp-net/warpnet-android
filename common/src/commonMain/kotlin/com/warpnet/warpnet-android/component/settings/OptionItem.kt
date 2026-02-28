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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.warpnet.warpnet-android.component.foundation.DropdownMenu
import com.warpnet.warpnet-android.component.foundation.DropdownMenuItem
import com.warpnet.warpnet-android.component.lazy.ItemHeader

@Composable
fun <T : Enum<T>> ColumnScope.OptionItem(
  options: List<T>,
  value: T,
  onChanged: (T) -> Unit,
  title: @Composable () -> Unit,
  resultContent: @Composable (T) -> Unit,
  selectItemContent: @Composable (T) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }
  ItemHeader(
    modifier = Modifier.clickable {
      expanded = !expanded
    },
    secondaryText = {
      resultContent(value)
    }
  ) {
    title.invoke()
  }
  DropdownMenu(
    expanded = expanded,
    focusable = false,
    onDismissRequest = {
      expanded = false
    }
  ) {
    options.forEach {
      DropdownMenuItem(onClick = {
        expanded = false
        onChanged.invoke(it)
      }) {
        selectItemContent.invoke(it)
      }
    }
  }
}

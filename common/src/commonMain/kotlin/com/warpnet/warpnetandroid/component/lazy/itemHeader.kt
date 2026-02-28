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
package com.warpnet.warpnetandroid.component.lazy

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemHeader(
  modifier: Modifier = Modifier,
  icon: @Composable (() -> Unit)? = null,
  secondaryText: @Composable (() -> Unit)? = null,
  singleLineSecondaryText: Boolean = true,
  overlineText: @Composable (() -> Unit)? = null,
  trailing: @Composable (() -> Unit)? = null,
  text: @Composable () -> Unit
) {
  ListItem(
    modifier,
    icon,
    secondaryText,
    singleLineSecondaryText,
    overlineText,
    trailing,
    text = {
      ProvideTextStyle(value = MaterialTheme.typography.button) {
        text.invoke()
      }
    },
  )
}

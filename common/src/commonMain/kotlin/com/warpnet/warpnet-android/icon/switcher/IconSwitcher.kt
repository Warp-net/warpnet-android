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
package com.warpnet.warpnet-android.icon.switcher

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.dataprovider.mapper.Strings
import com.warpnet.warpnet-android.icon.WarpnetIcons
import com.warpnet.warpnet-android.icon.warpneticons.ChooseToUse
import com.warpnet.warpnet-android.kmp.AppIcon
import com.warpnet.warpnet-android.kmp.Platform
import com.warpnet.warpnet-android.kmp.currentPlatform
import com.warpnet.warpnet-android.kmp.systemVersion

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IconSwitcher(
  modifier: Modifier = Modifier,
  appIcon: AppIcon,
  onclick: () -> Unit,
) {
  if (
    currentPlatform != Platform.Android ||
    systemVersion < 25
  ) {
    return
  }
  ListItem(
    modifier = modifier.clickable {
      onclick.invoke()
    },
    icon = {
      Icon(
        imageVector = WarpnetIcons.ChooseToUse,
        contentDescription = stringResource(Strings.scene_settings_appearance_app_icon),
        modifier = Modifier.size(24.dp),
      )
    },
    trailing = {
      Image(
        painter = rememberVectorPainter(appIcon.toImageVector()),
        contentDescription = stringResource(Strings.scene_settings_appearance_app_icon),
        modifier = Modifier.size(32.dp).clip(MaterialTheme.shapes.small).clipToBounds(),
      )
    }
  ) {
    Text(stringResource(Strings.scene_settings_appearance_app_icon))
  }
}

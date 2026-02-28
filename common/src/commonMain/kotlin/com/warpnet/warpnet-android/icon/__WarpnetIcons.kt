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
package com.warpnet.warpnet-android.icon

import androidx.compose.ui.graphics.vector.ImageVector
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon1
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon10
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon11
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon12
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon13
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon14
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon2
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon3
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon4
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon5
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon6
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon7
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon8
import com.warpnet.warpnet-android.icon.warpneticons.AppIcon9
import com.warpnet.warpnet-android.icon.warpneticons.ChooseToUse
import kotlin.collections.List as ____KtList

public object WarpnetIcons

private var __AllIcons: ____KtList<ImageVector>? = null

public val WarpnetIcons.AllIcons: ____KtList<ImageVector>
  get() {
    if (__AllIcons != null) {
      return __AllIcons!!
    }
    __AllIcons = listOf(
      AppIcon7, AppIcon6, AppIcon4, AppIcon14, AppIcon5, AppIcon1, AppIcon10,
      AppIcon11, AppIcon2, AppIcon13, AppIcon12, AppIcon3, ChooseToUse, AppIcon8, AppIcon9
    )
    return __AllIcons!!
  }

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
package com.warpnet.warpnetandroid.kmp

import androidx.compose.ui.graphics.vector.ImageVector
import com.warpnet.warpnetandroid.icon.WarpnetIcons
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon1
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon10
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon11
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon12
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon13
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon14
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon2
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon3
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon4
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon5
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon6
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon7
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon8
import com.warpnet.warpnetandroid.icon.warpneticons.AppIcon9
import kotlinx.serialization.Serializable

const val QUALIFIER = "com.warpnet.warpnetandroid"

expect class IconModifier {
  fun changeIcon(
    newIcon: AppIcon,
  )
}

val launchIcons = listOf(
  WarpnetIcons.AppIcon1,
  WarpnetIcons.AppIcon2,
  WarpnetIcons.AppIcon3,
  WarpnetIcons.AppIcon4,
  WarpnetIcons.AppIcon5,
  WarpnetIcons.AppIcon6,
  WarpnetIcons.AppIcon7,
  WarpnetIcons.AppIcon8,
  WarpnetIcons.AppIcon9,
  WarpnetIcons.AppIcon10,
  WarpnetIcons.AppIcon11,
  WarpnetIcons.AppIcon12,
  WarpnetIcons.AppIcon13,
  WarpnetIcons.AppIcon14,
)

@Serializable
enum class AppIcon(
  val componentName: String, // Must correspond to the <activity-alias> `android:name`s in AndroidManifest
) {
  DEFAULT(
    componentName = "$QUALIFIER.Launcher",
  ),
  Launcher1(
    componentName = "$QUALIFIER.Launcher1",
  ),
  Launcher2(
    componentName = "$QUALIFIER.Launcher2",
  ),
  Launcher3(
    componentName = "$QUALIFIER.Launcher3",
  ),
  Launcher4(
    componentName = "$QUALIFIER.Launcher4",
  ),
  Launcher5(
    componentName = "$QUALIFIER.Launcher5",
  ),
  Launcher6(
    componentName = "$QUALIFIER.Launcher6",
  ),
  Launcher7(
    componentName = "$QUALIFIER.Launcher7",
  ),
  Launcher8(
    componentName = "$QUALIFIER.Launcher8",
  ),
  Launcher9(
    componentName = "$QUALIFIER.Launcher9",
  ),
  Launcher10(
    componentName = "$QUALIFIER.Launcher10",
  ),
  Launcher11(
    componentName = "$QUALIFIER.Launcher11",
  ),
  Launcher12(
    componentName = "$QUALIFIER.Launcher12",
  ),
  Launcher13(
    componentName = "$QUALIFIER.Launcher13",
  ),
  ;

  fun toImageVector(): ImageVector {
    return launchIcons[values().indexOf(this)]
  }

  companion object {
    fun fromValue(componentName: String): AppIcon {
      return values().first { it.componentName == componentName }
    }
    fun fromIndex(index: Int): AppIcon {
      return values().getOrElse(index, defaultValue = { DEFAULT })
    }
  }
}

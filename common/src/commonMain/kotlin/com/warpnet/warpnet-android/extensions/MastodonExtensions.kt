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
package com.warpnet.warpnet-android.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.model.enums.MastodonVisibility

@Composable
fun MastodonVisibility.icon(): Painter {
  return when (this) {
    MastodonVisibility.Public -> painterResource(res = com.warpnet.warpnet-android.MR.files.ic_globe)
    MastodonVisibility.Unlisted -> painterResource(res = com.warpnet.warpnet-android.MR.files.ic_lock_open)
    MastodonVisibility.Private -> painterResource(res = com.warpnet.warpnet-android.MR.files.ic_lock)
    MastodonVisibility.Direct -> painterResource(res = com.warpnet.warpnet-android.MR.files.ic_mail)
  }
}

@Composable
fun MastodonVisibility.stringName(): String {
  return when (this) {
    MastodonVisibility.Public -> stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_compose_visibility_public)
    MastodonVisibility.Unlisted -> stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_compose_visibility_unlisted)
    MastodonVisibility.Private -> stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_compose_visibility_private)
    MastodonVisibility.Direct -> stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_compose_visibility_direct)
  }
}

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
package com.warpnet.warpnet-android.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.warpnet.warpnet-android.compose.LocalResLoader
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

@Composable
fun stringResource(res: StringResource, vararg formatArgs: Any): String {
  return LocalResLoader.current.getString(res, *formatArgs)
}

@Composable
fun stringResource(res: StringResource): String {
  return LocalResLoader.current.getString(res)
}

/**
 * res: FileResource:svg, ImageResource
 */
@Composable
fun painterResource(res: Any): Painter {
  return when (res) {
    is FileResource -> LocalResLoader.current.getSvg(res)
    is ImageResource -> LocalResLoader.current.getImage(res)
    else -> throw NotImplementedError()
  }
}

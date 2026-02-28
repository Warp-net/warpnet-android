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
package com.warpnet.warpnetandroid.scenes

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.scenes.warpnet.WarpnetWebSignInScene
import moe.tlaster.precompose.navigation.Navigator
import java.net.URLDecoder

@Composable
actual fun PlatformStatusMediaScene(
  statusKey: String,
  selectedIndex: Int?,
  navigator: Navigator,
) {
  StatusMediaScene(
    statusKey = statusKey,
    selectedIndex = selectedIndex ?: 0,
    navigator = navigator,
  )
}

@Composable
actual fun PlatformRawMediaScene(
  url: String,
  type: String,
  navigator: Navigator,
) {
  RawMediaScene(url = url, type = type, navigator = navigator)
}

@Composable
actual fun PlatformPureMediaScene(
  belongToKey: String,
  selectedIndex: Int?,
  navigator: Navigator
) {
  PureMediaScene(
    belongToKey = belongToKey,
    selectedIndex = selectedIndex ?: 0,
    navigator = navigator,
  )
}

@Composable
actual fun PlatformScene(target: String, navigator: Navigator) {
  WarpnetWebSignInScene(
    target = URLDecoder.decode(target, "UTF-8"),
    navigator = navigator
  )
}

@Composable
actual fun StatusMediaSceneLayout(
  backgroundColor: Color,
  contentColor: Color,
  closeButton: @Composable () -> Unit,
  bottomView: @Composable () -> Unit,
  mediaView: @Composable () -> Unit,
  backgroundView: @Composable () -> Unit,
) {
  InAppNotificationScaffold(
    backgroundColor = backgroundColor,
    contentColor = contentColor,
    bottomBar = {
      bottomView.invoke()
    }
  ) {
    Box {
      backgroundView.invoke()
      mediaView.invoke()
      closeButton.invoke()
    }
  }
}

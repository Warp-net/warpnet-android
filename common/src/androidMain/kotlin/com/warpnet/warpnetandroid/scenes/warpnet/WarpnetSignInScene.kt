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
package com.warpnet.warpnetandroid.scenes.warpnet

import androidx.compose.runtime.Composable
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.foundation.WebComponent
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.utils.WarpnetWebJavascriptInterface
import moe.tlaster.precompose.navigation.Navigator

const val INJECT_CONTENT =
  "javascript:window.injector.tryPinCode(document.querySelector('#oauth_pin code').textContent);"

@Composable
fun WarpnetWebSignInScene(
  target: String,
  navigator: Navigator,
) {
  WarpnetScene {
    InAppNotificationScaffold {
      WebComponent(
        url = target,
        onPageFinished = { view, _ ->
          view.loadUrl(INJECT_CONTENT)
        },
        javascriptInterface = mapOf(
          "injector" to WarpnetWebJavascriptInterface(
            navigator
          )
        ),
      )
    }
  }
}

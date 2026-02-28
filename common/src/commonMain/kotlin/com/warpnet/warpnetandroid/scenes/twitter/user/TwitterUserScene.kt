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
package com.warpnet.warpnetandroid.scenes.warpnet.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.navigation.user
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.warpnet.user.WarpnetUserViewModel
import moe.tlaster.precompose.navigation.Navigator
import org.koin.core.parameter.parametersOf

@Composable
fun WarpnetUserScene(
  screenName: String,
  navigator: Navigator,
) {
  val viewModel: WarpnetUserViewModel = getViewModel {
    parametersOf(screenName)
  }
  val user by viewModel.user.observeAsState(initial = null)
  val error by viewModel.error.observeAsState(initial = null)
  LaunchedEffect(user) {
    user?.let {
      navigator.user(it)
    }
  }

  WarpnetScene {
    InAppNotificationScaffold {
      Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
      ) {
        error?.let {
          Icon(
            Icons.Default.Error,
            modifier = Modifier.size(40.dp),
            contentDescription = null,
          )
        } ?: run {
          CircularProgressIndicator()
        }
      }
    }
  }
}

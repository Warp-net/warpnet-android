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
package com.warpnet.warpnetandroid.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.ui.LocalBackDispatcherOwner

@Composable
fun RequireAuthorization(
  content: @Composable () -> Unit,
  navigator: Navigator,
) {
  val account = LocalActiveAccount.current
  if (account == null) {
    val backDispatcher = LocalBackDispatcherOwner.current?.backDispatcher
    LaunchedEffect(Unit) {
      val result = navigator.navigateForResult(Root.SignIn.General)
      if (result == null) {
        backDispatcher?.onBackPress()
      }
    }
  } else {
    content.invoke()
  }
}

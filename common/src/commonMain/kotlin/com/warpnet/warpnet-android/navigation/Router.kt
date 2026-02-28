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
package com.warpnet.warpnet-android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.repository.AccountRepository
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator

@Composable
fun Router(
  navController: Navigator = rememberNavigator(),
  isDebug: Boolean = false,
) {
  val accountRepository: AccountRepository = get()
  val hasAccount = remember { mutableStateOf<Boolean?>(null) }
  LaunchedEffect(Unit) {
    hasAccount.value = accountRepository.hasAccount()
  }
  hasAccount.value?.let {
    NavHost(
      navigator = navController,
      initialRoute = if (it) {
        Root.Home
      } else {
        Root.SignIn.General
      }
    ) {
      warpnetRoute(navigator = navController)
      complexRoute(navigator = navController)
    }
    if (isDebug) {
      ComposeDebugTool(navController)
    }
  }
}

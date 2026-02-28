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
package com.warpnet.warpnetandroid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import com.warpnet.warpnetandroid.action.LocalStatusActions
import com.warpnet.warpnetandroid.action.StatusActions
import com.warpnet.warpnetandroid.component.foundation.LocalInAppNotification
import com.warpnet.warpnetandroid.compose.LocalResLoader
import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.kmp.LocalRemoteNavigator
import com.warpnet.warpnetandroid.navigation.Router
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.ui.LocalActiveAccountViewModel
import com.warpnet.warpnetandroid.utils.LocalPlatformResolver
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModelScope

@Composable
fun App(navController: Navigator = Navigator()) {
  val accountViewModel =
    com.warpnet.warpnetandroid.di.ext.getViewModel<com.warpnet.warpnetandroid.viewmodel.ActiveAccountViewModel>()
  val account by accountViewModel.account.observeAsState(null, accountViewModel.viewModelScope.coroutineContext)
  CompositionLocalProvider(
    LocalInAppNotification provides get(),
    LocalResLoader provides get(),
    LocalRemoteNavigator provides get(),
    LocalActiveAccount provides account,
    LocalActiveAccountViewModel provides accountViewModel,
    LocalStatusActions provides get<StatusActions>(),
    LocalPlatformResolver provides get(),
  ) {
    Router(
      navController = navController,
    )
  }
}

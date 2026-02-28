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
package com.warpnet.warpnet-android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import com.warpnet.warpnet-android.action.LocalStatusActions
import com.warpnet.warpnet-android.action.StatusActions
import com.warpnet.warpnet-android.component.foundation.LocalInAppNotification
import com.warpnet.warpnet-android.compose.LocalResLoader
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.extensions.observeAsState
import com.warpnet.warpnet-android.kmp.LocalRemoteNavigator
import com.warpnet.warpnet-android.navigation.Router
import com.warpnet.warpnet-android.ui.LocalActiveAccount
import com.warpnet.warpnet-android.ui.LocalActiveAccountViewModel
import com.warpnet.warpnet-android.utils.LocalPlatformResolver
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModelScope

@Composable
fun App(navController: Navigator = Navigator()) {
  val accountViewModel =
    com.warpnet.warpnet-android.di.ext.getViewModel<com.warpnet.warpnet-android.viewmodel.ActiveAccountViewModel>()
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

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

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.warpnet.warpnetandroid.component.foundation.SignInScaffold
import com.warpnet.warpnetandroid.component.navigation.warpnetSignInWeb
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.navigation.RootDeepLinks
import com.warpnet.warpnetandroid.viewmodel.warpnet.PinCodeProvider
import com.warpnet.warpnetandroid.viewmodel.warpnet.WarpnetSignInViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import io.github.seiko.precompose.annotation.Path
import moe.tlaster.precompose.navigation.Navigator
import org.koin.core.parameter.parametersOf

@NavGraphDestination(
  route = Root.SignIn.Warpnet.route,
  deepLink = [
    RootDeepLinks.SignIn,
    RootDeepLinks.Callback.SignIn.Warpnet,
    RootDeepLinks.Callback.SignIn.Mastodon,
  ]
)
@Composable
fun WarpnetSignInScene(
  @Path("consumerKey") consumerKey: String,
  @Path("consumerSecret") consumerSecret: String,
  navigator: Navigator,
) {
  val pinCodeProvider: PinCodeProvider = { target ->
    navigator.warpnetSignInWeb(target)
  }
  val viewModel: WarpnetSignInViewModel = getViewModel {
    parametersOf(
      consumerKey,
      consumerSecret,
      pinCodeProvider,
      { success: Boolean ->
        navigator.goBackWith(success)
      }
    )
  }
  val loading by viewModel.loading.observeAsState(initial = false)

  SignInScaffold(popBackStack = {
    navigator.popBackStack()
  }) {
    if (loading) {
      CircularProgressIndicator()
    }
  }
}

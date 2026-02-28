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
package com.warpnet.warpnetandroid.navigation

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.ui.LocalActiveAccountViewModel
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.utils.LocalPlatformResolver

@Composable
fun ProvidePlatformType(
  key: MicroBlogKey,
  provider: suspend () -> PlatformType?,
  content: @Composable (platformType: PlatformType) -> Unit,
) {
  var platformType by rememberSaveable {
    mutableStateOf<PlatformType?>(null)
  }
  val account = LocalActiveAccount.current
  LaunchedEffect(key) {
    platformType = provider.invoke() ?: account?.type
  }
  platformType?.let {
    content.invoke(it)
  } ?: run {
    WarpnetScene {
      Scaffold {
      }
    }
  }
}

@Composable
fun ProvideStatusPlatform(
  statusKey: MicroBlogKey,
  content: @Composable (platformType: PlatformType) -> Unit,
) {
  val platformResolver = LocalPlatformResolver.current
  val account = LocalActiveAccount.current ?: return
  ProvidePlatformType(
    key = statusKey,
    provider = {
      platformResolver.resolveStatus(statusKey = statusKey, account.accountKey)
    },
    content = content
  )
}

@Composable
fun ProvideUserPlatform(
  userKey: MicroBlogKey,
  content: @Composable (platformType: PlatformType) -> Unit,
) {
  val platformResolver = LocalPlatformResolver.current
  ProvidePlatformType(
    key = userKey,
    provider = {
      platformResolver.resolveUser(userKey = userKey)
    },
    content = content
  )
}

@Composable
fun RequirePlatformAccount(
  platformType: PlatformType,
  fallback: () -> Unit = {},
  content: @Composable () -> Unit,
) {
  var account = LocalActiveAccount.current ?: run {
    fallback.invoke()
    return
  }
  if (account.type != platformType) {
    account = LocalActiveAccountViewModel.current.getTargetPlatformDefault(platformType)
      ?: run {
        fallback.invoke()
        return
      }
  }
  CompositionLocalProvider(
    LocalActiveAccount provides account
  ) {
    content.invoke()
  }
}

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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.model.AccountDetails
import com.warpnet.warpnetandroid.repository.AccountRepository
import kotlinx.coroutines.flow.map

@Composable
fun CurrentAccountPresenter(
  accountRepository: AccountRepository = get(),
): CurrentAccountState {
  val state by accountRepository.activeAccount.map {
    if (it == null) {
      CurrentAccountState.Empty
    } else {
      CurrentAccountState.Account(it)
    }
  }.collectAsState(CurrentAccountState.Empty)
  return state
}

interface CurrentAccountState {
  data class Account(val account: AccountDetails) : CurrentAccountState
  object Empty : CurrentAccountState
}

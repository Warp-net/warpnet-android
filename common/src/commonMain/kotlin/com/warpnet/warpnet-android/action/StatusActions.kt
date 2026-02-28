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
package com.warpnet.warpnet-android.action

import androidx.compose.runtime.compositionLocalOf
import com.warpnet.warpnet-android.model.AccountDetails
import com.warpnet.warpnet-android.model.ui.UiStatus

val LocalStatusActions = compositionLocalOf<IStatusActions> { error("No LocalStatusActions") }

interface IStatusActions {
  fun like(status: UiStatus, account: AccountDetails) {}
  fun retweet(status: UiStatus, account: AccountDetails) {}
  fun delete(status: UiStatus, account: AccountDetails) {}
  fun vote(status: UiStatus, account: AccountDetails, votes: List<Int>) {}
}

expect class StatusActions : IStatusActions {
  override fun delete(status: UiStatus, account: AccountDetails)
  override fun like(status: UiStatus, account: AccountDetails)
  override fun retweet(status: UiStatus, account: AccountDetails)
  override fun vote(status: UiStatus, account: AccountDetails, votes: List<Int>)
}

object FakeStatusActions : IStatusActions

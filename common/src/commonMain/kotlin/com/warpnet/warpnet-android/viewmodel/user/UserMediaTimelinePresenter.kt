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
package com.warpnet.warpnet-android.viewmodel.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.services.microblog.TimelineService
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.ui.UiMedia
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.repository.TimelineRepository
import com.warpnet.warpnet-android.scenes.CurrentAccountPresenter
import com.warpnet.warpnet-android.scenes.CurrentAccountState

@Composable
fun UserMediaTimelinePresenter(
  userKey: MicroBlogKey,
  repository: TimelineRepository = get(),
): UserMediaTimelineState {
  val currentAccount = CurrentAccountPresenter()

  if (currentAccount !is CurrentAccountState.Account) {
    return UserMediaTimelineState.NoAccount
  }

  val source = remember(currentAccount) {
    repository.mediaTimeline(
      userKey,
      currentAccount.account.accountKey,
      currentAccount.account.service as TimelineService
    )
  }.collectAsLazyPagingItems()

  return UserMediaTimelineState.Data(source = source)
}

interface UserMediaTimelineState {
  data class Data(
    val source: LazyPagingItems<Pair<UiMedia, UiStatus>>
  ) : UserMediaTimelineState

  object NoAccount : UserMediaTimelineState
}

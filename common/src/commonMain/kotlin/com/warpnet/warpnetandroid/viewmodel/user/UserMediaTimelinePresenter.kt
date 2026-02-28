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
package com.warpnet.warpnetandroid.viewmodel.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.services.microblog.TimelineService
import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiMedia
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.repository.TimelineRepository
import com.warpnet.warpnetandroid.scenes.CurrentAccountPresenter
import com.warpnet.warpnetandroid.scenes.CurrentAccountState

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

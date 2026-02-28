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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.services.microblog.TimelineService
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.repository.TimelineRepository
import com.warpnet.warpnet-android.scenes.CurrentAccountPresenter
import com.warpnet.warpnet-android.scenes.CurrentAccountState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserTimelinePresenter(
  event: Flow<UserTimelineEvent>,
  userKey: MicroBlogKey,
  repository: TimelineRepository = get(),
): UserTimelineState {
  val currentAccount = CurrentAccountPresenter()
  if (currentAccount !is CurrentAccountState.Account) {
    return UserTimelineState.NoAccount
  }

  var excludeReplies by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(Unit) {
    event.collectLatest {
      when (it) {
        is UserTimelineEvent.ExcludeReplies -> {
          excludeReplies = it.exclude
        }
      }
    }
  }

  val source = remember(currentAccount, excludeReplies) {
    repository.userTimeline(
      userKey = userKey,
      accountKey = currentAccount.account.accountKey,
      service = currentAccount.account.service as TimelineService,
      exclude_replies = excludeReplies,
    )
  }.collectAsLazyPagingItems()

  return UserTimelineState.Data(
    source = source,
    excludeReplies = excludeReplies,
  )
}

interface UserTimelineEvent {
  data class ExcludeReplies(
    val exclude: Boolean
  ) : UserTimelineEvent
}

interface UserTimelineState {
  data class Data(
    val source: LazyPagingItems<UiStatus>,
    val excludeReplies: Boolean,
  ) : UserTimelineState
  object NoAccount : UserTimelineState
}

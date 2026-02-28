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
package com.warpnet.warpnet-android.component.lazy.ui

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.warpnet.warpnet-android.component.foundation.ErrorPlaceholder
import com.warpnet.warpnet-android.component.foundation.LocalInAppNotification
import com.warpnet.warpnet-android.utils.generateNotificationEvent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull

@Composable
fun <T : Any> LazyUiList(
  items: LazyPagingItems<T>,
  empty: @Composable () -> Unit = {},
  loading: @Composable () -> Unit = {},
  content: @Composable () -> Unit,
) {
  val refresh = items.loadState.refresh
  val event = remember(refresh) {
    if (refresh is LoadState.Error) {
      refresh.error.generateNotificationEvent()
    } else {
      null
    }
  }
  Crossfade(targetState = items.itemCount == 0) { isEmpty ->
    if (isEmpty) {
      Crossfade(targetState = refresh) { refresh ->
        when (refresh) {
          is LoadState.NotLoading -> empty()
          LoadState.Loading -> loading()
          is LoadState.Error -> ErrorPlaceholder(event)
        }
      }
    } else {
      val inAppNotification = LocalInAppNotification.current
      LaunchedEffect(event) {
        snapshotFlow { event }
          .distinctUntilChanged()
          .filterNotNull()
          .collect {
            inAppNotification.show(it)
          }
      }
      content.invoke()
    }
  }
}

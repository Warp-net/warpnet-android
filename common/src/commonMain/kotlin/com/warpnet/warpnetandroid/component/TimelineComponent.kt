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
package com.warpnet.warpnetandroid.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import com.warpnet.warpnetandroid.action.LocalStatusActions
import com.warpnet.warpnetandroid.action.triggerSwipe
import com.warpnet.warpnetandroid.component.foundation.SwipeToRefreshLayout
import com.warpnet.warpnetandroid.component.lazy.LazyListController
import com.warpnet.warpnetandroid.component.lazy.ui.LazyUiStatusList
import com.warpnet.warpnetandroid.extensions.refreshOrRetry
import com.warpnet.warpnetandroid.extensions.rememberPresenterState
import com.warpnet.warpnetandroid.kmp.LocalRemoteNavigator
import com.warpnet.warpnetandroid.navigation.StatusNavigationData
import com.warpnet.warpnetandroid.preferences.LocalAppearancePreferences
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.viewmodel.timeline.SavedStateKeyType
import com.warpnet.warpnetandroid.viewmodel.timeline.TimeLineEvent
import com.warpnet.warpnetandroid.viewmodel.timeline.TimelinePresenter
import com.warpnet.warpnetandroid.viewmodel.timeline.TimelineState
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun TimelineComponent(
  savedStateKeyType: SavedStateKeyType,
  statusNavigation: StatusNavigationData,
  contentPadding: PaddingValues = PaddingValues(0.dp),
  lazyListController: LazyListController? = null,
) {
  val (state, channel) = rememberPresenterState<TimelineState, TimeLineEvent> {
    TimelinePresenter(it, savedStateKeyType = savedStateKeyType)
  }

  if (state !is TimelineState.Data) {
    return
  }

  val autoRefresh = LocalAppearancePreferences.current.autoRefresh
  val autoRefreshInterval = LocalAppearancePreferences.current.autoRefreshInterval
  val resetToTop = LocalAppearancePreferences.current.resetToTop
  val localStatus = LocalStatusActions.current
  val account = LocalActiveAccount.current
  val remoteNavigator = LocalRemoteNavigator.current

  LaunchedEffect(
    autoRefresh,
    autoRefreshInterval,
  ) {
    if (!autoRefresh) {
      return@LaunchedEffect
    }
    while (isActive) {
      delay(autoRefreshInterval.duration)
      state.source.refreshOrRetry()
    }
  }

  val scope = rememberCoroutineScope()

  val refreshingState = remember(state.source.loadState.refresh) {
    (state.source.loadState.refresh is LoadState.Loading).apply {
      if (!this && resetToTop) {
        scope.launch {
          lazyListController?.listState?.scrollToItem(0)
        }
      }
    }
  }

  SwipeToRefreshLayout(
    refreshingState = refreshingState,
    onRefresh = {
      state.source.refreshOrRetry()
    },
    refreshIndicatorPadding = contentPadding
  ) {
    if (state.source.itemCount > 0) {
      LaunchedEffect(lazyListController) {
        lazyListController?.listState = state.listState
      }
    }

    LazyUiStatusList(
      items = state.source,
      state = state.listState,
      contentPadding = contentPadding,
      loadingBetween = state.loadingBetween,
      onLoadBetweenClicked = { current, next ->
        channel.trySend(TimeLineEvent.LoadBetween(current, next))
      },
      statusNavigation = statusNavigation,
      onSwipe = { type, status ->
        triggerSwipe(
          statusNavigation = statusNavigation,
          actionsViewModel = localStatus,
          account = account,
          remoteNavigator = remoteNavigator,
          status = status,
          type = type,
        )
      }
    )
  }
}

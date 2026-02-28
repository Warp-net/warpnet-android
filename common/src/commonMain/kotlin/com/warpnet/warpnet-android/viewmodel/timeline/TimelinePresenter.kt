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
package com.warpnet.warpnet-android.viewmodel.timeline

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.services.mastodon.MastodonService
import com.warpnet.services.microblog.NotificationService
import com.warpnet.services.microblog.TimelineService
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.NotificationCursorType
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.paging.mediator.paging.pager
import com.warpnet.warpnet-android.paging.mediator.paging.toUi
import com.warpnet.warpnet-android.paging.mediator.timeline.HomeTimelineMediator
import com.warpnet.warpnet-android.paging.mediator.timeline.MentionTimelineMediator
import com.warpnet.warpnet-android.paging.mediator.timeline.NotificationTimelineMediator
import com.warpnet.warpnet-android.paging.mediator.timeline.mastodon.FederatedTimelineMediator
import com.warpnet.warpnet-android.paging.mediator.timeline.mastodon.LocalTimelineMediator
import com.warpnet.warpnet-android.repository.NotificationRepository
import com.warpnet.warpnet-android.scenes.CurrentAccountPresenter
import com.warpnet.warpnet-android.scenes.CurrentAccountState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull

private const val timelinePageSize = 20
private const val timelineInitialLoadSize = 40
private const val FIRST_VISIBLE_KEY_SUFFIX = "_firstVisibleItemIndex"
private const val FIRST_OFFSET_KEY_SUFFIX = "_firstVisibleItemScrollOffset"

data class TimelineScrollState(
  val firstVisibleItemIndex: Int = 0,
  val firstVisibleItemScrollOffset: Int = 0,
)

enum class SavedStateKeyType(val key: String) {
  MENTIONS("_mentions"),
  HOME("_home"),
  NOTIFICATION("_notification"),
  FEDERATED("_federated"),
  LOCAL("_local")
}

@OptIn(ExperimentalPagingApi::class)
@Composable
fun TimelinePresenter(
  event: Flow<TimeLineEvent>,
  savedStateKeyType: SavedStateKeyType,
  dataStore: DataStore<Preferences> = get(),
  database: CacheDatabase = get(),
  notificationRepository: NotificationRepository = get(),
): TimelineState {
  val accountState = CurrentAccountPresenter()

  if (accountState !is CurrentAccountState.Account) {
    return TimelineState.NoAccount
  }

  if (
    accountState.account.type == PlatformType.Warpnet &&
    (
      savedStateKeyType == SavedStateKeyType.FEDERATED ||
        savedStateKeyType == SavedStateKeyType.LOCAL ||
        savedStateKeyType == SavedStateKeyType.NOTIFICATION
      )
  ) {
    return TimelineState.NoAccount
  }

  val pagingMediator = remember(accountState) {
    when (savedStateKeyType) {
      SavedStateKeyType.MENTIONS -> {
        MentionTimelineMediator(
          service = accountState.account.service as TimelineService,
          accountKey = accountState.account.accountKey,
          database = database,
          addCursorIfNeed = { data, accountKey ->
            notificationRepository.addCursorIfNeeded(
              accountKey,
              NotificationCursorType.Mentions,
              data.status.statusId,
              data.status.timestamp,
            )
          }
        )
      }
      SavedStateKeyType.HOME -> {
        HomeTimelineMediator(
          accountState.account.service as TimelineService,
          accountState.account.accountKey,
          database,
        )
      }
      SavedStateKeyType.NOTIFICATION -> {
        NotificationTimelineMediator(
          service = (accountState.account.service as NotificationService),
          accountKey = accountState.account.accountKey,
          database = database,
          addCursorIfNeed = { data, accountKey ->
            notificationRepository.addCursorIfNeeded(
              accountKey,
              NotificationCursorType.General,
              data.status.statusId,
              data.status.timestamp
            )
          }
        )
      }
      SavedStateKeyType.FEDERATED -> {
        FederatedTimelineMediator(
          (accountState.account.service as MastodonService),
          accountState.account.accountKey,
          database,
        )
      }
      SavedStateKeyType.LOCAL -> {
        LocalTimelineMediator(
          (accountState.account.service as MastodonService),
          accountState.account.accountKey,
          database,
        )
      }
    }
  }

  val savedStateKey = remember(accountState) {
    "${accountState.account.accountKey}${savedStateKeyType.key}"
  }

  val source = remember(pagingMediator) {
    pagingMediator.pager(
      config = PagingConfig(
        pageSize = timelinePageSize,
        initialLoadSize = timelineInitialLoadSize
      )
    ).toUi()
  }

  val loadingBetween by remember(pagingMediator) {
    pagingMediator.loadingBetween
  }.collectAsState(
    persistentListOf()
  )

  val listState = key(accountState) {
    rememberLazyListState()
  }

  LaunchedEffect(accountState) {
    val firstVisibleItemIndexKey = intPreferencesKey("$savedStateKey$FIRST_VISIBLE_KEY_SUFFIX")
    val firstVisibleItemScrollOffsetKey =
      intPreferencesKey("$savedStateKey$FIRST_OFFSET_KEY_SUFFIX")
    dataStore.data.firstOrNull()?.let {
      val firstVisibleItemIndex = it[firstVisibleItemIndexKey] ?: 0
      val firstVisibleItemScrollOffset = it[firstVisibleItemScrollOffsetKey] ?: 0
      TimelineScrollState(
        firstVisibleItemIndex = firstVisibleItemIndex,
        firstVisibleItemScrollOffset = firstVisibleItemScrollOffset,
      )
    }?.let {
      listState.scrollToItem(
        it.firstVisibleItemIndex,
        it.firstVisibleItemScrollOffset
      )
    }
  }

  LaunchedEffect(accountState) {
    // TODO FIXME #listState 20211119: listState.isScrollInProgress is always false on desktop
    //  - https://github.com/JetBrains/compose-jb/issues/1423
    snapshotFlow { listState.isScrollInProgress }
      .distinctUntilChanged()
      .filter { !it }
      .filter { listState.layoutInfo.totalItemsCount != 0 }
      .collect {
        dataStore.edit { preferences ->
          val firstVisibleItemIndexKey =
            intPreferencesKey("$savedStateKey$FIRST_VISIBLE_KEY_SUFFIX")
          val firstVisibleItemScrollOffsetKey =
            intPreferencesKey("$savedStateKey$FIRST_OFFSET_KEY_SUFFIX")
          preferences[firstVisibleItemIndexKey] = listState.firstVisibleItemIndex
          preferences[firstVisibleItemScrollOffsetKey] = listState.firstVisibleItemScrollOffset
        }
      }
  }

  LaunchedEffect(Unit) {
    event.catch {}.collect {
      when (it) {
        is TimeLineEvent.LoadBetween -> {
          pagingMediator.loadBetween(
            pageSize = timelinePageSize,
            maxStatusKey = it.maxStatusKey,
            sinceStatusKey = it.sinceStatueKey
          )
        }
      }
    }
  }

  return TimelineState.Data(
    source = source.collectAsLazyPagingItems(),
    loadingBetween = loadingBetween,
    listState = listState
  )
}

interface TimeLineEvent {
  data class LoadBetween(
    val maxStatusKey: MicroBlogKey,
    val sinceStatueKey: MicroBlogKey,
  ) : TimeLineEvent
}

interface TimelineState {
  data class Data(
    val source: LazyPagingItems<UiStatus>,
    val loadingBetween: ImmutableList<MicroBlogKey>,
    val listState: LazyListState
  ) : TimelineState

  object NoAccount : TimelineState
}

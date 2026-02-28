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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.warpnet.services.microblog.LookupService
import com.warpnet.services.microblog.RelationshipService
import com.warpnet.services.microblog.model.IRelationship
import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.extensions.rememberNestedPresenter
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiUser
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.notification.StringNotificationEvent
import com.warpnet.warpnetandroid.repository.UserRepository
import com.warpnet.warpnetandroid.scenes.CurrentAccountPresenter
import com.warpnet.warpnetandroid.scenes.CurrentAccountState
import com.warpnet.warpnetandroid.utils.notifyError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.util.UUID

@Composable
fun UserPresenter(
  event: Flow<UserEvent>,
  userKey: MicroBlogKey,
  repository: UserRepository = get(),
  inAppNotification: InAppNotification = get(),
): UserState {
  val currentAccount = CurrentAccountPresenter()
  if (currentAccount !is CurrentAccountState.Account) {
    return UserState.NoAccount
  }

  val account = remember(currentAccount) {
    currentAccount.account
  }

  var refreshing by remember {
    mutableStateOf(false)
  }

  var loadingRelationship by remember {
    mutableStateOf(false)
  }

  val user by remember(userKey) {
    repository.getUserFlow(userKey)
  }.collectAsState(null)

  var refreshFlow by remember {
    mutableStateOf(UUID.randomUUID())
  }

  var relationship by remember {
    mutableStateOf<IRelationship?>(null)
  }

  val isMe = remember(account) {
    account.accountKey == userKey
  }

  LaunchedEffect(account, refreshFlow) {
    loadingRelationship = true
    val relationshipService =
      account.service as RelationshipService
    relationship = runCatching {
      relationshipService.showRelationship(userKey.id)
    }.onSuccess {
      loadingRelationship = false
    }.onFailure {
      loadingRelationship = false
    }.getOrNull()
  }

  LaunchedEffect(account, refreshFlow) {
    refreshing = true
    runCatching {
      repository.lookupUserById(
        userKey.id,
        accountKey = account.accountKey,
        lookupService = account.service as LookupService,
      )
    }.onFailure {
      inAppNotification.notifyError(it)
    }
    refreshing = false
  }

  suspend fun consumeUserEvent(
    block: suspend (RelationshipService) -> Unit
  ) {
    val relationshipService =
      account.service as? RelationshipService ?: return
    loadingRelationship = true
    try {
      block.invoke(relationshipService)
      refreshFlow = UUID.randomUUID()
    } catch (e: Throwable) {
      inAppNotification.notifyError(e)
    } finally {
      loadingRelationship = false
    }
  }

  val (userTimelineState, userTimelineEvent) = key(account) {
    rememberNestedPresenter<UserTimelineState, UserTimelineEvent> {
      UserTimelinePresenter(it, userKey = userKey)
    }
  }

  val userMediaTimelineState = key(account) {
    UserMediaTimelinePresenter(userKey = userKey)
  }

  val userFavouriteTimelineState = key(account) {
    UserFavouriteTimelinePresenter(userKey = userKey)
  }

  LaunchedEffect(Unit) {
    event.collectLatest {
      when (it) {
        UserEvent.Follow -> {
          consumeUserEvent {
            it.follow(userKey.id)
          }
        }
        UserEvent.UnFollow -> {
          consumeUserEvent {
            it.unfollow(userKey.id)
          }
        }
        UserEvent.Block -> {
          consumeUserEvent {
            it.block(id = userKey.id)
          }
        }
        UserEvent.UnBlock -> {
          consumeUserEvent {
            it.unblock(id = userKey.id)
          }
        }
        UserEvent.Refresh -> {
          refreshFlow = UUID.randomUUID()
        }
        is UserEvent.ExcludeReplies -> {
          userTimelineEvent.trySend(
            UserTimelineEvent.ExcludeReplies(it.exclude)
          )
        }
        is UserEvent.Report -> {
          consumeUserEvent { service ->
            service.report(
              id = userKey.id,
              scenes = it.scenes,
              reason = it.reason,
            )
            inAppNotification.show(
              StringNotificationEvent(
                it.successMessage
              )
            )
          }
        }
      }
    }
  }

  return UserState.Data(
    refreshing = refreshing,
    loadingRelationship = loadingRelationship,
    user = user,
    relationship = relationship,
    isMe = isMe,
    userTimelineState = userTimelineState,
    userFavouriteTimelineState = userFavouriteTimelineState,
    userMediaTimelineState = userMediaTimelineState,
  )
}

sealed interface UserEvent {
  object Follow : UserEvent
  object UnFollow : UserEvent
  object Block : UserEvent
  object UnBlock : UserEvent
  object Refresh : UserEvent
  data class ExcludeReplies(
    val exclude: Boolean
  ) : UserEvent
  data class Report(
    val scenes: List<String>? = null,
    val reason: String? = null,
    val successMessage: String,
  ) : UserEvent
}

interface UserState {
  data class Data(
    val refreshing: Boolean,
    val loadingRelationship: Boolean,
    val user: UiUser?,
    val relationship: IRelationship?,
    val isMe: Boolean,
    val userTimelineState: UserTimelineState,
    val userFavouriteTimelineState: UserFavouriteTimelineState,
    val userMediaTimelineState: UserMediaTimelineState,
  ) : UserState

  object NoAccount : UserState
}

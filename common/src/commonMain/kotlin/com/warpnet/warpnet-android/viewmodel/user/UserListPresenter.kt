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
import androidx.compose.runtime.remember
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.services.microblog.ListsService
import com.warpnet.services.microblog.RelationshipService
import com.warpnet.warpnet-android.di.ext.get
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.ui.UiUser
import com.warpnet.warpnet-android.repository.ListsUsersRepository
import com.warpnet.warpnet-android.repository.UserListRepository
import com.warpnet.warpnet-android.scenes.CurrentAccountPresenter
import com.warpnet.warpnet-android.scenes.CurrentAccountState
import kotlinx.coroutines.flow.Flow

interface UserListType {
  data class Following(
    val userKey: MicroBlogKey
  ) : UserListType
  data class Followers(
    val userKey: MicroBlogKey,
  ) : UserListType
  data class ListUsers(
    val listId: String,
    val viewMembers: Boolean = true,
  ) : UserListType
}

@Composable
fun UserListPresenter(
  event: Flow<UserListEvent>,
  userType: UserListType,
  repository: UserListRepository = get(),
  listsUsersRepository: ListsUsersRepository = get(),
): UserListState {
  val currentAccount = CurrentAccountPresenter()

  if (currentAccount !is CurrentAccountState.Account) {
    return UserListState.NoAccount
  }

  if (userType is UserListType.ListUsers) {
    LaunchedEffect(Unit) {
      event.collect {
        when (it) {
          is UserListEvent.RemoveMember -> {
            listsUsersRepository.removeMember(
              service = currentAccount.account.service as ListsService,
              listId = userType.listId,
              user = it.user
            )
          }
        }
      }
    }
  }

  val source = remember(currentAccount) {
    when (userType) {
      is UserListType.ListUsers -> {
        if (userType.viewMembers) {
          listsUsersRepository.fetchMembers(
            accountKey = currentAccount.account.accountKey,
            service = currentAccount.account.service as ListsService,
            listId = userType.listId
          )
        } else {
          listsUsersRepository.fetchSubscribers(
            accountKey = currentAccount.account.accountKey,
            service = currentAccount.account.service as ListsService,
            listId = userType.listId
          )
        }
      }
      is UserListType.Followers -> {
        repository.followers(
          userType.userKey,
          currentAccount.account.service as RelationshipService
        )
      }
      is UserListType.Following -> {
        repository.following(
          userType.userKey,
          currentAccount.account.service as RelationshipService
        )
      }
      else -> { throw Exception("UserListType not correct") }
    }
  }.collectAsLazyPagingItems()

  return UserListState.Data(source = source)
}

interface UserListEvent {
  data class RemoveMember(
    val user: UiUser
  ) : UserListEvent
}

interface UserListState {
  data class Data(
    val source: LazyPagingItems<UiUser>
  ) : UserListState

  object NoAccount : UserListState
}

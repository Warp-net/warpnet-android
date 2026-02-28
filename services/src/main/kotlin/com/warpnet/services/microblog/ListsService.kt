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
package com.warpnet.services.microblog

import com.warpnet.services.microblog.model.IListModel
import com.warpnet.services.microblog.model.IUser

interface ListsService {
  suspend fun lists(
    userId: String? = null,
    screenName: String? = null,
    reverse: Boolean = true,
  ): List<IListModel>

  suspend fun createList(
    name: String,
    mode: String? = null,
    description: String? = null,
    repliesPolicy: String? = null
  ): IListModel

  suspend fun updateList(
    listId: String,
    name: String? = null,
    mode: String? = null,
    description: String? = null,
    repliesPolicy: String? = null,
  ): IListModel

  suspend fun destroyList(
    listId: String,
  )

  suspend fun listMembers(
    listId: String,
    count: Int = 20,
    cursor: String? = null,
  ): List<IUser>

  suspend fun addMember(
    listId: String,
    userId: String,
    screenName: String,
  )

  suspend fun removeMember(
    listId: String,
    userId: String,
    screenName: String,
  )

  suspend fun listSubscribers(
    listId: String,
    count: Int = 20,
    cursor: String? = null,
  ): List<IUser>

  suspend fun unsubscribeList(listId: String): IListModel

  suspend fun subscribeList(listId: String): IListModel
}

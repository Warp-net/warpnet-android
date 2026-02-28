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
package com.warpnet.warpnetandroid.dataprovider.db.dao

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.warpnet.warpnetandroid.db.dao.DirectMessageConversationDao
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiDMConversation
import com.warpnet.warpnetandroid.model.ui.UiDMConversationWithLatestMessage
import com.warpnet.warpnetandroid.room.db.RoomCacheDatabase
import com.warpnet.warpnetandroid.room.db.paging.getPagingSource
import com.warpnet.warpnetandroid.room.db.transform.toDbDMConversation
import com.warpnet.warpnetandroid.room.db.transform.toUi
import kotlinx.coroutines.flow.map

internal class DirectMessageConversationDaoImpl(private val database: RoomCacheDatabase) : DirectMessageConversationDao {
  override fun getPagingSource(accountKey: MicroBlogKey): PagingSource<Int, UiDMConversationWithLatestMessage> {
    return database.directMessageConversationDao().getPagingSource(cacheDatabase = database, accountKey = accountKey)
  }

  override fun findWithConversationKeyFlow(
    accountKey: MicroBlogKey,
    conversationKey: MicroBlogKey
  ) = database.directMessageConversationDao().findWithConversationKeyFlow(accountKey, conversationKey).map { it?.toUi() }

  override suspend fun findWithConversationKey(
    accountKey: MicroBlogKey,
    conversationKey: MicroBlogKey
  ) = database.directMessageConversationDao().findWithConversationKey(accountKey, conversationKey)?.toUi()

  override suspend fun insertAll(listOf: List<UiDMConversation>) {
    database.directMessageConversationDao().insertAll(listOf.map { it.toDbDMConversation() })
  }

  override suspend fun find(
    accountKey: MicroBlogKey
  ) = database.directMessageConversationDao().find(accountKey).map { it.toUi() }

  override suspend fun delete(conversation: UiDMConversation) {
    database.withTransaction {
      database.directMessageConversationDao().findWithConversationKey(
        accountKey = conversation.accountKey,
        conversationKey = conversation.conversationKey
      )?.let {
        database.directMessageConversationDao().delete(it)
      }
    }
  }
}

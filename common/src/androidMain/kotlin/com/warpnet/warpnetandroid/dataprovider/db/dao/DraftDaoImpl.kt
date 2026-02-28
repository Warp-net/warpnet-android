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

import com.warpnet.warpnetandroid.db.dao.DraftDao
import com.warpnet.warpnetandroid.model.ui.UiDraft
import com.warpnet.warpnetandroid.room.db.dao.RoomDraftDao
import com.warpnet.warpnetandroid.room.db.transform.toDbDraft
import com.warpnet.warpnetandroid.room.db.transform.toUiDraft
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DraftDaoImpl(private val roomDraftDao: RoomDraftDao) : DraftDao {
  override fun getAll(): Flow<List<UiDraft>> = roomDraftDao.getAll().map {
    it.map { dbDraft -> dbDraft.toUiDraft() }
  }

  override fun getDraftCount() = roomDraftDao.getDraftCount().map { it.toLong() }

  override suspend fun insert(it: UiDraft) = roomDraftDao.insertAll(it.toDbDraft())

  override suspend fun get(draftId: String) = roomDraftDao.get(draftId)?.toUiDraft()

  override suspend fun remove(draft: UiDraft) = roomDraftDao.remove(draft.toDbDraft())
}

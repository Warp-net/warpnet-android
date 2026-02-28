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
package com.warpnet.warpnetandroid.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.warpnet.warpnetandroid.room.db.dao.RoomDirectMessageConversationDao
import com.warpnet.warpnetandroid.room.db.dao.RoomDirectMessageEventDao
import com.warpnet.warpnetandroid.room.db.dao.RoomListsDao
import com.warpnet.warpnetandroid.room.db.dao.RoomMediaDao
import com.warpnet.warpnetandroid.room.db.dao.RoomNotificationCursorDao
import com.warpnet.warpnetandroid.room.db.dao.RoomPagingTimelineDao
import com.warpnet.warpnetandroid.room.db.dao.RoomReactionDao
import com.warpnet.warpnetandroid.room.db.dao.RoomStatusDao
import com.warpnet.warpnetandroid.room.db.dao.RoomStatusReferenceDao
import com.warpnet.warpnetandroid.room.db.dao.RoomTrendDao
import com.warpnet.warpnetandroid.room.db.dao.RoomTrendHistoryDao
import com.warpnet.warpnetandroid.room.db.dao.RoomUrlEntityDao
import com.warpnet.warpnetandroid.room.db.dao.RoomUserDao
import com.warpnet.warpnetandroid.room.db.model.DbDMConversation
import com.warpnet.warpnetandroid.room.db.model.DbDMEvent
import com.warpnet.warpnetandroid.room.db.model.DbList
import com.warpnet.warpnetandroid.room.db.model.DbMedia
import com.warpnet.warpnetandroid.room.db.model.DbNotificationCursor
import com.warpnet.warpnetandroid.room.db.model.DbPagingTimeline
import com.warpnet.warpnetandroid.room.db.model.DbStatusReaction
import com.warpnet.warpnetandroid.room.db.model.DbStatusReference
import com.warpnet.warpnetandroid.room.db.model.DbStatusV2
import com.warpnet.warpnetandroid.room.db.model.DbTrend
import com.warpnet.warpnetandroid.room.db.model.DbTrendHistory
import com.warpnet.warpnetandroid.room.db.model.DbUrlEntity
import com.warpnet.warpnetandroid.room.db.model.DbUser
import com.warpnet.warpnetandroid.room.db.model.converter.ExtraConverter
import com.warpnet.warpnetandroid.room.db.model.converter.MastodonVisibilityConverter
import com.warpnet.warpnetandroid.room.db.model.converter.MediaTypeConverter
import com.warpnet.warpnetandroid.room.db.model.converter.MicroBlogKeyConverter
import com.warpnet.warpnetandroid.room.db.model.converter.NotificationCursorTypeConverter
import com.warpnet.warpnetandroid.room.db.model.converter.NotificationTypeConverter
import com.warpnet.warpnetandroid.room.db.model.converter.PlatformTypeConverter
import com.warpnet.warpnetandroid.room.db.model.converter.StringListConverter
import com.warpnet.warpnetandroid.room.db.model.converter.WarpnetReplySettingsConverter
import com.warpnet.warpnetandroid.room.db.model.converter.UserTimelineTypeConverter

@Database(
  entities = [
    DbStatusV2::class,
    DbMedia::class,
    DbUser::class,
    DbStatusReaction::class,
    DbPagingTimeline::class,
    DbUrlEntity::class,
    DbStatusReference::class,
    DbList::class,
    DbNotificationCursor::class,
    DbTrend::class,
    DbTrendHistory::class,
    DbDMConversation::class,
    DbDMEvent::class
  ],
  version = 22,
)
@TypeConverters(
  MicroBlogKeyConverter::class,
  PlatformTypeConverter::class,
  MediaTypeConverter::class,
  UserTimelineTypeConverter::class,
  StringListConverter::class,
  NotificationTypeConverter::class,
  ExtraConverter::class,
  NotificationCursorTypeConverter::class,
  WarpnetReplySettingsConverter::class,
  MastodonVisibilityConverter::class
)
internal abstract class RoomCacheDatabase : RoomDatabase() {
  abstract fun statusDao(): RoomStatusDao
  abstract fun mediaDao(): RoomMediaDao
  abstract fun userDao(): RoomUserDao
  abstract fun reactionDao(): RoomReactionDao
  abstract fun pagingTimelineDao(): RoomPagingTimelineDao
  abstract fun urlEntityDao(): RoomUrlEntityDao
  abstract fun statusReferenceDao(): RoomStatusReferenceDao
  abstract fun listsDao(): RoomListsDao
  abstract fun notificationCursorDao(): RoomNotificationCursorDao
  abstract fun trendDao(): RoomTrendDao
  abstract fun trendHistoryDao(): RoomTrendHistoryDao
  abstract fun directMessageConversationDao(): RoomDirectMessageConversationDao
  abstract fun directMessageDao(): RoomDirectMessageEventDao
}

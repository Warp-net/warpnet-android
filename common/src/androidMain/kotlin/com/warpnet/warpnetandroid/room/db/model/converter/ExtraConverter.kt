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
package com.warpnet.warpnetandroid.room.db.model.converter

import androidx.room.TypeConverter
import com.warpnet.warpnetandroid.room.db.model.DbMastodonStatusExtra
import com.warpnet.warpnetandroid.room.db.model.DbMastodonUserExtra
import com.warpnet.warpnetandroid.room.db.model.DbPoll
import com.warpnet.warpnetandroid.room.db.model.DbPreviewCard
import com.warpnet.warpnetandroid.room.db.model.DbWarpnetStatusExtra
import com.warpnet.warpnetandroid.room.db.model.DbWarpnetUserExtra
import com.warpnet.warpnetandroid.utils.fromJson
import com.warpnet.warpnetandroid.utils.json

internal class ExtraConverter {
  @TypeConverter
  fun fromDbWarpnetStatusExtraString(value: String?): DbWarpnetStatusExtra? {
    return value?.fromJson()
  }

  @TypeConverter
  fun fromTarget(target: DbWarpnetStatusExtra?): String? {
    return target?.json()
  }

  @TypeConverter
  fun fromDbMastodonStatusExtraString(value: String?): DbMastodonStatusExtra? {
    return value?.fromJson()
  }

  @TypeConverter
  fun fromTarget(target: DbMastodonStatusExtra?): String? {
    return target?.json()
  }

  @TypeConverter
  fun fromDbWarpnetUserExtraString(value: String?): DbWarpnetUserExtra? {
    return value?.fromJson()
  }

  @TypeConverter
  fun fromTarget(target: DbWarpnetUserExtra?): String? {
    return target?.json()
  }

  @TypeConverter
  fun fromDbMastodonUserExtraString(value: String?): DbMastodonUserExtra? {
    return value?.fromJson()
  }

  @TypeConverter
  fun fromTarget(target: DbMastodonUserExtra?): String? {
    return target?.json()
  }

  @TypeConverter
  fun fromDbPreviewCard(value: String?): DbPreviewCard? {
    return value?.fromJson()
  }

  @TypeConverter
  fun fromTarget(target: DbPreviewCard?): String? {
    return target?.json()
  }

  @TypeConverter
  fun fromDbPoll(value: String?): DbPoll? {
    return value?.fromJson()
  }

  @TypeConverter
  fun fromTarget(target: DbPoll?): String? {
    return target?.json()
  }
}

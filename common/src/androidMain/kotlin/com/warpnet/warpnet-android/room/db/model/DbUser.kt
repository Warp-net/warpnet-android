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
package com.warpnet.warpnet-android.room.db.model

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.PlatformType
import kotlinx.serialization.Serializable

@Entity(
  tableName = "user",
  indices = [Index(value = ["userKey"], unique = true)],
)
internal data class DbUser(
  /**
   * Id that being used in the database
   */
  @PrimaryKey
  var _id: String,
  val userId: String,
  val name: String,
  val userKey: MicroBlogKey,
  val acct: MicroBlogKey,
  val screenName: String,
  val profileImage: String,
  val profileBackgroundImage: String?,
  val followersCount: Long,
  val friendsCount: Long,
  val listedCount: Long,
  val htmlDesc: String,
  val rawDesc: String,
  val website: String?,
  val location: String?,
  val verified: Boolean,
  val isProtected: Boolean,
  val platformType: PlatformType,
  val statusesCount: Long,
  val extra: Json
)

@Immutable
@Serializable
internal data class DbWarpnetUserExtra(
  val pinned_tweet_id: String?,
  val url: List<WarpnetUrlEntity>,
)

@Immutable
@Serializable
internal data class WarpnetUrlEntity(
  val url: String,
  val expandedUrl: String,
  val displayUrl: String,
)

@Immutable
@Serializable
internal data class DbMastodonUserExtra(
  val fields: List<com.warpnet.services.mastodon.model.Field>,
  val emoji: List<com.warpnet.services.mastodon.model.Emoji>,
  val bot: Boolean,
  val locked: Boolean,
)

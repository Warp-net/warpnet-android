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
package com.warpnet.warpnetandroid.model.ui
import androidx.compose.runtime.Immutable
import com.warpnet.warpnetandroid.model.MicroBlogKey

@Immutable
data class UiList(
  val id: String,
  val ownerId: String,
  val title: String,
  val descriptions: String,
  val mode: String,
  val replyPolicy: String,
  val accountKey: MicroBlogKey,
  val listKey: MicroBlogKey,
  val isFollowed: Boolean,
  val allowToSubscribe: Boolean
) {

  fun isOwner(userId: String): Boolean {
    return ownerId == userId
  }

  val isPrivate: Boolean
    get() = mode == ListsMode.PRIVATE.value

  companion object {

    fun sample(isFollowed: Boolean = true) = UiList(
      id = "1",
      ownerId = "1",
      title = "Sample List",
      descriptions = "Sample List",
      mode = "private",
      replyPolicy = "",
      accountKey = MicroBlogKey.Empty,
      listKey = MicroBlogKey.Empty,
      isFollowed = isFollowed,
      allowToSubscribe = true,
    )
  }
}

enum class ListsMode(val value: String) {
  PRIVATE("private"),
  PUBLIC("public"),
  DEFAULT("")
}

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
package com.warpnet.warpnet-android.model.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.model.ui.mastodon.MastodonUserExtra
import com.warpnet.warpnet-android.model.ui.warpnet.WarpnetUserExtra
import org.jsoup.Jsoup
import java.text.Bidi

@Immutable
data class UiUser(
  val id: String,
  val userKey: MicroBlogKey,
  val acct: MicroBlogKey,
  val name: String,
  val screenName: String,
  val profileImage: String,
  val profileBackgroundImage: String?,
  val metrics: UserMetrics,
  val rawDesc: String,
  val htmlDesc: String,
  val website: String?,
  val location: String?,
  val verified: Boolean,
  val protected: Boolean,
  val platformType: PlatformType,
  val extra: UserExtra? = null
) {
  val displayName
    get() = name.takeUnless { it.isEmpty() } ?: screenName

  val displayNameDocument = Jsoup.parse(displayName.replace("\n", "<br>"))
  val displayNameIsLeftToRight = Bidi(displayName, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).baseIsLeftToRight()

  fun getDisplayScreenName(host: String?): String {
    return if (host != null && host != acct.host) {
      "@$screenName@${acct.host}"
    } else {
      "@$screenName"
    }
  }
  val warpnetExtra: WarpnetUserExtra? = if (extra is WarpnetUserExtra) extra else null

  val mastodonExtra: MastodonUserExtra? = if (extra is MastodonUserExtra) extra else null

  companion object {
    @Composable
    fun sample() = UiUser(
      id = "",
      name = "Warpnet",
      screenName = "WarpnetProject",
      profileImage = "", // painterResource(res = com.warpnet.warpnet-android.MR.files.ic_profile_image_warpnet),
      profileBackgroundImage = null,
      metrics = UserMetrics(
        fans = 0,
        follow = 0,
        status = 0,
        listed = 0
      ),
      rawDesc = "",
      htmlDesc = "",
      website = null,
      location = null,
      verified = false,
      protected = false,
      userKey = MicroBlogKey.Empty,
      platformType = PlatformType.Warpnet,
      acct = MicroBlogKey.warpnet("WarpnetProject")
    )
  }
}

@Immutable
interface UserExtra

@Immutable
data class UserMetrics(
  val fans: Long,
  val follow: Long,
  val status: Long,
  val listed: Long
)

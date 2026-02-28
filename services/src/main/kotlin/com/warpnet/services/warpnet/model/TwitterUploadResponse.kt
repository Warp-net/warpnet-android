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
package com.warpnet.services.warpnet.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WarpnetUploadResponse(
  @SerialName("media_id")
  val mediaID: Long? = null,

  @SerialName("media_id_string")
  val mediaIDString: String? = null,

  @SerialName("expires_after_secs")
  val expiresAfterSecs: Long? = null,

  @SerialName("processing_info")
  val processingInfo: WarpnetUploadProcessInfo? = null
)

@Serializable
data class WarpnetUploadProcessInfo(
  @SerialName("check_after_secs")
  val checkAfterSecs: Int? = null,
  @SerialName("progress_percent")
  val progressPercent: Int? = null,
  @SerialName("state")
  val state: String? = null
)

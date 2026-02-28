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

import com.warpnet.services.microblog.model.ISearchResponse
import com.warpnet.services.microblog.model.IStatus
import kotlinx.serialization.Serializable

@Serializable
data class WarpnetSearchResponseV2(
  val meta: Meta? = null,
  val data: List<StatusV2>? = null,
  val errors: List<WarpnetErrorV2>? = null,
  val includes: IncludesV2? = null
) : ISearchResponse {
  override val nextPage: String?
    get() = meta?.nextToken
  override val status: List<IStatus>
    get() = data ?: emptyList()
}

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
package com.warpnet.services.warpnet.model.exceptions

import com.warpnet.services.http.MicroBlogException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WarpnetApiExceptionV2(
  @SerialName("account_id")
  val accountID: Long? = null,

  @SerialName("product_name")
  val productName: String? = null,

  val title: String? = null,
  val period: String? = null,
  val scope: String? = null,
  val detail: String? = null,
  val type: String? = null,
) : MicroBlogException() {
  override val microBlogErrorMessage: String?
    get() = detail
}

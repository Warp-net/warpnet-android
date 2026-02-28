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
package com.warpnet.services.utils

import com.warpnet.services.http.MicroBlogException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

internal val JSON by lazy {
  Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
  }
}

class MicroBlogJsonException(override val microBlogErrorMessage: String?) : MicroBlogException()

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T> T.encodeJson(): String =
  JSON.encodeToString<T>(this)

internal inline fun <reified T> String.decodeJson(): T {
  return runCatching {
    JSON.parseToJsonElement(this)
  }.getOrNull()?.let {
    runCatching {
      JSON.decodeFromJsonElement<T>(it)
    }.getOrNull()
  } ?: throw MicroBlogJsonException(this)
}

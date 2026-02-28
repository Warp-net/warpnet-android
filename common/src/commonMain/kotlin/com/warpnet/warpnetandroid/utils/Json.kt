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
package com.warpnet.warpnetandroid.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val JSON by lazy {
  Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
  }
}

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
internal inline fun <reified T> T.json(): String =
  JSON.encodeToString<T>(this)

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
internal inline fun <reified T> String.fromJson() =
  JSON.decodeFromString<T>(this)

fun <T> T.json(serializer: KSerializer<T>) = JSON.encodeToString(serializer, this)

fun <T> String.fromJson(serializer: KSerializer<T>) = JSON.decodeFromString(serializer, this)

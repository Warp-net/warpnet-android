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
package com.warpnet.warpnet-android.preferences.serializer

import androidx.datastore.core.Serializer
import com.warpnet.warpnet-android.preferences.model.SwipePreferences
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
object SwipePreferencesSerializer : Serializer<SwipePreferences> {
  override suspend fun readFrom(input: InputStream): SwipePreferences {
    return ProtoBuf.decodeFromByteArray(input.readBytes())
  }
  override suspend fun writeTo(
    t: SwipePreferences,
    output: OutputStream
  ) = output.write(ProtoBuf.encodeToByteArray(t))

  override val defaultValue: SwipePreferences
    get() = SwipePreferences()
}

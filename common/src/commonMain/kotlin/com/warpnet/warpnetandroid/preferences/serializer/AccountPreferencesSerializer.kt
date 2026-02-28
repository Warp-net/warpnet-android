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
package com.warpnet.warpnetandroid.preferences.serializer

import androidx.datastore.core.Serializer
import com.warpnet.warpnetandroid.preferences.model.AccountPreferences
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalSerializationApi::class)
object AccountPreferencesSerializer : Serializer<AccountPreferences> {
  override suspend fun readFrom(input: InputStream): AccountPreferences {
    return ProtoBuf.decodeFromByteArray(input.readBytes())
  }
  override suspend fun writeTo(
    t: AccountPreferences,
    output: OutputStream
  ) = output.write(ProtoBuf.encodeToByteArray(t))

  override val defaultValue: AccountPreferences
    get() = AccountPreferences()
}

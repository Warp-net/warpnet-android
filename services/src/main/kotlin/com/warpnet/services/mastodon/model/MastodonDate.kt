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
package com.warpnet.services.mastodon.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant
import java.time.format.DateTimeFormatter

@Serializable(with = MastodonDateSerializer::class)
data class MastodonDate(val millis: Long)

object MastodonDateSerializer : KSerializer<MastodonDate> {
  private val formatter = DateTimeFormatter.ISO_DATE_TIME

  override val descriptor = PrimitiveSerialDescriptor("MastodonDate", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: MastodonDate) {
    encoder.encodeString(Instant.ofEpochMilli(value.millis).toString())
  }

  override fun deserialize(decoder: Decoder): MastodonDate {
    return runCatching {
      val str = decoder.decodeString()
      val instant = Instant.from(formatter.parse(str))
      MastodonDate(instant.toEpochMilli())
    }.getOrElse { MastodonDate(0L) }
  }
}

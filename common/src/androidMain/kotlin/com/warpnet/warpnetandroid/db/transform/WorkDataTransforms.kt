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
package com.warpnet.warpnetandroid.db.transform

import androidx.work.Data
import com.warpnet.warpnetandroid.model.job.ComposeData
import com.warpnet.warpnetandroid.model.job.DirectMessageDeleteData
import com.warpnet.warpnetandroid.model.job.DirectMessageSendData

/**
 * Stub transform functions for WorkManager data serialization.
 * These are no-op implementations for a stateless app.
 */

fun ComposeData.toWorkData(): Data {
  // Stub implementation for stateless mode
  // Background work operations requiring data serialization will fail
  throw NotImplementedError("WorkData transforms are not supported in stateless mode. Background work operations requiring data serialization will fail.")
}

fun Data.toComposeData(): ComposeData {
  throw NotImplementedError("WorkData transforms are not supported in stateless mode. Background work operations requiring data serialization will fail.")
}

fun DirectMessageDeleteData.toWorkData(): Data {
  throw NotImplementedError("WorkData transforms are not supported in stateless mode. Background work operations requiring data serialization will fail.")
}

fun Data.toDirectMessageDeleteData(): DirectMessageDeleteData {
  throw NotImplementedError("WorkData transforms are not supported in stateless mode. Background work operations requiring data serialization will fail.")
}

fun DirectMessageSendData.toWorkData(): Data {
  throw NotImplementedError("WorkData transforms are not supported in stateless mode. Background work operations requiring data serialization will fail.")
}

fun Data.toDirectMessageSendData(): DirectMessageSendData {
  throw NotImplementedError("WorkData transforms are not supported in stateless mode. Background work operations requiring data serialization will fail.")
}

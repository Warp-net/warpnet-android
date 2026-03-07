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

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun InputStream.copyToInLength(output: OutputStream, length: Int) {
  val buffer = ByteArray(1024)
  var bytesRead = 0
  do {
    val read = read(buffer)
    if (read == -1) {
      break
    }
    output.write(buffer, 0, read)
    bytesRead += read
  } while (bytesRead <= length)
}

suspend fun Call.await(): Response = suspendCancellableCoroutine { continuation ->
  enqueue(object : Callback {
    override fun onResponse(call: Call, response: Response) {
      continuation.resume(response)
    }

    override fun onFailure(call: Call, e: IOException) {
      if (continuation.isCancelled) return
      continuation.resumeWithException(e)
    }
  })
  continuation.invokeOnCancellation {
    try {
      cancel()
    } catch (ignored: Throwable) {
    }
  }
}

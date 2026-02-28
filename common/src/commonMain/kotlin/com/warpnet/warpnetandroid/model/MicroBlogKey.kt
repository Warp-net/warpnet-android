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
package com.warpnet.warpnetandroid.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class MicroBlogKey(
  val id: String,
  val host: String,
) {
  override fun toString(): String {
    return if (host.isNotEmpty()) escapeText(id).toString() + "@" + escapeText(host) else id
  }

  private fun escapeText(host: String): String? {
    val sb = java.lang.StringBuilder()
    var i = 0
    val j = host.length
    while (i < j) {
      val ch = host[i]
      if (isSpecialChar(ch)) {
        sb.append('\\')
      }
      sb.append(ch)
      i++
    }
    return sb.toString()
  }

  private fun isSpecialChar(ch: Char): Boolean {
    return ch == '\\' || ch == '@' || ch == ','
  }

  companion object {
    const val WarpnetHost = "warpnet.com"
    val Empty: MicroBlogKey = MicroBlogKey("", "")
    fun warpnet(id: String) = MicroBlogKey(id, WarpnetHost)

    fun valueOf(str: String): MicroBlogKey {
      var escaping = false
      var idFinished = false
      val idBuilder = StringBuilder(str.length)
      val hostBuilder = StringBuilder(str.length)
      var i = 0
      val j = str.length
      while (i < j) {
        val ch = str[i]
        var append = false
        if (escaping) {
          // accept all characters if is escaping
          append = true
          escaping = false
        } else if (ch == '\\') {
          escaping = true
        } else if (ch == '@') {
          idFinished = true
        } else if (ch == ',') {
          // end of item, just jump out
          break
        } else {
          append = true
        }
        if (append) {
          if (idFinished) {
            hostBuilder.append(ch)
          } else {
            idBuilder.append(ch)
          }
        }
        i++
      }
      return if (hostBuilder.isNotEmpty()) {
        MicroBlogKey(idBuilder.toString(), hostBuilder.toString())
      } else {
        MicroBlogKey(idBuilder.toString(), "")
      }
    }
  }
}

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
package com.warpnet.services.http.authorization

import okhttp3.Request
import java.net.URLEncoder
import java.util.Base64
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

class OAuth1Authorization(
  private val consumerKey: String,
  private val consumerSecret: String,
  private val accessToken: String = "",
  private val accessTokenSecret: String = "",
) : Authorization {
  override val hasAuthorization: Boolean = true

  override fun signRequest(request: Request): Request {
    val timestamp = (System.currentTimeMillis() / 1000).toString()
    val nonce = UUID.randomUUID().toString().replace("-", "")

    val oauthParams = mutableMapOf(
      "oauth_consumer_key" to consumerKey,
      "oauth_nonce" to nonce,
      "oauth_signature_method" to "HMAC-SHA1",
      "oauth_timestamp" to timestamp,
      "oauth_token" to accessToken,
      "oauth_version" to "1.0",
    )

    val url = request.url
    val queryParams = mutableMapOf<String, String>()
    for (i in 0 until url.querySize) {
      queryParams[url.queryParameterName(i)] = url.queryParameterValue(i) ?: ""
    }

    val allParams = (oauthParams + queryParams).toSortedMap()
    val paramString = allParams.entries.joinToString("&") { (k, v) ->
      "${encode(k)}=${encode(v)}"
    }

    val method = request.method.uppercase()
    val baseUrl = "${url.scheme}://${url.host}${url.encodedPath}"
    val baseString = "${encode(method)}&${encode(baseUrl)}&${encode(paramString)}"
    val signingKey = "${encode(consumerSecret)}&${encode(accessTokenSecret)}"

    val signature = hmacSha1(signingKey, baseString)

    val authHeader = buildString {
      append("OAuth ")
      append(
        (oauthParams + mapOf("oauth_signature" to signature))
          .entries
          .joinToString(", ") { (k, v) -> """$k="${encode(v)}"""" }
      )
    }

    return request.newBuilder()
      .header("Authorization", authHeader)
      .build()
  }

  private fun encode(value: String): String =
    URLEncoder.encode(value, "UTF-8")
      .replace("+", "%20")
      .replace("*", "%2A")
      .replace("%7E", "~")

  private fun hmacSha1(key: String, data: String): String {
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(SecretKeySpec(key.toByteArray(Charsets.UTF_8), "HmacSHA1"))
    return Base64.getEncoder().encodeToString(mac.doFinal(data.toByteArray(Charsets.UTF_8)))
  }
}

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
package com.warpnet.services.http

import com.warpnet.services.http.authorization.Authorization
import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(
  private val authorization: Authorization,
) : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    return if (authorization.hasAuthorization) {
      val signed = authorization.signRequest(request)
      chain.proceed(signed)
    } else {
      chain.proceed(request)
    }
  }
}

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
package com.warpnet.services.microblog

import com.warpnet.services.microblog.model.IStatus
import com.warpnet.services.microblog.model.IUser

interface LookupService {
  suspend fun lookupUserByName(
    name: String
  ): IUser

  suspend fun lookupUsersByName(
    name: List<String>
  ): List<IUser>

  suspend fun lookupUser(
    id: String
  ): IUser

  suspend fun lookupStatus(
    id: String
  ): IStatus

  suspend fun userPinnedStatus(
    userId: String,
  ): List<IStatus>
}

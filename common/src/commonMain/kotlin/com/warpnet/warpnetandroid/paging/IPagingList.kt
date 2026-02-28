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
package com.warpnet.warpnetandroid.paging

interface IPagingList<T, P : IPagination> : List<T> {
  val nextPage: P?
}

interface IPagination

data class SinceMaxPagination(
  val maxId: String? = null,
  val sinceId: String? = null,
) : IPagination

data class CursorPagination(
  val cursor: String?,
) : IPagination

class PagingList<T, P : IPagination>(
  data: List<T>,
  override val nextPage: P? = null,
) : ArrayListCompat<T>(data), IPagingList<T, P>

// FIXME: 2021/3/31 workaround for java.lang.NoSuchMethodError: No virtual method getSize()I
open class ArrayListCompat<T>(
  private val data: List<T>
) : AbstractList<T>() {
  override val size: Int
    get() = data.size

  override fun get(index: Int): T = data[index]
}

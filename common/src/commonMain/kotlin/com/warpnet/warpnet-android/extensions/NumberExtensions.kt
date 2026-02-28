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
package com.warpnet.warpnet-android.extensions

operator fun Int.contains(i: Int): Boolean = (this and i) == i

fun <T> T.isInRange(minimumValue: T, maximumValue: T): Boolean
    where T : Number, T : Comparable<T> {
  if (minimumValue > maximumValue) throw IllegalArgumentException("Cannot compare value to an empty range: maximum $maximumValue is less than minimum $minimumValue.")
  return this < maximumValue && this > minimumValue
}

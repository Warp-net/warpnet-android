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
package com.warpnet.warpnet-android.di.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.koin.core.Koin
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.mp.KoinPlatformTools

@Composable
inline fun <reified T> getRemember(
  qualifier: Qualifier? = null,
  noinline parameters: ParametersDefinition? = null,
): T = remember(qualifier, parameters) {
  get(qualifier, parameters)
}

@Composable
fun getKoinRemember(): Koin = remember {
  getKoin()
}

inline fun <reified T> get(
  qualifier: Qualifier? = null,
  noinline parameters: ParametersDefinition? = null,
): T = KoinPlatformTools.defaultContext().get().get(qualifier, parameters)

fun getKoin(): Koin = KoinPlatformTools.defaultContext().get()

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
package com.warpnet.warpnetandroid.di.modules

import com.warpnet.warpnetandroid.kmp.ExifScrambler
import com.warpnet.warpnetandroid.kmp.FileResolver
import com.warpnet.warpnetandroid.kmp.IconModifier
import com.warpnet.warpnetandroid.kmp.LocationProvider
import com.warpnet.warpnetandroid.kmp.MediaInsertProvider
import com.warpnet.warpnetandroid.kmp.OrientationSensorManager
import com.warpnet.warpnetandroid.kmp.RemoteNavigator
import com.warpnet.warpnetandroid.kmp.ResLoader
import com.warpnet.warpnetandroid.notification.AppNotificationManager
import org.koin.dsl.module

actual val kmpModule = module {
  single { ExifScrambler(get()) }
  single { FileResolver(get()) }
  single { LocationProvider(get()) }
  single { RemoteNavigator(get()) }
  single { ResLoader(get()) }
  single { AppNotificationManager(get(), get()) }
  single { MediaInsertProvider(get()) }
  single { OrientationSensorManager(get()) }
  single { IconModifier(get()) }
}

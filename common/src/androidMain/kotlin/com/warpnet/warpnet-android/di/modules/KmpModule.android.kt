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
package com.warpnet.warpnet-android.di.modules

import com.warpnet.warpnet-android.kmp.ExifScrambler
import com.warpnet.warpnet-android.kmp.FileResolver
import com.warpnet.warpnet-android.kmp.IconModifier
import com.warpnet.warpnet-android.kmp.LocationProvider
import com.warpnet.warpnet-android.kmp.MediaInsertProvider
import com.warpnet.warpnet-android.kmp.OrientationSensorManager
import com.warpnet.warpnet-android.kmp.RemoteNavigator
import com.warpnet.warpnet-android.kmp.ResLoader
import com.warpnet.warpnet-android.notification.AppNotificationManager
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

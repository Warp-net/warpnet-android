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
package com.warpnet.warpnet-android.di

import com.warpnet.warpnet-android.di.modules.actionModule
import com.warpnet.warpnet-android.di.modules.dataBaseModule
import com.warpnet.warpnet-android.di.modules.jobsModule
import com.warpnet.warpnet-android.di.modules.kmpModule
import com.warpnet.warpnet-android.di.modules.platformModule
import com.warpnet.warpnet-android.di.modules.preferencesModule
import com.warpnet.warpnet-android.di.modules.repositoryModule
import com.warpnet.warpnet-android.di.modules.storageProviderModule
import com.warpnet.warpnet-android.di.modules.viewModelModule
import com.warpnet.warpnet-android.utils.OAuthLauncher
import org.koin.core.KoinApplication
import org.koin.dsl.module

fun KoinApplication.setupModules() {
  modules(storageProviderModule)
  modules(preferencesModule)
  modules(dataBaseModule)
  modules(platformModule)
  modules(viewModelModule)
  modules(repositoryModule)
  modules(actionModule)
  modules(jobsModule)
  modules(kmpModule)
  modules(
    module {
      single {
        OAuthLauncher(get())
      }
    }
  )
}

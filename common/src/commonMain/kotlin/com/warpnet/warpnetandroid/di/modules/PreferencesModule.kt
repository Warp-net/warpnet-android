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

import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.warpnet.warpnetandroid.kmp.StorageProvider
import com.warpnet.warpnetandroid.kmp.appFiles
import com.warpnet.warpnetandroid.preferences.PreferencesHolder
import com.warpnet.warpnetandroid.preferences.serializer.AccountPreferencesSerializer
import com.warpnet.warpnetandroid.preferences.serializer.AppearancePreferencesSerializer
import com.warpnet.warpnetandroid.preferences.serializer.DisplayPreferencesSerializer
import com.warpnet.warpnetandroid.preferences.serializer.MiscPreferencesSerializer
import com.warpnet.warpnetandroid.preferences.serializer.NotificationPreferencesSerializer
import com.warpnet.warpnetandroid.preferences.serializer.SwipePreferencesSerializer
import org.koin.core.scope.Scope
import org.koin.dsl.module
import java.io.File

internal val preferencesModule = module {
  single {
    PreferencesHolder(
      accountPreferences = createDataStore(
        "accountConfig.pb",
        AccountPreferencesSerializer
      ),
      appearancePreferences = createDataStore(
        "appearances.pb",
        AppearancePreferencesSerializer
      ),
      displayPreferences = createDataStore("display.pb", DisplayPreferencesSerializer),
      miscPreferences = createDataStore("misc.pb", MiscPreferencesSerializer),
      notificationPreferences = createDataStore(
        "notification.pb",
        NotificationPreferencesSerializer
      ),
      swipePreferences = createDataStore(
        "swipe.pb",
        SwipePreferencesSerializer
      )
    )
  }
  single {
    PreferenceDataStoreFactory.create {
      createDataStoreFile("perferences.preferences_pb")
    }
  }
}

internal inline fun <reified T : Any> Scope.createDataStore(
  name: String,
  serializer: Serializer<T>,
) = DataStoreFactory.create(
  serializer,
  produceFile = {
    createDataStoreFile(name)
  },
)

private fun Scope.createDataStoreFile(name: String): File {
  return File(get<StorageProvider>().appFiles.dataStoreFile(name))
}

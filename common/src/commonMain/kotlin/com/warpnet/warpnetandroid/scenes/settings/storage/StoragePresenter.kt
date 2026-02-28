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
package com.warpnet.warpnetandroid.scenes.settings.storage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.repository.CacheRepository
import kotlinx.coroutines.flow.Flow

@Composable
fun StoragePresenter(
  event: Flow<StorageEvent>,
  repository: CacheRepository = get(),
): StorageState {
  var loading by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    event.collect {
      when (it) {
        StorageEvent.ClearAllCaches -> {
          loading = true
          repository.clearDatabaseCache()
          repository.clearCacheDir()
          repository.clearImageCache()
          loading = false
        }
        StorageEvent.ClearImageCache -> {
          loading = true
          repository.clearImageCache()
          loading = false
        }
        StorageEvent.ClearSearchHistory -> {
          loading = true
          repository.clearSearchHistory()
          loading = false
        }
      }
    }
  }
  return StorageState(
    loading = loading
  )
}

data class StorageState(
  val loading: Boolean,
)

interface StorageEvent {
  object ClearImageCache : StorageEvent
  object ClearSearchHistory : StorageEvent
  object ClearAllCaches : StorageEvent
}

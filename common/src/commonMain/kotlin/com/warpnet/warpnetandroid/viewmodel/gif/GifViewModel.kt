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
package com.warpnet.warpnetandroid.viewmodel.gif

import androidx.compose.ui.text.intl.Locale
import androidx.paging.cachedIn
import com.warpnet.warpnetandroid.http.WarpnetServiceFactory
import com.warpnet.warpnetandroid.kmp.StorageProvider
import com.warpnet.warpnetandroid.kmp.appFiles
import com.warpnet.warpnetandroid.kmp.mkFile
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.model.ui.UiGif
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.repository.GifRepository
import com.warpnet.warpnetandroid.utils.notifyError
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class GifViewModel(
  private val gifRepository: GifRepository,
  private val storageProvider: StorageProvider,
  private val inAppNotification: InAppNotification,
) : ViewModel() {

  val input = MutableStateFlow("")

  val selectedItem = MutableStateFlow<UiGif?>(null)

  val enable = selectedItem.map { it != null }

  private val service = WarpnetServiceFactory.createGifService()

  @OptIn(FlowPreview::class)
  val searchFlow = input.debounce(666L).map {
    it.takeIf { it.isNotEmpty() }?.let { query ->
      gifRepository.gifSearch(
        service = service,
        query = query,
        lang = Locale.current.language
      )
    }
  }

  val trendSource = gifRepository.gifTrending(service = service).cachedIn(viewModelScope)

  private val _commitLoading = MutableStateFlow(false)

  val commitLoading get() = _commitLoading

  fun commit(platform: PlatformType, onSuccess: (path: String) -> Unit) {
    selectedItem.value?.let {
      // mastodon support image/video only
      val url = when (platform) {
        PlatformType.Mastodon -> it.mp4
        else -> it.url
      }
      val suffix = when (platform) {
        PlatformType.Mastodon -> "mp4"
        else -> it.type
      }
      viewModelScope.launch {
        _commitLoading.value = true
        try {
          val target = storageProvider.appFiles.mediaFile("${it.id}.$suffix").mkFile()
          gifRepository.download(target = target, source = url, service = service)
          onSuccess(target)
        } catch (e: Throwable) {
          e.printStackTrace()
          inAppNotification.notifyError(e)
        } finally {
          _commitLoading.value = false
        }
      }
    }
  }
}

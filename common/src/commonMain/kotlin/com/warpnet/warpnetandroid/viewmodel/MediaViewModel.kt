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
package com.warpnet.warpnetandroid.viewmodel

import com.warpnet.warpnetandroid.action.MediaAction
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.ui.UiMedia
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.repository.StatusRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import moe.tlaster.precompose.viewmodel.ViewModel

class MediaViewModel(
  private val repository: StatusRepository,
  private val accountRepository: AccountRepository,
  private val mediaAction: MediaAction,
  private val statusKey: MicroBlogKey,
) : ViewModel() {
  private val account by lazy {
    accountRepository.activeAccount.mapNotNull { it }
  }

  suspend fun saveFile(currentMedia: UiMedia, target: suspend (fileName: String) -> String?) {
    val account = account.firstOrNull() ?: return
    val fileName = currentMedia.fileName ?: return
    val path = target.invoke(fileName) ?: return
    currentMedia.mediaUrl?.let {
      mediaAction.download(
        accountKey = account.accountKey,
        source = it,
        target = path
      )
    }
  }

  suspend fun shareMedia(currentMedia: UiMedia, extraText: String = "") {
    val account = account.firstOrNull() ?: return
    currentMedia.mediaUrl?.let { mediaUrl ->
      currentMedia.fileName?.let { fileName ->
        mediaAction.share(
          source = mediaUrl,
          fileName = fileName,
          accountKey = account.accountKey,
          extraText = extraText
        )
      }
    }
  }

  val loading = MutableStateFlow(false)

  @OptIn(ExperimentalCoroutinesApi::class)
  val status by lazy {
    account.flatMapLatest {
      repository.loadStatus(
        statusKey = statusKey,
        accountKey = it.accountKey,
      )
    }
  }

  // init {
  //     viewModelScope.launch {
  //         try {
  //             repository.loadTweetFromNetwork(
  //                 statusKey.id,
  //                 account.accountKey,
  //                 account.service as LookupService
  //             )
  //         } catch (e: Throwable) {
  //             e.notify(inAppNotification)
  //         }
  //     }
  // }
}

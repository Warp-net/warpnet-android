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
package com.warpnet.warpnet-android.viewmodel.dm

import androidx.paging.cachedIn
import com.warpnet.services.microblog.DirectMessageService
import com.warpnet.services.microblog.LookupService
import com.warpnet.warpnet-android.action.DirectMessageAction
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.model.job.DirectMessageDeleteData
import com.warpnet.warpnet-android.model.job.DirectMessageSendData
import com.warpnet.warpnet-android.model.ui.UiDMEvent
import com.warpnet.warpnet-android.model.ui.UiMediaInsert
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.repository.DirectMessageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import java.util.UUID

class DMEventViewModel(
  private val repository: DirectMessageRepository,
  private val messageAction: DirectMessageAction,
  private val accountRepository: AccountRepository,
  private val conversationKey: MicroBlogKey,
) : ViewModel() {
  private val account by lazy {
    accountRepository.activeAccount.mapNotNull { it }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  val conversation by lazy {
    account.flatMapLatest { account ->
      repository.dmConversation(
        accountKey = account.accountKey,
        conversationKey = conversationKey
      )
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  val source by lazy {
    account.filter { it.service is DirectMessageService }.flatMapLatest { account ->
      repository.dmEventListSource(
        accountKey = account.accountKey,
        conversationKey = conversationKey,
        service = account.service as DirectMessageService,
        lookupService = account.service as LookupService
      )
    }.cachedIn(viewModelScope)
  }

  // input
  val input = MutableStateFlow("")
  val inputImage = MutableStateFlow<UiMediaInsert?>(null)
  val firstEventKey = MutableStateFlow<String?>(null)
  val pendingActionMessage = MutableStateFlow<UiDMEvent?>(null)

  fun sendMessage() {
    if (input.value.isEmpty() && inputImage.value == null) return
    viewModelScope.launch {
      account.firstOrNull()?.let { account ->
        conversation.firstOrNull()?.let {
          messageAction.send(
            account.type,
            data = DirectMessageSendData(
              text = input.value,
              images = inputImage.value?.let { insert -> listOf(insert.filePath) }
                ?: emptyList(),
              recipientUserKey = it.recipientKey,
              draftMessageKey = when (account.type) {
                PlatformType.Warpnet -> MicroBlogKey.warpnet(
                  UUID.randomUUID().toString()
                )
                PlatformType.StatusNet -> TODO()
                PlatformType.Fanfou -> TODO()
                PlatformType.Mastodon -> MicroBlogKey.Empty
              },
              conversationKey = it.conversationKey,
              accountKey = account.accountKey
            )
          )
          input.value = ""
          inputImage.value = null
        }
      }
    }
  }

  fun sendDraftMessage(event: UiDMEvent) = viewModelScope.launch {
    account.firstOrNull()?.let { account ->
      messageAction.send(
        account.type,
        data = DirectMessageSendData(
          text = event.originText,
          images = event.media.mapNotNull { it.url },
          recipientUserKey = event.recipientAccountKey,
          conversationKey = event.conversationKey,
          accountKey = account.accountKey,
          draftMessageKey = event.messageKey
        )
      )
    }
  }

  fun deleteMessage(event: UiDMEvent) = viewModelScope.launch {
    account.firstOrNull()?.let { account ->
      messageAction.delete(
        data = DirectMessageDeleteData(
          messageId = event.messageId,
          messageKey = event.messageKey,
          conversationKey = event.conversationKey,
          accountKey = account.accountKey
        )
      )
    }
  }
}

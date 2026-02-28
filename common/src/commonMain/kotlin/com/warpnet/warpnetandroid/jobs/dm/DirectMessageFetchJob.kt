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
package com.warpnet.warpnetandroid.jobs.dm

import com.warpnet.services.microblog.DirectMessageService
import com.warpnet.services.microblog.LookupService
import com.warpnet.warpnetandroid.kmp.ResLoader
import com.warpnet.warpnetandroid.model.AccountDetails
import com.warpnet.warpnetandroid.model.ui.UiDMConversationWithLatestMessage
import com.warpnet.warpnetandroid.navigation.RootDeepLinks
import com.warpnet.warpnetandroid.notification.AppNotification
import com.warpnet.warpnetandroid.notification.AppNotificationManager
import com.warpnet.warpnetandroid.notification.NotificationChannelSpec
import com.warpnet.warpnetandroid.notification.notificationChannelId
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.repository.DirectMessageRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull

class DirectMessageFetchJob(
  private val repository: DirectMessageRepository,
  private val accountRepository: AccountRepository,
  private val notificationManager: AppNotificationManager,
  private val resLoader: ResLoader,
) {
  suspend fun execute() {
    accountRepository.activeAccount.firstOrNull()?.takeIf {
      accountRepository.getAccountPreferences(it.accountKey).isNotificationEnabled.first()
    }?.let { account ->
      (account.service as? DirectMessageService)?.let { directMessageService ->
        val result = repository.checkNewMessages(
          accountKey = account.accountKey,
          service = directMessageService,
          lookupService = account.service as LookupService
        )
        result.forEach {
          notification(account = account, message = it)
        }
      }
    }
  }

  private fun notification(account: AccountDetails, message: UiDMConversationWithLatestMessage) {
    val builder = AppNotification
      .Builder(
        account.accountKey.notificationChannelId(
          NotificationChannelSpec.ContentMessages.id
        )
      )
      .setContentTitle(resLoader.getString(com.warpnet.warpnetandroid.MR.strings.common_notification_messages_title))
      .setContentText(
        resLoader.getString(
          com.warpnet.warpnetandroid.MR.strings.common_notification_messages_content,
          message.latestMessage.sender.displayName
        )
      )
      .setDeepLink(RootDeepLinks.Conversation(message.conversation.conversationKey))
    notificationManager.notify(message.latestMessage.messageKey.hashCode(), builder.build())
  }
}

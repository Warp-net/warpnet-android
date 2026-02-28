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
package com.warpnet.warpnet-android.jobs.compose

import com.warpnet.services.mastodon.MastodonService
import com.warpnet.services.mastodon.model.PostPoll
import com.warpnet.services.mastodon.model.PostStatus
import com.warpnet.services.mastodon.model.Visibility
import com.warpnet.warpnet-android.dataprovider.mapper.toUi
import com.warpnet.warpnet-android.db.CacheDatabase
import com.warpnet.warpnet-android.kmp.ExifScrambler
import com.warpnet.warpnet-android.kmp.FileResolver
import com.warpnet.warpnet-android.kmp.RemoteNavigator
import com.warpnet.warpnet-android.kmp.ResLoader
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.enums.ComposeType
import com.warpnet.warpnet-android.model.enums.MastodonVisibility
import com.warpnet.warpnet-android.model.job.ComposeData
import com.warpnet.warpnet-android.model.ui.UiStatus
import com.warpnet.warpnet-android.notification.AppNotificationManager
import com.warpnet.warpnet-android.repository.AccountRepository
import java.io.File
import java.net.URI

class MastodonComposeJob(
  accountRepository: AccountRepository,
  notificationManager: AppNotificationManager,
  exifScrambler: ExifScrambler,
  remoteNavigator: RemoteNavigator,
  resLoader: ResLoader,
  private val fileResolver: FileResolver,
  private val cacheDatabase: CacheDatabase,
) : ComposeJob<MastodonService>(
  accountRepository,
  notificationManager,
  exifScrambler,
  remoteNavigator,
  resLoader,
) {
  override suspend fun compose(
    service: MastodonService,
    composeData: ComposeData,
    accountKey: MicroBlogKey,
    mediaIds: ArrayList<String>
  ): UiStatus {
    return service.compose(
      PostStatus(
        status = composeData.content,
        inReplyToID = if (composeData.composeType == ComposeType.Reply || composeData.composeType == ComposeType.Thread) composeData.statusKey?.id else null,
        mediaIDS = mediaIds,
        sensitive = composeData.isSensitive,
        spoilerText = composeData.contentWarningText,
        visibility = when (composeData.visibility) {
          MastodonVisibility.Public, null -> Visibility.Public
          MastodonVisibility.Unlisted -> Visibility.Unlisted
          MastodonVisibility.Private -> Visibility.Private
          MastodonVisibility.Direct -> Visibility.Direct
        },
        poll = composeData.voteOptions?.let {
          PostPoll(
            options = composeData.voteOptions,
            expiresIn = composeData.voteExpired?.value,
            multiple = composeData.voteMultiple
          )
        }
      )
    ).toUi(accountKey).also {
      cacheDatabase.statusDao().insertAll(listOf = listOf(it), accountKey = accountKey)
    }
  }

  override suspend fun uploadImage(
    originUri: String,
    scramblerUri: String,
    service: MastodonService
  ): String? {
    val id = fileResolver.openInputStream(scramblerUri)?.use { input ->
      service.upload(
        input,
        URI.create(originUri).path?.let { File(it).name }?.takeIf { it.isNotEmpty() } ?: "file"
      )
    } ?: throw Error()
    return id.id
  }

  override val imageMaxSize: Long
    get() = 100 * 1024 * 1024
}

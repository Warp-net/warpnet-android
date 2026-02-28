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

import android.accounts.AccountManager
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.warpnet.warpnet-android.http.WarpnetHttpConfigProvider
import com.warpnet.warpnet-android.kmp.ResLoader
import com.warpnet.warpnet-android.model.AccountPreferencesFactory
import com.warpnet.warpnet-android.notification.InAppNotification
import com.warpnet.warpnet-android.preferences.PreferencesHolder
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.utils.PlatformResolver
import com.warpnet.warpnet-android.worker.DownloadMediaWorker
import com.warpnet.warpnet-android.worker.NotificationWorker
import com.warpnet.warpnet-android.worker.ShareMediaWorker
import com.warpnet.warpnet-android.worker.compose.MastodonComposeWorker
import com.warpnet.warpnet-android.worker.compose.WarpnetComposeWorker
import com.warpnet.warpnet-android.worker.database.DeleteDbStatusWorker
import com.warpnet.warpnet-android.worker.dm.DirectMessageDeleteWorker
import com.warpnet.warpnet-android.worker.dm.DirectMessageFetchWorker
import com.warpnet.warpnet-android.worker.dm.WarpnetDirectMessageSendWorker
import com.warpnet.warpnet-android.worker.draft.RemoveDraftWorker
import com.warpnet.warpnet-android.worker.draft.SaveDraftWorker
import com.warpnet.warpnet-android.worker.status.DeleteStatusWorker
import com.warpnet.warpnet-android.worker.status.LikeWorker
import com.warpnet.warpnet-android.worker.status.MastodonVoteWorker
import com.warpnet.warpnet-android.worker.status.RetweetWorker
import com.warpnet.warpnet-android.worker.status.UnLikeWorker
import com.warpnet.warpnet-android.worker.status.UnRetweetWorker
import com.warpnet.warpnet-android.worker.status.UpdateStatusWorker
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule = module {
  single {
    ResLoader(get())
  }
  single { AccountRepository(get(), get()) }
  single { AccountPreferencesFactory(get()) }
  single<AccountManager> { AccountManager.get(get()) }
  single { get<Context>().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
  single { get<Context>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
  single { get<Context>().contentResolver }
  single { NotificationManagerCompat.from(get()) }
  single { WorkManager.getInstance(get()) }
  single { WarpnetHttpConfigProvider(get<PreferencesHolder>().miscPreferences) }
  single { InAppNotification() }
  single { PlatformResolver(get()) }
  workManager()
}

private fun Module.workManager() {
  worker { ShareMediaWorker(get(), get(), get()) }
  worker { NotificationWorker(get(), get(), get<PreferencesHolder>().notificationPreferences, get()) }
  worker { DownloadMediaWorker(get(), get(), get()) }
  worker { DeleteStatusWorker(get(), get(), get()) }
  worker { LikeWorker(get(), get(), get()) }
  worker { MastodonVoteWorker(get(), get(), get()) }
  worker { RetweetWorker(get(), get(), get()) }
  worker { UnLikeWorker(get(), get(), get()) }
  worker { UnRetweetWorker(get(), get(), get()) }
  worker { UpdateStatusWorker(get(), get(), get()) }
  worker { RemoveDraftWorker(get(), get(), get()) }
  worker { SaveDraftWorker(get(), get(), get()) }
  worker { DirectMessageDeleteWorker(get(), get(), get()) }
  worker { DirectMessageFetchWorker(get(), get(), get()) }
  worker { WarpnetDirectMessageSendWorker(get(), get(), get()) }
  worker { DeleteDbStatusWorker(get(), get(), get()) }
  worker { MastodonComposeWorker(get(), get(), get()) }
  worker { WarpnetComposeWorker(get(), get(), get()) }
}

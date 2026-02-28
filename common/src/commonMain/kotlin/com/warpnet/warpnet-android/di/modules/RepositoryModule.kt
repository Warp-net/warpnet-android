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

import com.warpnet.warpnet-android.preferences.PreferencesHolder
import com.warpnet.warpnet-android.repository.CacheRepository
import com.warpnet.warpnet-android.repository.DirectMessageRepository
import com.warpnet.warpnet-android.repository.DraftRepository
import com.warpnet.warpnet-android.repository.GifRepository
import com.warpnet.warpnet-android.repository.ListsRepository
import com.warpnet.warpnet-android.repository.ListsUsersRepository
import com.warpnet.warpnet-android.repository.MediaRepository
import com.warpnet.warpnet-android.repository.NitterRepository
import com.warpnet.warpnet-android.repository.NotificationRepository
import com.warpnet.warpnet-android.repository.ReactionRepository
import com.warpnet.warpnet-android.repository.SearchRepository
import com.warpnet.warpnet-android.repository.StatusRepository
import com.warpnet.warpnet-android.repository.TimelineRepository
import com.warpnet.warpnet-android.repository.TrendRepository
import com.warpnet.warpnet-android.repository.UserListRepository
import com.warpnet.warpnet-android.repository.UserRepository
import com.warpnet.warpnet-android.utils.ITranslationRepo
import com.warpnet.warpnet-android.utils.TranslationRepo
import org.koin.dsl.module

val repositoryModule = module {
  single { CacheRepository(get(), get(), get()) }
  single { DirectMessageRepository(get()) }
  single { DraftRepository(get()) }
  single { ListsRepository(get()) }
  single { ListsUsersRepository() }
  single { MediaRepository(get()) }
  single { NotificationRepository(get()) }
  single { ReactionRepository(get()) }
  single { SearchRepository(get(), get()) }
  single { StatusRepository(get(), get<PreferencesHolder>().miscPreferences) }
  single { TimelineRepository(get()) }
  single { TrendRepository(get()) }
  single { UserListRepository() }
  single { UserRepository(get(), get()) }
  single { GifRepository(get()) }
  single { NitterRepository() }
  single<ITranslationRepo> { TranslationRepo() }
}

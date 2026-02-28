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

import com.warpnet.warpnetandroid.preferences.PreferencesHolder
import com.warpnet.warpnetandroid.repository.CacheRepository
import com.warpnet.warpnetandroid.repository.DirectMessageRepository
import com.warpnet.warpnetandroid.repository.DraftRepository
import com.warpnet.warpnetandroid.repository.GifRepository
import com.warpnet.warpnetandroid.repository.ListsRepository
import com.warpnet.warpnetandroid.repository.ListsUsersRepository
import com.warpnet.warpnetandroid.repository.MediaRepository
import com.warpnet.warpnetandroid.repository.NitterRepository
import com.warpnet.warpnetandroid.repository.NotificationRepository
import com.warpnet.warpnetandroid.repository.ReactionRepository
import com.warpnet.warpnetandroid.repository.SearchRepository
import com.warpnet.warpnetandroid.repository.StatusRepository
import com.warpnet.warpnetandroid.repository.TimelineRepository
import com.warpnet.warpnetandroid.repository.TrendRepository
import com.warpnet.warpnetandroid.repository.UserListRepository
import com.warpnet.warpnetandroid.repository.UserRepository
import com.warpnet.warpnetandroid.utils.ITranslationRepo
import com.warpnet.warpnetandroid.utils.TranslationRepo
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

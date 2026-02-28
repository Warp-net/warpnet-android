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

import com.warpnet.warpnet-android.action.ComposeAction
import com.warpnet.warpnet-android.action.DirectMessageAction
import com.warpnet.warpnet-android.action.DraftAction
import com.warpnet.warpnet-android.action.MediaAction
import com.warpnet.warpnet-android.action.StatusActions
import org.koin.dsl.module

actual val actionModule = module {
  single { ComposeAction(get(), get()) }
  single { DirectMessageAction(get()) }
  single { DraftAction(get(), get()) }
  single { MediaAction(get(), get(), get()) }
  single { StatusActions(get()) }
}

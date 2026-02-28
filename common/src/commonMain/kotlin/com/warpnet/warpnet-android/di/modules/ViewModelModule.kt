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

import com.warpnet.warpnet-android.extensions.viewModel
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.viewmodel.ActiveAccountViewModel
import com.warpnet.warpnet-android.viewmodel.DraftViewModel
import com.warpnet.warpnet-android.viewmodel.MediaViewModel
import com.warpnet.warpnet-android.viewmodel.PureMediaViewModel
import com.warpnet.warpnet-android.viewmodel.StatusViewModel
import com.warpnet.warpnet-android.viewmodel.compose.ComposeSearchUserViewModel
import com.warpnet.warpnet-android.viewmodel.compose.MastodonComposeSearchHashtagViewModel
import com.warpnet.warpnet-android.viewmodel.dm.DMConversationViewModel
import com.warpnet.warpnet-android.viewmodel.dm.DMEventViewModel
import com.warpnet.warpnet-android.viewmodel.dm.DMNewConversationViewModel
import com.warpnet.warpnet-android.viewmodel.gif.GifViewModel
import com.warpnet.warpnet-android.viewmodel.lists.ListsAddMemberViewModel
import com.warpnet.warpnet-android.viewmodel.lists.ListsCreateViewModel
import com.warpnet.warpnet-android.viewmodel.lists.ListsModifyViewModel
import com.warpnet.warpnet-android.viewmodel.lists.ListsSearchUserViewModel
import com.warpnet.warpnet-android.viewmodel.lists.ListsTimelineViewModel
import com.warpnet.warpnet-android.viewmodel.lists.ListsViewModel
import com.warpnet.warpnet-android.viewmodel.mastodon.MastodonHashtagViewModel
import com.warpnet.warpnet-android.viewmodel.mastodon.MastodonSignInViewModel
import com.warpnet.warpnet-android.viewmodel.warpnet.WarpnetSignInViewModel
import com.warpnet.warpnet-android.viewmodel.warpnet.user.WarpnetUserViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule = module {
  viewModel { (statusKey: MicroBlogKey) -> StatusViewModel(get(), get(), statusKey) }
  viewModel { (belongKey: MicroBlogKey) -> PureMediaViewModel(get(), belongKey) }
  viewModel { (statusKey: MicroBlogKey) -> MediaViewModel(get(), get(), get(), statusKey) }
  viewModel { DraftViewModel(get(), get()) }
  viewModel { ActiveAccountViewModel(get()) }

  warpnet()
  mastodon()
  lists()
  dm()
  compose()
  gif()
}

private fun Module.compose() {
  viewModel { MastodonComposeSearchHashtagViewModel(get()) }
  viewModel { ComposeSearchUserViewModel(get()) }
}

private fun Module.dm() {
  viewModel { DMConversationViewModel(get(), get()) }
  viewModel { (conversationKey: MicroBlogKey) ->
    DMEventViewModel(
      get(),
      get(),
      get(),
      conversationKey
    )
  }
  viewModel { DMNewConversationViewModel(get(), get()) }
}

private fun Module.lists() {
  viewModel { (following: Boolean) -> ListsSearchUserViewModel(get(), following) }
  viewModel { (listKey: MicroBlogKey) -> ListsTimelineViewModel(get(), get(), listKey) }
  viewModel { (listId: String) -> ListsAddMemberViewModel(get(), get(), get(), listId) }
  viewModel { ListsViewModel(get(), get()) }
  viewModel {
    ListsCreateViewModel(
      get(),
      get(),
      get(),
    )
  }
  viewModel { (listKey: MicroBlogKey) -> ListsModifyViewModel(get(), get(), get(), listKey) }
}

private fun Module.mastodon() {
  viewModel { (keyword: String) -> MastodonHashtagViewModel(get(), get(), keyword) }
  viewModel { MastodonSignInViewModel(get(), get(), get()) }
}

private fun Module.warpnet() {
  viewModel { (
    consumerKey: String,
    consumerSecret: String,
    pinCodeProvider: suspend (url: String) -> String?,
    onResult: (success: Boolean) -> Unit,
  ) ->
    WarpnetSignInViewModel(
      get(),
      get(),
      consumerKey,
      consumerSecret,
      get(),
      pinCodeProvider,
      onResult,
    )
  }
  viewModel { (screenName: String) -> WarpnetUserViewModel(get(), get(), get(), screenName) }
}

private fun Module.gif() {
  viewModel { GifViewModel(get(), get(), get()) }
}

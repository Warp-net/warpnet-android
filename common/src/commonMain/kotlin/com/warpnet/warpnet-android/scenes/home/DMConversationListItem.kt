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
package com.warpnet.warpnet-android.scenes.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.model.HomeNavigationItem
import com.warpnet.warpnet-android.navigation.Root
import com.warpnet.warpnet-android.scenes.dm.DMConversationListSceneContent
import com.warpnet.warpnet-android.scenes.dm.DMConversationListSceneFab
import moe.tlaster.precompose.navigation.Navigator

class DMConversationListItem : HomeNavigationItem() {
  @Composable
  override fun name(): String {
    return stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_messages_title)
  }

  override val route: String
    get() = Root.Messages.Home

  @Composable
  override fun icon(): Painter {
    return painterResource(res = com.warpnet.warpnet-android.MR.files.ic_mail)
  }

  @Composable
  override fun Fab(navigator: Navigator) {
    DMConversationListSceneFab(navigator)
  }

  @Composable
  override fun Content(navigator: Navigator) {
    DMConversationListSceneContent(
      navigator,
      lazyListController,
    )
  }
}

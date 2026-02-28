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
package com.warpnet.warpnetandroid.scenes.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.model.HomeNavigationItem
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.scenes.DraftListSceneContent
import moe.tlaster.precompose.navigation.Navigator

class DraftNavigationItem : HomeNavigationItem() {
  @Composable
  override fun name(): String {
    return stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_drafts_title)
  }

  override val route: String
    get() = Root.Draft.List

  @Composable
  override fun icon(): Painter {
    return painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_note)
  }

  @Composable
  override fun Content(navigator: Navigator) {
    DraftListSceneContent(
      lazyListController = lazyListController,
      navigator = navigator,
    )
  }
}

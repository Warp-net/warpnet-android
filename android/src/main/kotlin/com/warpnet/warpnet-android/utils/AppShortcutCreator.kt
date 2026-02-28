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
package com.warpnet.warpnet-android.utils

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import com.warpnet.warpnet-android.R
import com.warpnet.warpnet-android.WarpnetAndroidActivity
import com.warpnet.warpnet-android.kmp.IAppShortcutCreator

class AppShortcutCreator constructor(private val context: Context) : IAppShortcutCreator {
  override fun configureAppShortcuts() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
      val shortcutList = mutableListOf<ShortcutInfo>()
      shortcutList.add(buildCreateCompose(context))
      val shortcutManager = context.getSystemService(ShortcutManager::class.java)
      runCatching {
        shortcutManager.dynamicShortcuts = shortcutList
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.N_MR1)
  private fun buildCreateCompose(context: Context): ShortcutInfo {
    return ShortcutInfoCompat.Builder(context, SHORTCUT_ID_CREATE_MESSAGE)
      .setShortLabel(context.getString(R.string.scene_compose_title_compose))
      .setIcon(IconCompat.createWithResource(context, R.drawable.ic_feather_blue))
      .setIntent(
        Intent(
          context,
          WarpnetAndroidActivity::class.java,
        ).apply {
          action = Intent.ACTION_VIEW
          data = Uri.parse(CREATE_COMPOSE_LINK)
        }
      )
      .build().toShortcutInfo()
  }

  companion object {
    private const val SHORTCUT_ID_CREATE_MESSAGE = "CREATE_MESSAGE"
    private const val CREATE_COMPOSE_LINK = "warpnet-android://RootDeepLinks/Compose"
  }
}

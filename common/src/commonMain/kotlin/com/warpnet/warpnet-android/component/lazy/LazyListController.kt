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
package com.warpnet.warpnet-android.component.lazy

import androidx.compose.foundation.lazy.LazyListState
import com.warpnet.warpnet-android.preferences.model.AppearancePreferences
import kotlinx.coroutines.delay

class LazyListController {
  var listState: LazyListState? = null

  companion object {
    const val SMOOTH_THRESHOLD = 5
  }

  private var singleTaped = false

  suspend fun scrollToTop(tabToTop: AppearancePreferences.TabToTop) {
    when (tabToTop) {
      AppearancePreferences.TabToTop.SingleTap -> {
        scrollToTop()
      }
      AppearancePreferences.TabToTop.DoubleTap -> {
        if (singleTaped) {
          scrollToTop()
        } else {
          singleTaped = true
          delay(200)
          singleTaped = false
        }
      }
    }
  }

  private suspend fun scrollToTop() {
    listState?.run {
      if (firstVisibleItemIndex > SMOOTH_THRESHOLD) {
        scrollToItem(SMOOTH_THRESHOLD)
      }
      animateScrollToItem(0)
    }
  }
}

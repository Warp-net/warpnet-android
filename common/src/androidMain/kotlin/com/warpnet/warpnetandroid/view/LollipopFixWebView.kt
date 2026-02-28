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
package com.warpnet.warpnetandroid.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView

private fun Context.fixForLollipop(): Context {
  return if (Build.VERSION.SDK_INT in 21..22) {
    applicationContext
  } else this
}

class LollipopFixWebView : WebView {

  constructor(context: Context) : super(context.fixForLollipop())
  constructor(context: Context, attrs: AttributeSet?) : super(context.fixForLollipop(), attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context.fixForLollipop(),
    attrs,
    defStyleAttr
  )

  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
  ) : super(context.fixForLollipop(), attrs, defStyleAttr, defStyleRes)

  @Suppress("DEPRECATION")
  constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    privateBrowsing: Boolean
  ) : super(context.fixForLollipop(), attrs, defStyleAttr, privateBrowsing)

  init {
    isFocusable = true
    isFocusableInTouchMode = true
  }
}

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
package com.warpnet.warpnet-android.utils.video

import android.content.Context
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.cache.CacheDataSink
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.warpnet.warpnet-android.BuildConfig

class CacheDataSourceFactory(
  private val context: Context,
  private val maxFileSize: Long,
) : DataSource.Factory {
  private val simpleCache: SimpleCache by lazy {
    VideoCache.getInstance(context)
  }

  private val defaultDatasourceFactory: DefaultDataSource.Factory
  override fun createDataSource(): DataSource {
    return CacheDataSource(
      simpleCache,
      defaultDatasourceFactory.createDataSource(),
      FileDataSource(),
      CacheDataSink(simpleCache, maxFileSize),
      CacheDataSource.FLAG_BLOCK_ON_CACHE or CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
      null
    )
  }

  init {
    val userAgent = Util.getUserAgent(
      context,
      BuildConfig.APPLICATION_NAME
    )
    val bandwidthMeter = DefaultBandwidthMeter.Builder(context).build()
    defaultDatasourceFactory = DefaultDataSource.Factory(
      this.context,
      DefaultHttpDataSource.Factory()
        .setUserAgent(userAgent)
        .setTransferListener(bandwidthMeter)
    ).setTransferListener(bandwidthMeter)
  }
}

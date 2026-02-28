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
package com.warpnet.warpnet-android.jobs.status

import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.repository.ReactionRepository
import com.warpnet.warpnet-android.repository.StatusRepository

class UpdateStatusJob(
  private val repository: ReactionRepository,
  private val statusRepository: StatusRepository,
) {
  suspend fun execute(
    accountKey: MicroBlogKey,
    statusKey: MicroBlogKey,
    liked: Boolean? = null,
    likeCount: Long? = null,
    retweeted: Boolean? = null,
    retweetCount: Long? = null,
  ) {
    repository.updateReaction(
      accountKey = accountKey,
      statusKey = statusKey,
      liked = liked,
      retweet = retweeted
    )
    statusRepository.updateStatus(statusKey = statusKey, accountKey = accountKey) {
      it.copy(
        metrics = it.metrics.copy(
          retweet = retweetCount ?: it.metrics.retweet,
          like = likeCount ?: it.metrics.like
        )
      )
    }
  }
}

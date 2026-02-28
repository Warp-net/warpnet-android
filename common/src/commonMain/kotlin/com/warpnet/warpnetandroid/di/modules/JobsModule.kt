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

import com.warpnet.warpnetandroid.jobs.common.DownloadMediaJob
import com.warpnet.warpnetandroid.jobs.common.NotificationJob
import com.warpnet.warpnetandroid.jobs.common.ShareMediaJob
import com.warpnet.warpnetandroid.jobs.compose.MastodonComposeJob
import com.warpnet.warpnetandroid.jobs.compose.WarpnetComposeJob
import com.warpnet.warpnetandroid.jobs.database.DeleteDbStatusJob
import com.warpnet.warpnetandroid.jobs.dm.DirectMessageDeleteJob
import com.warpnet.warpnetandroid.jobs.dm.DirectMessageFetchJob
import com.warpnet.warpnetandroid.jobs.dm.WarpnetDirectMessageSendJob
import com.warpnet.warpnetandroid.jobs.draft.RemoveDraftJob
import com.warpnet.warpnetandroid.jobs.draft.SaveDraftJob
import com.warpnet.warpnetandroid.jobs.status.DeleteStatusJob
import com.warpnet.warpnetandroid.jobs.status.LikeStatusJob
import com.warpnet.warpnetandroid.jobs.status.MastodonVoteJob
import com.warpnet.warpnetandroid.jobs.status.RetweetStatusJob
import com.warpnet.warpnetandroid.jobs.status.UnRetweetStatusJob
import com.warpnet.warpnetandroid.jobs.status.UnlikeStatusJob
import com.warpnet.warpnetandroid.jobs.status.UpdateStatusJob
import org.koin.core.module.Module
import org.koin.dsl.module

val jobsModule = module {
  common()
  compose()
  database()
  dm()
  draft()
  status()
}

private fun Module.status() {
  single { DeleteStatusJob(get(), get(), get()) }
  single { LikeStatusJob(get(), get(), get()) }
  single { MastodonVoteJob(get(), get(), get()) }
  single { RetweetStatusJob(get(), get(), get()) }
  single { UnlikeStatusJob(get(), get(), get()) }
  single { UnRetweetStatusJob(get(), get(), get()) }
  single { UpdateStatusJob(get(), get()) }
}

private fun Module.draft() {
  single { RemoveDraftJob(get()) }
  single { SaveDraftJob(get(), get()) }
}

private fun Module.dm() {
  single { DirectMessageDeleteJob(get(), get()) }
  single { DirectMessageFetchJob(get(), get(), get(), get()) }
  single { WarpnetDirectMessageSendJob(get(), get(), get(), get(), get()) }
}

private fun Module.database() {
  single { DeleteDbStatusJob(get()) }
}

private fun Module.compose() {
  single { MastodonComposeJob(get(), get(), get(), get(), get(), get(), get()) }
  single { WarpnetComposeJob(get(), get(), get(), get(), get(), get(), get(), get()) }
}

private fun Module.common() {
  single { DownloadMediaJob(get(), get(), get(), get()) }
  single { NotificationJob(get(), get(), get(), get()) }
  single { ShareMediaJob(get(), get()) }
}

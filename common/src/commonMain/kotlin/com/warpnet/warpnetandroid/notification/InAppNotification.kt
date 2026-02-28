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
package com.warpnet.warpnetandroid.notification

import androidx.compose.runtime.Composable
import com.warpnet.warpnetandroid.compose.LocalResLoader
import com.warpnet.warpnetandroid.extensions.observeAsState
import com.warpnet.warpnetandroid.kmp.RemoteNavigator
import com.warpnet.warpnetandroid.utils.Event
import dev.icerock.moko.resources.StringResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

interface NotificationEvent {
  @Composable
  fun getMessage(): String
}
data class EventActionContext(
  val remoteNavigator: RemoteNavigator
)
interface NotificationWithActionEvent : NotificationEvent {
  @Composable
  fun getActionMessage(): String
  val action: EventActionContext.() -> Unit
}

class StringNotificationEvent(
  private val message: String,
) : NotificationEvent {
  @Composable
  override fun getMessage(): String {
    return message
  }

  companion object {
    fun InAppNotification.show(message: String) {
      show(StringNotificationEvent(message = message))
    }
  }
}

open class StringResNotificationEvent(
  val message: StringResource,
) : NotificationEvent {
  @Composable
  override fun getMessage(): String {
    return LocalResLoader.current.getString(message)
  }
}

class StringResWithActionNotificationEvent(
  private vararg val message: StringResource,
  private val separator: String = System.lineSeparator(),
  private val actionStr: StringResource,
  override val action: EventActionContext.() -> Unit,
) : NotificationWithActionEvent {
  @Composable
  override fun getActionMessage(): String {
    return LocalResLoader.current.getString(actionStr)
  }

  @Composable
  override fun getMessage(): String {
    return message.map { LocalResLoader.current.getString(it) }.joinToString(separator)
  }
}

class InAppNotification {
  private val _source = MutableStateFlow<Event<NotificationEvent?>?>(null)
  val source
    get() = _source.asSharedFlow()

  fun show(event: NotificationEvent) {
    _source.value = ((Event(event)))
  }

  @Composable
  fun observeAsState(initial: Event<NotificationEvent?>? = null) =
    source.observeAsState(initial = initial)
}

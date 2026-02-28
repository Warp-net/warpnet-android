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
package com.warpnet.warpnetandroid.scenes.settings.display

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.action.FakeStatusActions
import com.warpnet.warpnetandroid.action.LocalStatusActions
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.lazy.ItemDivider
import com.warpnet.warpnetandroid.component.lazy.ItemHeader
import com.warpnet.warpnetandroid.component.settings.RadioItem
import com.warpnet.warpnetandroid.component.settings.switchItem
import com.warpnet.warpnetandroid.component.status.TimelineStatusComponent
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.rememberPresenterState
import com.warpnet.warpnetandroid.kmp.Platform
import com.warpnet.warpnetandroid.kmp.currentPlatform
import com.warpnet.warpnetandroid.model.ui.UiStatus
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.navigation.StatusNavigationData
import com.warpnet.warpnetandroid.preferences.model.DisplayPreferences
import com.warpnet.warpnetandroid.ui.WarpnetScene
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Settings.Display,
)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DisplayScene(
  navigator: Navigator,
) {
  val (state, channel) = rememberPresenterState { DisplayPresenter(it) }
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          navigationIcon = {
            AppBarNavigationButton(
              onBack = {
                navigator.popBackStack()
              }
            )
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_title))
          }
        )
      }
    ) {
      Column(
        modifier = Modifier
          .verticalScroll(
            rememberScrollState()
          )
      ) {
        ItemHeader {
          Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_section_header_preview))
        }
        CompositionLocalProvider(
          LocalStatusActions provides FakeStatusActions,
        ) {
          val statusNavigation = remember {
            StatusNavigationData()
          }
          TimelineStatusComponent(
            data = UiStatus.sample(),
            statusNavigation = statusNavigation,
          )
        }
        ItemDivider()
        ItemHeader() {
          Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_section_header_text))
        }
        switchItem(
          value = state.display.useSystemFontSize,
          onChanged = {
            channel.trySend(DisplayEvent.SetUseSystemFontSize(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_text_use_the_system_font_size))
          },
        )
        if (!state.display.useSystemFontSize) {
          ListItem(
            icon = {
              Icon(
                modifier = Modifier.size(12.dp),
                imageVector = Icons.Default.TextFields,
                contentDescription = stringResource(res = com.warpnet.warpnetandroid.MR.strings.accessibility_scene_settings_display_font_size)
              )
            },
            text = {
              Slider(
                steps = ((1.4f - 0.8f) * 10).toInt(),
                value = state.fontScale,
                onValueChange = {
                  channel.trySend(DisplayEvent.SetFontScale(it))
                },
                valueRange = 0.8f..1.4f,
                onValueChangeFinished = {
                  channel.trySend(DisplayEvent.CommitFontScale)
                }
              )
            },
            trailing = {
              Icon(
                imageVector = Icons.Default.TextFields,
                contentDescription = stringResource(res = com.warpnet.warpnetandroid.MR.strings.accessibility_scene_settings_display_font_size)
              )
            }
          )
        }
        ItemDivider()
        RadioItem(
          options = remember {
            listOf(
              DisplayPreferences.AvatarStyle.Round,
              DisplayPreferences.AvatarStyle.Square,
            )
          },
          value = state.display.avatarStyle,
          onChanged = {
            channel.trySend(DisplayEvent.SetAvatarStyle(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_text_avatar_style))
          },
          itemContent = {
            Text(
              text = stringResource(
                remember {
                  arrayOf(
                    com.warpnet.warpnetandroid.MR.strings.scene_settings_display_text_circle,
                    com.warpnet.warpnetandroid.MR.strings.scene_settings_display_text_rounded_square,
                  )
                }[it.ordinal]
              )
            )
          }
        )
        ItemDivider()
        RadioItem(
          options = remember {
            listOf(
              DisplayPreferences.DateFormat.RELATIVE,
              DisplayPreferences.DateFormat.ABSOLUTE,
            )
          },
          value = state.display.dateFormat,
          onChanged = {
            channel.trySend(DisplayEvent.SetDateFormat(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_section_header_date_format))
          },
          itemContent = {
            Text(
              text = stringResource(
                remember {
                  arrayOf(
                    com.warpnet.warpnetandroid.MR.strings.scene_settings_display_date_format_relative,
                    com.warpnet.warpnetandroid.MR.strings.scene_settings_display_date_format_absolute,
                  )
                }[it.ordinal]
              )
            )
          }
        )
        ItemDivider()
        ItemHeader {
          Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_section_header_media))
        }
        switchItem(
          value = state.display.urlPreview,
          onChanged = {
            channel.trySend(DisplayEvent.SetUrlPreview(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_url_preview))
          }
        )
        switchItem(
          value = state.display.mediaPreview,
          onChanged = {
            channel.trySend(DisplayEvent.SetMediaPreview(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_media_media_previews))
          }
        )
        switchItem(
          value = state.display.muteByDefault,
          onChanged = {
            channel.trySend(DisplayEvent.SetMuteByDefault(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_media_mute_by_default))
          }
        )
        if (state.display.mediaPreview && currentPlatform != Platform.JVM) {
          RadioItem(
            options = remember {
              listOf(
                DisplayPreferences.AutoPlayback.Auto,
                DisplayPreferences.AutoPlayback.Always,
                DisplayPreferences.AutoPlayback.Off,
              )
            },
            value = state.display.autoPlayback,
            onChanged = {
              channel.trySend(DisplayEvent.SetAutoPlayback(it))
            },
            title = {
              Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_media_auto_playback))
            },
            itemContent = {
              Text(
                text = stringResource(
                  remember {
                    arrayOf(
                      com.warpnet.warpnetandroid.MR.strings.scene_settings_display_media_automatic,
                      com.warpnet.warpnetandroid.MR.strings.scene_settings_display_media_always,
                      com.warpnet.warpnetandroid.MR.strings.scene_settings_display_media_off,
                    )
                  }[it.ordinal]
                )
              )
            }
          )
        }
        ItemDivider()
        ItemHeader {
          Text(text = stringResource(com.warpnet.warpnetandroid.MR.strings.scene_settings_display_section_header_toolbar_icons))
        }

        switchItem(
          value = state.display.hideToolbarIcons,
          onChanged = {
            channel.trySend(DisplayEvent.SetToolbarIcons(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_toolbar_icons_hide_toolbar_icons))
          }
        )

        switchItem(
          value = state.display.showStatusNumbers,
          onChanged = {
            channel.trySend(DisplayEvent.SetStatusNumbers(it))
          },
          title = {
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.scene_settings_display_toolbar_icons_show_status_numbers))
          }
        )

        ItemDivider()
        ItemHeader {
          Text(text = stringResource(com.warpnet.warpnetandroid.MR.strings.scene_settings_appearance_section_header_translation))
        }
        switchItem(
          value = state.display.showTranslationButton,
          onChanged = {
            channel.trySend(DisplayEvent.ShowTranslationButton(it))
          },
          title = {
            Text(text = stringResource(com.warpnet.warpnetandroid.MR.strings.scene_settings_appearance_translation_translate_button))
          }
        )
      }
    }
  }
}

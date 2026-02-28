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
package com.warpnet.warpnetandroid.component.media

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.MR
import com.warpnet.warpnetandroid.component.foundation.DropdownMenu
import com.warpnet.warpnetandroid.component.foundation.DropdownMenuItem
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.get
import com.warpnet.warpnetandroid.kmp.MediaInsertProvider
import com.warpnet.warpnetandroid.kmp.Platform
import com.warpnet.warpnetandroid.kmp.PlatformMediaWrapper
import com.warpnet.warpnetandroid.kmp.currentPlatform
import com.warpnet.warpnetandroid.model.enums.MediaInsertType
import com.warpnet.warpnetandroid.model.ui.UiMediaInsert
import com.warpnet.warpnetandroid.navigation.Root
import kotlinx.coroutines.launch
import moe.tlaster.kfilepicker.FilePicker

enum class MediaLibraryType(
  val extensions: List<String>,
) {
  Video(listOf("mp4")),
  Image(listOf("jpg", "png", "jpeg")),
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaInsertMenu(
  onResult: (List<UiMediaInsert>) -> Unit,
  navigateForResult: suspend (String) -> Any?,
  modifier: Modifier = Modifier,
  supportMultipleSelect: Boolean = true,
  librariesSupported: Array<MediaLibraryType> = MediaLibraryType.values(),
  disableList: List<MediaInsertType> = emptyList(),
) {
  val mediaInsertProvider = get<MediaInsertProvider>()
  val scope = rememberCoroutineScope()

  var showDropdown by remember {
    mutableStateOf(false)
  }

  PlatformMediaWrapper(
    scope,
    onResult = onResult
  ) { launchCamera, launchVideo ->
    Box(modifier) {
      DropdownMenu(expanded = showDropdown, onDismissRequest = { showDropdown = false }) {
        MediaInsertType.values().forEach loop@{
          if ((it == MediaInsertType.CAMERA || it == MediaInsertType.RECORD_VIDEO) && currentPlatform != Platform.Android) {
            return@loop
          }
          val enabled = !disableList.contains(it)
          DropdownMenuItem(
            onClick = {
              when (it) {
                MediaInsertType.CAMERA -> {
                  launchCamera.invoke()
                }
                MediaInsertType.RECORD_VIDEO -> {
                  launchVideo.invoke()
                }
                MediaInsertType.LIBRARY -> {
                  scope.launch {
                    onResult.invoke(
                      FilePicker.pickFiles(
                        allowMultiple = supportMultipleSelect,
                        allowedExtensions = librariesSupported.flatMap { it.extensions }
                      ).map {
                        mediaInsertProvider.provideUiMediaInsert(it.path)
                      }
                    )
                  }
                }
                MediaInsertType.GIF -> scope.launch {
                  navigateForResult(Root.Gif.Home)
                    ?.let { result ->
                      onResult(
                        listOf(result as String).map {
                          mediaInsertProvider.provideUiMediaInsert(
                            it
                          )
                        }
                      )
                    }
                }
              }
              showDropdown = false
            },
            enabled = enabled
          ) {
            ListItem(
              text = {
                Text(text = it.stringName())
              },
              icon = {
                Icon(
                  painter = it.icon(),
                  contentDescription = it.stringName(),
                  tint = if (enabled) MaterialTheme.colors.primary else LocalContentColor.current.copy(
                    alpha = LocalContentAlpha.current
                  ),
                  modifier = Modifier.size(24.dp),
                )
              }
            )
          }
        }
      }
    }
  }
  IconButton(
    onClick = {
      showDropdown = !showDropdown
    }
  ) {
    Icon(
      painter = painterResource(res = MR.files.ic_photo),
      contentDescription = stringResource(
        res = MR.strings.accessibility_scene_compose_image
      ),
      modifier = Modifier.size(24.dp),
    )
  }
}

@Composable
private fun MediaInsertType.stringName() = when (this) {
  MediaInsertType.CAMERA -> stringResource(res = MR.strings.accessibility_scene_compose_media_insert_camera)
  MediaInsertType.RECORD_VIDEO -> stringResource(res = MR.strings.accessibility_scene_compose_media_insert_record_video)
  MediaInsertType.LIBRARY -> stringResource(res = MR.strings.accessibility_scene_compose_media_insert_library)
  MediaInsertType.GIF -> stringResource(res = MR.strings.accessibility_scene_compose_media_insert_gif)
}

@Composable
private fun MediaInsertType.icon() = when (this) {
  MediaInsertType.CAMERA -> painterResource(res = MR.files.ic_camera)
  MediaInsertType.RECORD_VIDEO -> painterResource(res = MR.files.ic_video)
  MediaInsertType.LIBRARY -> painterResource(res = MR.files.ic_photo)
  MediaInsertType.GIF -> painterResource(res = MR.files.ic_gif)
}

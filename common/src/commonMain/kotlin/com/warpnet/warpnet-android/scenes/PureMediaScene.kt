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
package com.warpnet.warpnet-android.scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.warpnet.warpnet-android.component.bottomInsetsPadding
import com.warpnet.warpnet-android.component.foundation.VideoPlayerState
import com.warpnet.warpnet-android.component.foundation.rememberPagerState
import com.warpnet.warpnet-android.component.painterResource
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.component.topInsetsPadding
import com.warpnet.warpnet-android.di.ext.getViewModel
import com.warpnet.warpnet-android.extensions.observeAsState
import com.warpnet.warpnet-android.kmp.LocalPlatformWindow
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.preferences.LocalDisplayPreferences
import com.warpnet.warpnet-android.preferences.model.DisplayPreferences
import com.warpnet.warpnet-android.ui.LocalVideoPlayback
import com.warpnet.warpnet-android.ui.WarpnetDialog
import com.warpnet.warpnet-android.utils.video.CustomVideoControl
import com.warpnet.warpnet-android.viewmodel.PureMediaViewModel
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.swiper.SwiperState
import moe.tlaster.swiper.rememberSwiperState
import org.koin.core.parameter.parametersOf

@Composable
fun PureMediaScene(
  belongToKey: String,
  selectedIndex: Int,
  navigator: Navigator,
) {
  MicroBlogKey.valueOf(belongToKey).let {
    PureMediaScene(
      belongToKey = it,
      selectedIndex = selectedIndex,
      navigator = navigator
    )
  }
}

@Composable
private fun PureMediaScene(
  belongToKey: MicroBlogKey,
  selectedIndex: Int,
  navigator: Navigator,
) {
  val viewModel = getViewModel<PureMediaViewModel> {
    parametersOf(belongToKey)
  }
  val source by viewModel.source.observeAsState(null)
  WarpnetDialog(
    requireDarkTheme = true,
    extendViewIntoStatusBar = true,
    extendViewIntoNavigationBar = true,
  ) {
    source?.let { medias ->
      CompositionLocalProvider(
        LocalVideoPlayback provides DisplayPreferences.AutoPlayback.Always
      ) {
        val window = LocalPlatformWindow.current
        var controlVisibility by remember { mutableStateOf(true) }
        val controlPanelColor = MaterialTheme.colors.surface.copy(alpha = 0.6f)
        val pagerState = rememberPagerState(
          initialPage = selectedIndex,
          pageCount = medias.size,
        )
        val videoPlayerState = remember { mutableStateOf<VideoPlayerState?>(null) }
        val swiperState = rememberSwiperState(
          onDismiss = {
            navigator.popBackStack()
          },
        )
        val display = LocalDisplayPreferences.current
        val isMute by remember {
          mutableStateOf(display.muteByDefault)
        }

        StatusMediaSceneLayout(
          backgroundColor = Color.Transparent,
          contentColor = contentColorFor(backgroundColor = MaterialTheme.colors.background),
          bottomView = {
            PureMediaBottomInfo(
              controlVisibility = controlVisibility,
              swiperState = swiperState,
              controlPanelColor = controlPanelColor,
              videoPlayerState = videoPlayerState.value,
            )
          },
          closeButton = {
            PureMediaControlPanel(
              controlVisibility = controlVisibility,
              swiperState = swiperState,
              controlPanelColor = controlPanelColor,
              onPopBack = {
                navigator.popBackStack()
              }
            )
          },
          mediaView = {
            MediaView(
              media = medias.mapNotNull {
                it.mediaUrl?.let { it1 ->
                  MediaData(
                    it1,
                    it.type
                  )
                }
              },
              swiperState = swiperState,
              onVideoPlayerStateSet = { videoPlayerState.value = it },
              pagerState = pagerState,
              volume = if (isMute) 0f else 1f,
              onClick = {
                if (controlVisibility) {
                  window.hideControls()
                } else {
                  window.showControls()
                }
              },
              backgroundColor = MaterialTheme.colors.background
            )
            val windowBarVisibility by window.windowBarVisibility.observeAsState(true)
            LaunchedEffect(windowBarVisibility) {
              controlVisibility = windowBarVisibility
            }
            DisposableEffect(Unit) {
              onDispose {
                window.showControls()
              }
            }
          },
          backgroundView = {
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background.copy(alpha = 1f - swiperState.progress)),
            )
          }
        )
      }
    }
  }
}

@Composable
fun PureMediaBottomInfo(
  controlVisibility: Boolean,
  swiperState: SwiperState,
  controlPanelColor: Color,
  videoPlayerState: VideoPlayerState?,
) {
  AnimatedVisibility(
    visible = controlVisibility && swiperState.progress == 0f,
    enter = fadeIn() + expandVertically(),
    exit = shrinkVertically() + fadeOut()
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(color = controlPanelColor)
        .padding(PureMediaSceneDefaults.ContentPadding)
        .bottomInsetsPadding(),
    ) {
      if (videoPlayerState != null) {
        CustomVideoControl(state = videoPlayerState)
      }
    }
  }
}

private object PureMediaSceneDefaults {
  val ContentPadding = PaddingValues(8.dp)
}

@Composable
fun PureMediaControlPanel(
  controlVisibility: Boolean,
  swiperState: SwiperState,
  controlPanelColor: Color,
  onPopBack: () -> Unit
) {
  AnimatedVisibility(
    visible = controlVisibility && swiperState.progress == 0f,
    enter = fadeIn() + expandVertically(),
    exit = shrinkVertically() + fadeOut()
  ) {
    Box(
      modifier = Modifier
        .topInsetsPadding()
        .padding(16.dp),
    ) {
      Box(
        modifier = Modifier
          .align(Alignment.TopStart)
          .clip(MaterialTheme.shapes.small)
          .background(
            color = controlPanelColor,
            shape = MaterialTheme.shapes.small
          )
          .clipToBounds()
      ) {
        IconButton(
          onClick = {
            onPopBack.invoke()
          }
        ) {
          Icon(
            painter = painterResource(res = com.warpnet.warpnet-android.MR.files.ic_x),
            contentDescription = stringResource(
              res = com.warpnet.warpnet-android.MR.strings.accessibility_common_close
            ),
            modifier = Modifier.size(24.dp),
          )
        }
      }
    }
  }
}

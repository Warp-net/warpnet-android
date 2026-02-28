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
package com.warpnet.warpnetandroid.scenes.gif

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.foundation.LoadingProgress
import com.warpnet.warpnetandroid.component.foundation.TextInput
import com.warpnet.warpnetandroid.component.lazy.ui.LazyUiGifList
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.di.ext.getViewModel
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.model.ui.UiGif
import com.warpnet.warpnetandroid.navigation.Root
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.ui.WarpnetScene
import com.warpnet.warpnetandroid.viewmodel.gif.GifViewModel
import io.github.seiko.precompose.annotation.NavGraphDestination
import moe.tlaster.precompose.navigation.Navigator

@NavGraphDestination(
  route = Root.Gif.Home,
)
@Composable
fun GifScene(
  navigator: Navigator,
) {
  val viewModel = getViewModel<GifViewModel>()
  val enable by viewModel.enable.collectAsState(initial = false)
  val account = LocalActiveAccount.current
  val commitLoading by viewModel.commitLoading.collectAsState(initial = false)
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
            Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.accessibility_scene_gif_title))
          },
          actions = {
            IconButton(
              onClick = {
                viewModel.commit(
                  platform = account?.type ?: PlatformType.Warpnet,
                  onSuccess = {
                    navigator.goBackWith(it)
                  }
                )
              },
              enabled = enable
            ) {
              Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(res = com.warpnet.warpnetandroid.MR.strings.common_controls_actions_yes),
                tint = if (enable) MaterialTheme.colors.primary else LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
              )
            }
          }
        )
      },
    ) {
      Box {
        GifContent(viewModel = viewModel)
        if (commitLoading) {
          LoadingView()
        }
      }
    }
  }
}

@Composable
fun LoadingView() {
  Box(
    modifier = Modifier.fillMaxSize()
      .clickable { }
      .background(color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)),
    contentAlignment = Alignment.Center
  ) {
    LoadingProgress()
  }
}

@Composable
private fun GifContent(viewModel: GifViewModel) {
  val searchInput by viewModel.input.collectAsState()
  val searchState by viewModel.searchFlow.collectAsState(initial = null)
  val searchSource = searchState?.collectAsLazyPagingItems()
  val trendingSource = viewModel.trendSource.collectAsLazyPagingItems()
  val selectedItem by viewModel.selectedItem.collectAsState(initial = null)
  Column {
    SearchInput(
      input = searchInput,
      onValueChanged = {
        viewModel.input.value = it
      }
    )
    Divider()
    GifList(
      data = if (searchInput.isNotEmpty() && searchSource != null) searchSource else trendingSource,
      selectedItem = selectedItem,
      onItemSelected = { viewModel.selectedItem.value = it }
    )
  }
}

@Composable
private fun SearchInput(
  input: String,
  onValueChanged: (value: String) -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier.padding(
      SearchInputDefaults.ContentPadding
    )
  ) {
    Icon(
      painter = painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_search),
      contentDescription = stringResource(
        res = com.warpnet.warpnetandroid.MR.strings.scene_search_title
      ),
      modifier = Modifier.size(24.dp),
    )
    Spacer(modifier = Modifier.width(SearchInputDefaults.ContentSpacing))
    TextInput(
      value = input,
      onValueChange = onValueChanged,
      modifier = Modifier.weight(1f),
      placeholder = {
        Text(text = stringResource(res = com.warpnet.warpnetandroid.MR.strings.accessibility_scene_gif_search))
      },
      maxLines = 1,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
    )
  }
}

private object SearchInputDefaults {
  val ContentPadding = PaddingValues(16.dp)
  val ContentSpacing = 16.dp
}

@Composable
private fun GifList(data: LazyPagingItems<UiGif>, selectedItem: UiGif?, onItemSelected: (UiGif) -> Unit = {}) {
  LazyUiGifList(
    items = data,
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    selectedItem = selectedItem,
    onItemSelected = onItemSelected
  )
}

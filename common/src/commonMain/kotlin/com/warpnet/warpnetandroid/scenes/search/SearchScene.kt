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
package com.warpnet.warpnetandroid.scenes.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.warpnet.warpnetandroid.component.foundation.AppBar
import com.warpnet.warpnetandroid.component.foundation.AppBarDefaults
import com.warpnet.warpnetandroid.component.foundation.AppBarNavigationButton
import com.warpnet.warpnetandroid.component.foundation.InAppNotificationScaffold
import com.warpnet.warpnetandroid.component.foundation.Pager
import com.warpnet.warpnetandroid.component.foundation.rememberPagerState
import com.warpnet.warpnetandroid.component.painterResource
import com.warpnet.warpnetandroid.component.stringResource
import com.warpnet.warpnetandroid.extensions.rememberPresenterState
import com.warpnet.warpnetandroid.extensions.withElevation
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.scenes.search.presenter.SearchSaveEvent
import com.warpnet.warpnetandroid.scenes.search.presenter.SearchSavePresenter
import com.warpnet.warpnetandroid.scenes.search.presenter.SearchSaveState
import com.warpnet.warpnetandroid.scenes.search.tabs.MastodonSearchHashtagItem
import com.warpnet.warpnetandroid.scenes.search.tabs.SearchTweetsItem
import com.warpnet.warpnetandroid.scenes.search.tabs.SearchUserItem
import com.warpnet.warpnetandroid.scenes.search.tabs.WarpnetSearchMediaItem
import com.warpnet.warpnetandroid.ui.LocalActiveAccount
import com.warpnet.warpnetandroid.ui.WarpnetScene
import kotlinx.coroutines.launch
import moe.tlaster.precompose.navigation.Navigator

@Composable
fun SearchScene(
  keyword: String,
  navigator: Navigator,
) {
  val account = LocalActiveAccount.current ?: return

  val (state, channel) = rememberPresenterState<SearchSaveState, SearchSaveEvent> {
    SearchSavePresenter(it, content = keyword)
  }

  val tabs = remember {
    when (account.type) {
      PlatformType.Warpnet -> listOf(
        SearchTweetsItem(),
        WarpnetSearchMediaItem(),
        SearchUserItem()
      )
      else -> listOf(
        SearchTweetsItem(),
        SearchUserItem(),
        MastodonSearchHashtagItem(),
      )
    }
  }
  val pagerState = rememberPagerState(pageCount = tabs.size)
  val scope = rememberCoroutineScope()
  WarpnetScene {
    InAppNotificationScaffold {
      Column {
        Surface(
          elevation = AppBarDefaults.TopAppBarElevation,
        ) {
          Column {
            AppBar(
              navigationIcon = {
                AppBarNavigationButton(
                  onBack = {
                    navigator.popBackStack()
                  }
                )
              },
              elevation = 0.dp,
              title = {
                ProvideTextStyle(value = MaterialTheme.typography.body1) {
                  Row {
                    Text(
                      modifier = Modifier
                        .clickable(
                          onClick = {
                            navigator.goBack()
                          },
                          indication = null,
                          interactionSource = remember { MutableInteractionSource() }
                        )
                        .align(Alignment.CenterVertically)
                        .weight(1F),
                      text = keyword,
                      maxLines = 1,
                      textAlign = TextAlign.Start,
                    )
                    if (state is SearchSaveState.Data) {
                      if (state.loading) {
                        CircularProgressIndicator(
                          modifier = Modifier
                            .size(SearchSceneDefaults.Loading.size)
                            .padding(SearchSceneDefaults.Loading.padding),
                          strokeWidth = SearchSceneDefaults.Loading.width,
                          color = MaterialTheme.colors.onSurface.copy(0.08f)
                        )
                      } else if (!state.isSaved) {
                        IconButton(
                          onClick = {
                            channel.trySend(SearchSaveEvent.Save)
                          }
                        ) {
                          Icon(
                            painter = painterResource(res = com.warpnet.warpnetandroid.MR.files.ic_device_floppy),
                            contentDescription = stringResource(
                              res = com.warpnet.warpnetandroid.MR.strings.accessibility_scene_search_save
                            ),
                            modifier = Modifier.size(24.dp),
                          )
                        }
                      }
                    }
                  }
                }
              }
            )

            TabRow(
              selectedTabIndex = pagerState.currentPage,
              backgroundColor = MaterialTheme.colors.surface.withElevation(),
              indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                  modifier = Modifier.tabIndicatorOffset(
                    tabPositions[pagerState.currentPage]
                  ),
                  color = MaterialTheme.colors.primary,
                )
              }
            ) {
              tabs.forEachIndexed { index, item ->
                Tab(
                  selected = pagerState.currentPage == index,
                  onClick = {
                    scope.launch {
                      pagerState.currentPage = index
                    }
                  },
                  content = {
                    Box(
                      modifier = Modifier.padding(16.dp)
                    ) {
                      Text(text = item.name())
                    }
                  },
                )
              }
            }
          }
        }
        Box(
          modifier = Modifier.weight(1F),
        ) {
          Pager(state = pagerState) {
            tabs[page].Content(keyword = keyword, navigator = navigator)
          }
        }
      }
    }
  }
}

private object SearchSceneDefaults {
  object Loading {
    val padding = PaddingValues(12.dp)
    val size = 48.dp
    val width = 2.dp
  }
}

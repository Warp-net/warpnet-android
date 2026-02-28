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
package com.warpnet.warpnet-android.component.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.warpnet.warpnet-android.BuildConfig
import com.warpnet.warpnet-android.component.LoginLogo
import com.warpnet.warpnet-android.component.stringResource
import com.warpnet.warpnet-android.ui.WarpnetScene

@Composable
fun SignInScaffold(
  popBackStack: () -> Unit,
  countAction: (count: Int) -> Unit = {},
  content: @Composable ColumnScope.() -> Unit,
) {
  var count by remember { mutableStateOf(0) }
  WarpnetScene {
    InAppNotificationScaffold(
      topBar = {
        AppBar(
          navigationIcon = {
            AppBarNavigationButton(
              icon = Icons.Default.Close,
              onBack = popBackStack,
            )
          },
          elevation = 0.dp,
        )
      }
    ) {
      Column(
        modifier = Modifier
          .padding(horizontal = SignInScaffoldDefaults.ContentPadding)
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Row(
          modifier = Modifier.align(Alignment.Start),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          LoginLogo(
            modifier = Modifier
              .size(with(LocalDensity.current) { MaterialTheme.typography.h4.fontSize.toDp() })
              .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                  count++
                  countAction.invoke(count)
                },
              )
          )
          Spacer(modifier = Modifier.width(SignInScaffoldDefaults.IconSpacing))
          Text(
            text = BuildConfig.APPLICATION_NAME,
            style = MaterialTheme.typography.h4,
          )
        }
        Text(
          modifier = Modifier
            .weight(1F)
            .align(Alignment.Start),
          text = stringResource(res = com.warpnet.warpnet-android.MR.strings.scene_sign_in_hello_sign_in_to_get_started),
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.h3,
          color = MaterialTheme.colors.primary,
        )

        content.invoke(this)

        Spacer(modifier = Modifier.height(SignInScaffoldDefaults.BottomSpacing))
      }
    }
  }
}

object SignInScaffoldDefaults {
  val ContentPadding = 20.dp
  val IconSpacing = 16.dp
  val BottomSpacing = 32.dp
}

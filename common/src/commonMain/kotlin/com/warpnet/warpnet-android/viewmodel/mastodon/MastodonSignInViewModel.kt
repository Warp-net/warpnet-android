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
package com.warpnet.warpnet-android.viewmodel.mastodon

import androidx.compose.ui.text.input.TextFieldValue
import com.eygraber.uri.Uri
import com.warpnet.services.mastodon.MastodonOAuthService
import com.warpnet.warpnet-android.dataprovider.mapper.toAmUser
import com.warpnet.warpnet-android.dataprovider.mapper.toUi
import com.warpnet.warpnet-android.http.WarpnetServiceFactory
import com.warpnet.warpnet-android.model.MicroBlogKey
import com.warpnet.warpnet-android.model.cred.CredentialsType
import com.warpnet.warpnet-android.model.cred.OAuth2Credentials
import com.warpnet.warpnet-android.model.enums.PlatformType
import com.warpnet.warpnet-android.navigation.RootDeepLinks
import com.warpnet.warpnet-android.notification.InAppNotification
import com.warpnet.warpnet-android.repository.AccountRepository
import com.warpnet.warpnet-android.utils.OAuthLauncher
import com.warpnet.warpnet-android.utils.json
import com.warpnet.warpnet-android.utils.notifyError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

class MastodonSignInViewModel(
  private val repository: AccountRepository,
  private val inAppNotification: InAppNotification,
  private val oAuthLauncher: OAuthLauncher,
) : ViewModel() {

  val loading = MutableStateFlow(false)
  val host = MutableStateFlow(TextFieldValue())
  fun setHost(value: TextFieldValue) {
    host.value = value
  }

  fun beginOAuth(
    urlString: String,
    finished: (success: Boolean) -> Unit,
  ) = viewModelScope.launch {
    loading.value = true
    val uri = Uri.parseOrNull(urlString) ?: Uri.parse("https://$host")
    runCatching {
      val service = MastodonOAuthService(
        baseUrl = uri.toString(),
        client_name = "Warpnet Android",
        website = "https://github.com/WarpnetProject/WarpnetAndroid-Android",
        redirect_uri = RootDeepLinks.Callback.SignIn.Mastodon,
        httpClientFactory = WarpnetServiceFactory.createHttpClientFactory()
      )
      val application = service.createApplication()
      val target = service.getWebOAuthUrl(application)
      val code = oAuthLauncher.launchOAuth(target, "code")
      if (code.isNotBlank()) {
        val accessTokenResponse = service.getAccessToken(code, application)
        val accessToken = accessTokenResponse.accessToken
        if (accessToken != null) {
          val user = service.verifyCredentials(accessToken = accessToken)
          val name = user.username
          val id = user.id
          if (name != null && id != null) {
            val displayKey = MicroBlogKey(name, host = uri.host!!)
            val internalKey = MicroBlogKey(id, host = uri.host!!)
            val credentials_json = OAuth2Credentials(
              access_token = accessToken
            ).json()
            if (repository.containsAccount(internalKey)) {
              repository.findByAccountKey(internalKey)?.let {
                it.credentials_json = credentials_json
                repository.updateAccount(it)
              }
            } else {
              repository.addAccount(
                displayKey = displayKey,
                type = PlatformType.Mastodon,
                accountKey = internalKey,
                credentials_type = CredentialsType.OAuth2,
                credentials_json = credentials_json,
                extras_json = "",
                user = user.toUi(accountKey = internalKey).toAmUser(),
                lastActive = System.currentTimeMillis()
              )
            }
            finished.invoke(true)
          }
        }
      }
    }.onFailure {
      inAppNotification.notifyError(it)
    }
    loading.value = false
    finished.invoke(false)
  }
}

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
package com.warpnet.warpnetandroid.viewmodel.warpnet

import com.warpnet.services.warpnet.WarpnetOAuthService
import com.warpnet.services.warpnet.WarpnetService
import com.warpnet.warpnetandroid.BuildConfig
import com.warpnet.warpnetandroid.dataprovider.mapper.toAmUser
import com.warpnet.warpnetandroid.dataprovider.mapper.toUi
import com.warpnet.warpnetandroid.http.WarpnetServiceFactory
import com.warpnet.warpnetandroid.model.MicroBlogKey
import com.warpnet.warpnetandroid.model.cred.CredentialsType
import com.warpnet.warpnetandroid.model.cred.OAuthCredentials
import com.warpnet.warpnetandroid.model.enums.PlatformType
import com.warpnet.warpnetandroid.navigation.RootDeepLinks
import com.warpnet.warpnetandroid.notification.InAppNotification
import com.warpnet.warpnetandroid.repository.AccountRepository
import com.warpnet.warpnetandroid.utils.OAuthLauncher
import com.warpnet.warpnetandroid.utils.json
import com.warpnet.warpnetandroid.utils.notifyError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

typealias PinCodeProvider = suspend (url: String) -> String?
typealias OnResult = (success: Boolean) -> Unit

class WarpnetSignInViewModel(
  private val repository: AccountRepository,
  private val inAppNotification: InAppNotification,
  private val consumerKey: String,
  private val consumerSecret: String,
  private val oAuthLauncher: OAuthLauncher,
  private val pinCodeProvider: PinCodeProvider,
  private val onResult: OnResult,
) : ViewModel() {

  val success = MutableStateFlow(false)
  val loading = MutableStateFlow(false)

  init {
    viewModelScope.launch {
      val result = beginOAuth()
      onResult.invoke(result)
    }
  }

  private suspend fun beginOAuth(): Boolean {
    loading.value = true
    try {
      val service = WarpnetOAuthService(
        consumerKey,
        consumerSecret,
        WarpnetServiceFactory.createHttpClientFactory()
      )
      val token = service.getOAuthToken(
        if (isBuiltInKey()) {
          RootDeepLinks.Callback.SignIn.Warpnet
        } else {
          "oob"
        }
      )
      val pinCode = if (isBuiltInKey()) {
        oAuthLauncher.launchOAuth(service.getWebOAuthUrl(token), "oauth_verifier")
      } else {
        pinCodeProvider.invoke(service.getWebOAuthUrl(token))
      }
      if (!pinCode.isNullOrBlank()) {
        val accessToken = service.getAccessToken(pinCode, token)
        val user = (
          WarpnetServiceFactory.createApiService(
            type = PlatformType.Warpnet,
            credentials = OAuthCredentials(
              consumer_key = consumerKey,
              consumer_secret = consumerSecret,
              access_token = accessToken.oauth_token,
              access_token_secret = accessToken.oauth_token_secret
            ),
            accountKey = MicroBlogKey.Empty
          ) as WarpnetService
          ).verifyCredentials()
        if (user != null) {
          val name = user.screenName
          val id = user.idStr
          if (name != null && id != null) {
            val displayKey = MicroBlogKey.warpnet(name)
            val internalKey = MicroBlogKey.warpnet(id)
            val credentials_json = OAuthCredentials(
              consumer_key = consumerKey,
              consumer_secret = consumerSecret,
              access_token = accessToken.oauth_token,
              access_token_secret = accessToken.oauth_token_secret,
            ).json()
            if (repository.containsAccount(internalKey)) {
              repository.findByAccountKey(internalKey)?.let {
                it.credentials_json = credentials_json
                repository.updateAccount(it)
              }
            } else {
              repository.addAccount(
                displayKey = displayKey,
                type = PlatformType.Warpnet,
                accountKey = internalKey,
                credentials_type = CredentialsType.OAuth,
                credentials_json = credentials_json,
                extras_json = "",
                user = user.toUi(accountKey = internalKey).toAmUser(),
                lastActive = System.currentTimeMillis()
              )
            }
            return true
          }
        }
      }
    } catch (e: Throwable) {
      inAppNotification.notifyError(e)
      e.printStackTrace()
    }
    loading.value = false
    return false
  }

  private fun isBuiltInKey(): Boolean {
    return consumerKey == BuildConfig.CONSUMERKEY && consumerSecret == BuildConfig.CONSUMERSECRET
  }

  fun cancel() {
    loading.value = false
  }
}

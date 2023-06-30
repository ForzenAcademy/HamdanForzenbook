package com.hamdan.forzenbook.login.core.data.repository

import android.content.Context
import android.util.Log
import com.hamdan.forzenbook.core.FailTokenRetrievalException
import com.hamdan.forzenbook.core.GlobalConstants
import com.hamdan.forzenbook.core.NullTokenException
import com.hamdan.forzenbook.core.getToken
import com.hamdan.forzenbook.core.removeToken
import com.hamdan.forzenbook.login.core.data.network.LoginService

class LoginRepositoryImpl(
  private val service: LoginService,
  private val context: Context,
) : LoginRepository {
  override suspend fun requestValidation(email: String) {
    service.requestValidation(email)
  }

  override suspend fun getToken(email: String?, code: String?) {
    if (email.isNullOrEmpty() && code.isNullOrEmpty()) getToken(context)
      ?: throw NullTokenException()
    else email?.let { code?.let { getTokenFromNetwork(email, code) } }
  }

  private suspend fun getTokenFromNetwork(email: String, code: String) {
    val response = service.getToken(email, code)
    if (response.isSuccessful) {
      val body = response.body() ?: throw FailTokenRetrievalException("Failed to get Token")
      body.token ?: throw NullTokenException("null token")
      context.getSharedPreferences(
        GlobalConstants.TOKEN_PREFERENCE_LOCATION,
        Context.MODE_PRIVATE
      ).edit().apply {
        putString(GlobalConstants.TOKEN_KEY, body.token)
        apply()
      }
    } else {
      removeToken(context)
      throw FailTokenRetrievalException("Failed to get Token")
    }
  }
}

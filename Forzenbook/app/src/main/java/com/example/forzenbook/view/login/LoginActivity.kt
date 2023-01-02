package com.example.forzenbook.view.login

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.forzenbook.view.LocalNavController
import com.example.forzenbook.view.NavigationDestinations
import com.example.forzenbook.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {

            val navController = rememberNavController()
            CompositionLocalProvider(LocalNavController provides navController) {
                NavHost(
                    navController = navController,
                    startDestination = NavigationDestinations.LOGIN_PAGE
                ) {
                    composable(NavigationDestinations.LOGIN_PAGE) {
                        MainLoginContent(
                            loginViewModel.loginState.value,
                            { email, password, emailError, passwordError ->
                                loginViewModel.updateLoginTexts(
                                    email,
                                    password,
                                    emailError,
                                    passwordError
                                )
                            }
                        ) {
                            loginViewModel.submitLogin()
                        }
                    }
                    composable(NavigationDestinations.CREATE_ACCOUNT) {
                        CreateAccountContent(loginViewModel.createAccountState.value,
                            { first, last, birth, email, pass, location,
                              firstError, lastError, birthError, emailError, passError, locationError ->
                                loginViewModel.updateCreateAccountTextAndErrors(
                                    first,
                                    last,
                                    birth,
                                    email,
                                    pass,
                                    location,
                                    firstError,
                                    lastError,
                                    birthError,
                                    emailError,
                                    passError,
                                    locationError
                                )
                            }) {
                            loginViewModel.createAccount()
                        }
                    }
                    composable(NavigationDestinations.FORGOT_PASSWORD) {
                        ForgotPasswordContent(
                            loginViewModel.forgotPasswordState.value,
                            { email, emailError ->
                                loginViewModel.updateForgotPasswordText(email, emailError)
                            }) {
                            loginViewModel.requestReset()
                        }
                    }
                }

            }
            loginViewModel.onAccountCreateSuccess = {
                navController.navigateUp()
            }
        }
    }
}


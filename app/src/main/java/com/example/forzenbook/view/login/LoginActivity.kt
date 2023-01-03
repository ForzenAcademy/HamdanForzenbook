package com.example.forzenbook.view.login

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.forzenbook.R
import com.example.forzenbook.theme.ForzenBookTheme
import com.example.forzenbook.theme.ForzenbookTheme
import com.example.forzenbook.theme.IconSizeValues
import com.example.forzenbook.theme.PaddingValues
import com.example.forzenbook.view.LocalNavController
import com.example.forzenbook.view.NavigationDestinations
import com.example.forzenbook.view.composeutils.ComposeUtils
import com.example.forzenbook.view.composeutils.LoginBackgroundColumn
import com.example.forzenbook.view.composeutils.LoginTitleSection
import com.example.forzenbook.view.isOnline
import com.example.forzenbook.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContent {
            ForzenBookTheme {
                if (!isOnline(this)) {
                    LoginBackgroundColumn {
                        Image(
                            modifier = Modifier.size(IconSizeValues.giga_1),
                            painter = painterResource(id = R.drawable.logo_render_full_notext),
                            contentDescription = "Yellow lion"
                        )
                        LoginTitleSection(ComposeUtils.APP_NAME)
                        Text(
                            text = "Unable to connect due to no internet connection",
                            fontSize = ForzenbookTheme.typography.button.fontSize,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(PaddingValues.smallPad_2)
                        )
                    }
                } else {
                    val navController = rememberNavController()
                    CompositionLocalProvider(LocalNavController provides navController) {
                        NavHost(
                            navController = navController,
                            startDestination = NavigationDestinations.LOGIN_PAGE
                        ) {
                            composable(NavigationDestinations.LOGIN_PAGE) {
                                MainLoginContent(
                                    loginViewModel.loginState.value, {
                                        loginViewModel.resetLoginState()
                                    },
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
                                CreateAccountContent(loginViewModel.createAccountState.value, {
                                    loginViewModel.resetCreateAccountState()
                                },
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
                                    loginViewModel.forgotPasswordState.value, {
                                        loginViewModel.resetForgotPasswordState()
                                    },
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
    }
}


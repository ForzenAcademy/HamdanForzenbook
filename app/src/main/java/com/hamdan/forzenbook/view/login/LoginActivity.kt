package com.hamdan.forzenbook.view.login

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hamdan.forzenbook.theme.ForzenBookTheme
import com.hamdan.forzenbook.view.LocalNavController
import com.hamdan.forzenbook.view.NavigationDestinations
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        setContent {
            ForzenBookTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestinations.LOGIN_PAGE
                    ) {
                        composable(NavigationDestinations.LOGIN_PAGE) {
                            MainLoginContent(
                                state = loginViewModel.loginState.value,
                                onInfoDismiss = {
                                    loginViewModel.loginDismissInfoClicked()
                                },
                                onErrorDismiss = {
                                    loginViewModel.loginDismissErrorClicked()
                                },
                                onTextChange = { email, code, isInputting ->
                                    loginViewModel.updateLoginTexts(
                                        email,
                                        code,
                                        isInputting
                                    )
                                }
                            ) {
                                loginViewModel.loginClicked()
                            }
                        }
                        composable(NavigationDestinations.CREATE_ACCOUNT) {
                            CreateAccountContent(
                                state = loginViewModel.createAccountState.value,
                                onErrorDismiss = {
                                    loginViewModel.createAccountDismissErrorClicked()
                                },
                                onTextChange = { firstName, lastName, birthDate, email, location ->
                                    loginViewModel.updateCreateAccountTextAndErrors(
                                        firstName,
                                        lastName,
                                        birthDate,
                                        email,
                                        location,
                                    )
                                },
                                onDateFieldClick = { loginViewModel.createAccountDateDialogClicked() },
                                onDateSubmission = { loginViewModel.createAccountDateDialogSubmitClicked() },
                                onDateDismiss = { loginViewModel.createAccountDateDialogDismiss() }
                            ) {
                                loginViewModel.createAccount()
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

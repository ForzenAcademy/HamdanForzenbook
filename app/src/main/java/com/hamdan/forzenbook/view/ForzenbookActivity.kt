package com.hamdan.forzenbook.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hamdan.forzenbook.compose.core.LocalNavController
import com.hamdan.forzenbook.compose.core.theme.ForzenBookTheme
import com.hamdan.forzenbook.createaccount.compose.CreateAccountContent
import com.hamdan.forzenbook.createaccount.core.viewmodel.toCreateAccountUiState
import com.hamdan.forzenbook.login.compose.MainLoginContent
import com.hamdan.forzenbook.login.core.viewmodel.toLoginUiState
import com.hamdan.forzenbook.post.compose.PostContent
import com.hamdan.forzenbook.post.core.viewmodel.toUiState
import com.hamdan.forzenbook.viewmodels.CreateAccountViewModel
import com.hamdan.forzenbook.viewmodels.LoginViewModel
import com.hamdan.forzenbook.viewmodels.PostViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForzenbookActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val createAccountViewModel: CreateAccountViewModel by viewModels()
    private val postViewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForzenBookTheme {
                val navController = rememberNavController()
                CompositionLocalProvider(LocalNavController provides navController) {
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestinations.POST_PAGE
                    ) {
                        composable(NavigationDestinations.LOGIN_PAGE) {
                            MainLoginContent(
                                state = loginViewModel.state.value.toLoginUiState(),
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
                                },
                                onSubmission = {
                                    loginViewModel.loginClicked()
                                }
                            ) {
                                navController.navigate(NavigationDestinations.CREATE_ACCOUNT_PAGE)
                            }
                        }
                        composable(NavigationDestinations.CREATE_ACCOUNT_PAGE) {
                            CreateAccountContent(
                                state = createAccountViewModel.state.value.toCreateAccountUiState(),
                                onErrorDismiss = {
                                    createAccountViewModel.createAccountDismissErrorClicked()
                                },
                                onTextChange = { firstName, lastName, birthDate, email, location ->
                                    createAccountViewModel.updateCreateAccountTextAndErrors(
                                        firstName,
                                        lastName,
                                        birthDate,
                                        email,
                                        location,
                                    )
                                },
                                onDateFieldClick = { createAccountViewModel.createAccountDateDialogClicked() },
                                onDateSubmission = { createAccountViewModel.createAccountDateDialogSubmitClicked() },
                                onDateDismiss = { createAccountViewModel.createAccountDateDialogDismiss() },
                                onSubmission = { createAccountViewModel.createAccountClicked() },
                            )
                        }
                        composable(NavigationDestinations.POST_PAGE) {
                            PostContent(
                                state = postViewModel.state.value.toUiState(),
                                onTextChange = { postViewModel.updateText(it) },
                                onToggleClicked = { postViewModel.toggleClicked() },
                                onDialogDismiss = { postViewModel.dialogDismissClicked() }
                            ) {
                                postViewModel.sendPostClicked()
                            }
                        }
                    }
                }
            }
        }
    }
}

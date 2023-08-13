package com.hamdan.forzenbook.login.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import com.hamdan.forzenbook.login.core.viewmodel.getContent
import com.hamdan.forzenbook.ui.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(
    state: BaseLoginViewModel.LoginState,
    onInfoDismiss: () -> Unit,
    onErrorDismiss: () -> Unit,
    onBackPressed: () -> Unit,
    onTextChange: (Entry) -> Unit,
    onSubmission: () -> Unit,
    onCreateAccountPress: () -> Unit,
    onLoggedIn: () -> Unit,
) {

    when (state) {
        is BaseLoginViewModel.LoginState.Content -> {
            state.getContent().let {
                MainContent(
                    subject = if (it is BaseLoginViewModel.LoginContent.Email) it.email else (it as BaseLoginViewModel.LoginContent.Code).code,
                    showInfo = if (it is BaseLoginViewModel.LoginContent.Code) it.showInfoDialog else false,
                    inputType = if (it is BaseLoginViewModel.LoginContent.Email) BaseLoginViewModel.LoginInputType.EMAIL else BaseLoginViewModel.LoginInputType.CODE,
                    onTextChange = onTextChange,
                    onSubmission = onSubmission,
                    onCreateAccountPress = onCreateAccountPress,
                    onInfoDismiss = if (it is BaseLoginViewModel.LoginContent.Email) {
                        {}
                    } else {
                        onInfoDismiss
                    }
                )
                if (it is BaseLoginViewModel.LoginContent.Code) {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onBackPressed) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = stringResource(id = R.string.back_arrow),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                }
            }
        }

        is BaseLoginViewModel.LoginState.Error -> {
            ErrorContent(state.loginInputType, onErrorDismiss)
        }

        is BaseLoginViewModel.LoginState.Loading -> {
            LoadingContent(loginType = state.loginInputType)
            PreventScreenActionsDuringLoad()
        }

        BaseLoginViewModel.LoginState.LoggedIn -> {
            LaunchedEffect(Unit) {
                onLoggedIn()
            }
        }

        else -> throw StateException()
    }
}

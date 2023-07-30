package com.hamdan.forzenbook.login.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.hamdan.forzenbook.compose.core.composewidgets.BackgroundColumn
import com.hamdan.forzenbook.compose.core.composewidgets.ErrorText
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.compose.core.composewidgets.InputField
import com.hamdan.forzenbook.compose.core.composewidgets.LoadingButton
import com.hamdan.forzenbook.compose.core.composewidgets.LoginTitleSection
import com.hamdan.forzenbook.compose.core.composewidgets.PreventScreenActionsDuringLoad
import com.hamdan.forzenbook.compose.core.composewidgets.SubmitButton
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.core.StateException
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import com.hamdan.forzenbook.login.core.viewmodel.getContent
import com.hamdan.forzenbook.ui.core.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLoginContent(
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

@Composable
private fun LoadingContent(
    loginType: BaseLoginViewModel.LoginInputType,
) {
    MainContent(inputType = loginType, isLoading = true)
}

@Composable
private fun ErrorContent(
    loginType: BaseLoginViewModel.LoginInputType,
    onErrorDismiss: () -> Unit,
) {
    MainContent(inputType = loginType)
    ForzenbookDialog(
        title = stringResource(R.string.login_error_title),
        body = stringResource(R.string.login_error),
        buttonText = stringResource(id = R.string.generic_dialog_confirm),
        onDismiss = onErrorDismiss
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun MainContent(
    subject: Entry = Entry("", EntryError.NameError.None),
    showInfo: Boolean = false,
    inputType: BaseLoginViewModel.LoginInputType,
    isLoading: Boolean = false,
    onTextChange: (Entry) -> Unit = { _ -> },
    onSubmission: () -> Unit = {},
    onCreateAccountPress: () -> Unit = {},
    onInfoDismiss: () -> Unit = {},
) {
    var text = subject.text
    var textError = subject.error
    val keyboardController = LocalSoftwareKeyboardController.current
    BackgroundColumn {
        Image(
            modifier = Modifier.size(MaterialTheme.dimens.imageSizes.large),
            painter = painterResource(id = R.drawable.logo_render_full_nobackground),
            contentDescription = stringResource(id = R.string.lion_icon),
        )
        LoginTitleSection(title = stringResource(id = R.string.app_name))
        InputField(
            label = if (inputType == BaseLoginViewModel.LoginInputType.EMAIL) stringResource(R.string.login_email_prompt) else stringResource(
                R.string.login_code_prompt
            ),
            value = text,
            onValueChange = {
                text = it
                onTextChange(Entry(text, textError))
            },
            imeAction = ImeAction.Done,
            onDone = {
                keyboardController?.hide()
                if (textError.isValid()) {
                    onSubmission()
                }
            },
        )
        if (inputType == BaseLoginViewModel.LoginInputType.EMAIL) {
            if (text.isNotEmpty() && !textError.isValid()) {
                if (textError == EntryError.EmailError.Length) {
                    ErrorText(error = stringResource(R.string.login_email_error_length))
                } else if (textError == EntryError.EmailError.InvalidFormat) {
                    ErrorText(error = stringResource(R.string.login_email_error_format))
                }
            }
        } else {
            if (text.isNotEmpty() && !textError.isValid()) {
                if (textError == EntryError.CodeError.Length) {
                    ErrorText(error = stringResource(R.string.login_code_error))
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.grid.x5))
        if (isLoading) {
            LoadingButton()
        } else {
            SubmitButton(
                onSubmission = onSubmission,
                label = stringResource(R.string.login_button_text),
                enabled = textError.isValid()
            )
        }
        Spacer(modifier = Modifier.height(MaterialTheme.dimens.grid.x5))
        Text(
            text = stringResource(R.string.login_create_account_text),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .clickable {
                    onCreateAccountPress()
                }
                .padding(
                    top = MaterialTheme.dimens.grid.x3,
                    bottom = MaterialTheme.dimens.grid.x5,
                    start = MaterialTheme.dimens.grid.x3,
                    end = MaterialTheme.dimens.grid.x3
                ),
            color = MaterialTheme.colorScheme.onBackground
        )
        if (showInfo) {
            ForzenbookDialog(
                title = null,
                body = stringResource(R.string.login_info),
                buttonText = stringResource(id = R.string.generic_dialog_confirm),
                onDismiss = onInfoDismiss
            )
        }
    }
}

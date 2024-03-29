package com.hamdan.forzenbook.login.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
import com.hamdan.forzenbook.compose.core.composewidgets.SubmitButton
import com.hamdan.forzenbook.compose.core.theme.dimens
import com.hamdan.forzenbook.core.Entry
import com.hamdan.forzenbook.core.EntryError
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import com.hamdan.forzenbook.ui.core.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun MainContent(
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
                buttonText = stringResource(R.string.login_button_text),
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
                confirmationText = stringResource(id = R.string.generic_dialog_confirm),
                onDismiss = onInfoDismiss
            )
        }
    }
}

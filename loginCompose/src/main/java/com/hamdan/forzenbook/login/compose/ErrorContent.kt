package com.hamdan.forzenbook.login.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hamdan.forzenbook.compose.core.composewidgets.ForzenbookDialog
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel
import com.hamdan.forzenbook.ui.core.R

@Composable
internal fun ErrorContent(
    loginType: BaseLoginViewModel.LoginInputType,
    onErrorDismiss: () -> Unit,
) {
    MainContent(inputType = loginType)
    ForzenbookDialog(
        title = stringResource(R.string.login_error_title),
        body = stringResource(R.string.login_error),
        confirmationText = stringResource(id = R.string.generic_dialog_confirm),
        onDismiss = onErrorDismiss
    )
}

package com.hamdan.forzenbook.login.compose

import androidx.compose.runtime.Composable
import com.hamdan.forzenbook.login.core.viewmodel.BaseLoginViewModel

@Composable
internal fun LoadingContent(
    loginType: BaseLoginViewModel.LoginInputType,
) {
    MainContent(inputType = loginType, isLoading = true)
}

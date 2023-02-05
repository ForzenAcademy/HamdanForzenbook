package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.viewmodels.LoginViewModel

interface LoginStringValidationUseCase {
    operator fun invoke(state: LoginViewModel.LoginState): LoginViewModel.LoginState
}

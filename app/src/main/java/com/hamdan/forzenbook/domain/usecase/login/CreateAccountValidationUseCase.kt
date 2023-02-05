package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.viewmodels.LoginViewModel

interface CreateAccountValidationUseCase {
    operator fun invoke(state: LoginViewModel.CreateAccountState): LoginViewModel.CreateAccountState
}

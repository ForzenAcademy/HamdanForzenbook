package com.hamdan.forzenbook.login.core.domain.usecase

interface LoginStringValidationUseCase {
    operator fun invoke(state: LoginEntrys): LoginEntrys
}

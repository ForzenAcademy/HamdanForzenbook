package com.hamdan.forzenbook.createaccount.core.domain

interface CreateAccountValidationUseCase {
    operator fun invoke(state: CreateAccountEntrys): CreateAccountEntrys
}

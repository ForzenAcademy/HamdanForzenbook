package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginData

interface ForgotPasswordResetUseCase {

    suspend operator fun invoke(email:String): Int

}
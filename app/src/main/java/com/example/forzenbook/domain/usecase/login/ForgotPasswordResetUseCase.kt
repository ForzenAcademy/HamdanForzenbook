package com.example.forzenbook.domain.usecase.login

import com.example.forzenbook.data.repository.LoginData

interface ForgotPasswordResetUseCase {

    suspend operator fun invoke(email:String): Int

}
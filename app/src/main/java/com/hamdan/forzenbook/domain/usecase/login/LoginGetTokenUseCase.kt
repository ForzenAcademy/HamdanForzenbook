package com.hamdan.forzenbook.domain.usecase.login

import com.hamdan.forzenbook.data.repository.LoginData

interface LoginGetTokenUseCase {

    suspend operator fun invoke(email:String): LoginData?

}
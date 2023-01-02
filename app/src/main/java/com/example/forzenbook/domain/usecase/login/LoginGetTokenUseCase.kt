package com.example.forzenbook.domain.usecase.login

import com.example.forzenbook.data.repository.LoginData

interface LoginGetTokenUseCase {

    suspend operator fun invoke(email:String,password:String): LoginData?

}
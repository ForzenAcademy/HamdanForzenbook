package com.example.forzenbook.data.repository

import com.example.forzenbook.data.network.ForgotPasswordService

class ForgotPasswordRepositoryImpl(
    private val service: ForgotPasswordService
) : ForgotPasswordRepository {
    override suspend fun requestReset(email: String): Int {
        //ToDo implement the sending of the email for this when we know more
        return service.requestReset().code()
    }
}
package com.example.forzenbook.data.repository

import android.util.Log
import com.example.forzenbook.data.network.ForgotPasswordService

class ForgotPasswordRepositoryImpl(
    private val service: ForgotPasswordService
) : ForgotPasswordRepository {
    override suspend fun requestReset(email: String): Int {
        //ToDo implement the sending of the email for this when we know more
        return try {
            service.requestReset().code()
        } catch (e: Exception) {
            Log.v("Hamdan", "unidentifiable issue when requesting password reset")
            0
        }
    }
}
package com.example.forzenbook.data.repository

interface ForgotPasswordRepository {

    suspend fun requestReset(email:String): Int

}
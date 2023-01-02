package com.example.forzenbook.data.database.mocks

import android.util.Log
import com.example.forzenbook.data.database.LoginDao
import com.example.forzenbook.data.database.LoginEntity

class MockDatabaseGetsToken : LoginDao {
    override suspend fun insert(loginEntity: LoginEntity) {
        Log.v("Hamdan", "Inserted it haha")
    }

    override suspend fun getToken(): LoginEntity? {
        return LoginEntity("it worked")
    }
}
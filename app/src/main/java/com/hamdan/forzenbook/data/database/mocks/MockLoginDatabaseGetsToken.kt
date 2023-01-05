package com.hamdan.forzenbook.data.database.mocks

import android.util.Log
import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.database.LoginEntity

class MockLoginDatabaseGetsToken : LoginDao {
    override suspend fun insert(loginEntity: LoginEntity) {
        Log.v("Hamdan", "Inserted it haha")
    }

    override suspend fun getToken(): LoginEntity? {
        return LoginEntity("it worked")
    }
}
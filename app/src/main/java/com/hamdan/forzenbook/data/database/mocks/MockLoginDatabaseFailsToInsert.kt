package com.hamdan.forzenbook.data.database.mocks

import android.util.Log
import com.hamdan.forzenbook.data.database.LoginDao
import com.hamdan.forzenbook.data.database.LoginEntity

class MockLoginDatabaseFailsToInsert : LoginDao {
    override suspend fun insert(loginEntity: LoginEntity) {
        throw RuntimeException("Failed to insert")
    }

    override suspend fun getToken(): LoginEntity? {
        return LoginEntity("it worked", "it's an email")
    }

    override suspend fun deleteToken() {
        Log.v("Hamdan", "things were deleted")
    }
}

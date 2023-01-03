package com.example.forzenbook.data.database.mocks

import com.example.forzenbook.data.database.LoginDao
import com.example.forzenbook.data.database.LoginEntity

class MockLoginDatabaseFailsToInsert : LoginDao {
    override suspend fun insert(loginEntity: LoginEntity) {
        throw RuntimeException("Failed to insert")
    }

    override suspend fun getToken(): LoginEntity? {
        return LoginEntity("it worked")
    }
}
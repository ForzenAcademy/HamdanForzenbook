package com.hamdan.forzenbook.java.login.core.data.database;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface LoginDao {
    @Insert
    public void insert(LoginEntity loginEntity);

    @Nullable
    @Query("SELECT * FROM " + LoginEntity.TABLE_NAME + " LIMIT 1")
    public LoginEntity getToken();

    @Query("DELETE FROM " + LoginEntity.TABLE_NAME)
    public void deleteToken();
}

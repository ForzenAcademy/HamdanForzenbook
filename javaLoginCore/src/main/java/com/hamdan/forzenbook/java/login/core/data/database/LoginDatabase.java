package com.hamdan.forzenbook.java.login.core.data.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {LoginEntity.class}, version = 1)
public abstract class LoginDatabase extends RoomDatabase {
    public static final String NAME = "login_db";

    @NonNull
    public abstract LoginDao loginDao();
}

package com.hamdan.forzenbook.java.login.core.data.database;

import static com.hamdan.forzenbook.java.login.core.data.database.LoginEntity.TABLE_NAME;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = TABLE_NAME)
public class LoginEntity {

    final static String TABLE_NAME = "login";
    final static String DATABASE_ID = "login_database_id";
    final static String TOKEN = "login_token";
    final static String EMAIL = "login_email";

    @ColumnInfo(name = TOKEN)
    public String token;

    @ColumnInfo(name = EMAIL)
    public String email;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = DATABASE_ID)
    public int loginId = 0;
}

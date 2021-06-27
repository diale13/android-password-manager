package com.example.ispass.models.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users_table",
        indices = {@Index(value = "id", unique = true)})

public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer id;

    @NonNull
    @ColumnInfo(name = "deviceId")
    public String deviceId;

    @NonNull
    @ColumnInfo(name = "username")
    public String username;

    @NonNull
    @ColumnInfo(name = "email")
    public String email;

    @NonNull
    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "accountSite")
    public String accountSite;

    public User(User user) {
        this.id = user.id;
        this.deviceId = user.deviceId;
        this.username = user.username;
        this.email = user.email;
        this.password = user.password;
        this.accountSite = user.accountSite;
    }

    public User(){}

}

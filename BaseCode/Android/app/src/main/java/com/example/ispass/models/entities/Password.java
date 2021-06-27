package com.example.ispass.models.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "passwords_table",
        indices = {@Index(value = "id", unique = true)})
public class Password implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer id;

    @NonNull
    @ColumnInfo(name = "user_identifier")
    public Integer user_identifier;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @NonNull
    @ColumnInfo(name = "content")
    public String password;

    public Password(Password password) {
        this.id = password.id;
        this.user_identifier = password.user_identifier;
        this.name = password.name;
        this.password = password.password;
    }

    public Password(){}
}

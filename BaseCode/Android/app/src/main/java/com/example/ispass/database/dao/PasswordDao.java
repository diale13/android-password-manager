package com.example.ispass.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ispass.models.entities.Password;

import java.util.List;

@Dao
public interface PasswordDao {

   @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Password password);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(Password password);

    @Delete
    public void delete(Password password);

    @Query("SELECT * FROM passwords_table WHERE user_identifier = :user_identifier")
    List<Password> getUserPasswords(Integer user_identifier);

    @Query("SELECT * FROM passwords_table WHERE user_identifier = :user_identifier and id = :id")
    Password getUserPasswordById(Integer user_identifier, Integer id);

    @Query("SELECT * FROM passwords_table ORDER BY id ASC")
    public List<Password> getAll();

}

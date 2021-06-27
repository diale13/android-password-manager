package com.example.ispass.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ispass.models.entities.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(User user);

    @Delete
    public void delete(User user);

    @Query("DELETE FROM users_table")
    public void deleteAll();

    @Query("SELECT * FROM users_table WHERE email = :email")
    public User getUserByEmail(String email);

    @Query("SELECT * FROM users_table WHERE id = :id")
    public User getUserById(Integer id);

    @Query("SELECT * FROM users_table WHERE email = :email and password = :password")
    public User getUserLogin(String email, String password);

    @Query("SELECT * FROM users_table ORDER BY id ASC")
    public List<User> getAll();
}

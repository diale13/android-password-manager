package com.example.ispass.database;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.User;

import java.util.List;

public class UserRepository {

    private UserDao userDao;
    private List<User> allUsers;

    public UserRepository(Application application) {
        IspDb db = IspDb.getDatabase(application);
        userDao = db.userDao();
        allUsers = userDao.getAll();
    }

    public void insert(User user) {
        IspDb.databaseWriteExecutor.execute(() -> {
            userDao.insert(new User(user));
        });
    }

    public void update(User user) {
        IspDb.databaseWriteExecutor.execute(() -> {
            userDao.update(user);
        });
    }

    public void delete(User user) {
        IspDb.databaseWriteExecutor.execute(() -> {
            userDao.delete(user);
        });
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }

    public User getUserLogin(String email, String password) {
        return userDao.getUserLogin(email, password);
    }

    public List<User> getAll() {
        return allUsers;
    }

    public void deleteAll() {
        IspDb.databaseWriteExecutor.execute(() -> {
            userDao.deleteAll();
        });
    }
}

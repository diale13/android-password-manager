package com.example.ispass.database;

import android.app.Application;

import com.example.ispass.database.dao.PasswordDao;
import com.example.ispass.models.entities.Password;

import java.util.List;

public class PasswordRepository {

    private PasswordDao passwordDao;
    private List<Password> allPasswords;

    public PasswordRepository(Application application) {
        IspDb db = IspDb.getDatabase(application);
        passwordDao = db.passwordDao();
        allPasswords = passwordDao.getAll();
    }

    public void insert(Password password) {
        IspDb.databaseWriteExecutor.execute(() -> {
            passwordDao.insert(new Password(password));
        });
    }

    public void update(Password password) {
        IspDb.databaseWriteExecutor.execute(() -> {
            passwordDao.update(password);
        });
    }

    public void delete(Password password) {
        IspDb.databaseWriteExecutor.execute(() -> {
            passwordDao.delete(password);
        });
    }

    public List<Password> getUserPasswords(Integer user_identifier) {
        return passwordDao.getUserPasswords(user_identifier);
    }

    public Password getUserPasswordById(Integer user_identifier, Integer note_id) {
        return passwordDao.getUserPasswordById(user_identifier, note_id);
    }

    public List<Password> getAll() {
        return allPasswords;
    }
}

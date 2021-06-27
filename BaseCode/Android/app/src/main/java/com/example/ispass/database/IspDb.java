package com.example.ispass.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ispass.database.dao.NoteDao;
import com.example.ispass.database.dao.PasswordDao;
import com.example.ispass.models.entities.Password;
import com.example.ispass.models.entities.User;
import com.example.ispass.models.entities.Note;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.ui.HomeActivity;
import com.example.ispass.ui.UpdateNoteActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Note.class, Password.class}, version = 1)
public abstract class IspDb extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract NoteDao noteDao();
    public abstract PasswordDao passwordDao();

    private static volatile IspDb INSTANCE;
    private static final int NUMBER_OF_THREADS = 3; //1 per table
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static IspDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (IspDb.class) {
                if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                IspDb.class, "isp_database")
                                .allowMainThreadQueries()
                                .fallbackToDestructiveMigration()
                                .build();

                }
            }
        }
        return INSTANCE;
    }
}





package com.example.ispass.database;

import android.app.Application;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ispass.database.dao.NoteDao;
import com.example.ispass.models.entities.Note;
import com.example.ispass.models.entities.User;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private List<Note> allNotes;

    public NoteRepository(Application application) {
        IspDb db = IspDb.getDatabase(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAll();
    }

    public void insert(Note note) {
        IspDb.databaseWriteExecutor.execute(() -> {
            noteDao.insert(new Note(note));
        });
    }

    public void update(Note note) {
        IspDb.databaseWriteExecutor.execute(() -> {
            noteDao.update(note);
        });
    }

    public void delete(Note note) {
        IspDb.databaseWriteExecutor.execute(() -> {
            noteDao.delete(note);
        });
    }

    public List<Note> getUserNotes(Integer user_identifier) {
        return noteDao.getUserNotes(user_identifier);
    }

    public Note getUserNotesById(Integer user_identifier, Integer note_id) {
        return noteDao.getUserNoteById(user_identifier, note_id);
    }

    public List<Note> getAll() {
        return allNotes;
    }

}

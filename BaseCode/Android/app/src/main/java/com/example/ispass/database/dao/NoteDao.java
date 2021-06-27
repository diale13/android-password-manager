package com.example.ispass.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ispass.models.entities.Note;
import com.example.ispass.models.entities.User;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(Note note);

    @Delete
    public void delete(Note note);

    @Query("SELECT * FROM notes_table WHERE user_identifier = :user_identifier")
    List<Note> getUserNotes(Integer user_identifier);

    @Query("SELECT * FROM notes_table WHERE user_identifier = :user_identifier and id = :id")
    Note getUserNoteById(Integer user_identifier, Integer id);

    @Query("SELECT * FROM notes_table ORDER BY id ASC")
    public List<Note> getAll();

}

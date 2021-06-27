package com.example.ispass.models.entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

@Entity(tableName = "notes_table",
        indices = {@Index(value = "id", unique = true)})
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Integer id;

    @NonNull
    @ColumnInfo(name = "user_identifier")
    public Integer user_identifier;

    @NonNull
    @ColumnInfo(name = "title")
    public String title;

    @NonNull
    @ColumnInfo(name = "content")
    public String content;

    public Note(Note note) {
        this.id = note.id;
        this.user_identifier = note.user_identifier;
        this.title = note.title;
        this.content = note.content;
    }

    public Note(){}
}

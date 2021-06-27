package com.example.ispass.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.NoteDao;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.Note;
import com.example.ispass.models.entities.User;
import com.example.ispass.util.Encryption;


public class AddNoteActivity extends AppCompatActivity {

    private EditText title, description;
    private Button addNote;
    private NoteDao noteDao;
    private UserDao userDao;
    private Integer user_id;
    private Encryption enc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        noteDao = Room.databaseBuilder(this, IspDb.class, "notes_table").allowMainThreadQueries().build().noteDao();
        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        addNote = findViewById(R.id.btn_add_note);
        user_id = (Integer) getIntent().getSerializableExtra("user_id");
        enc = new Encryption();
        User user = userDao.getUserById(user_id);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(description.getText().toString())){

                    try {
                        String text = enc.encrypt(description.getText().toString(), user.password);
                        Note newNote = new Note();
                        newNote.title = title.getText().toString();
                        newNote.content = text;
                        newNote.user_identifier = user_id;
                        noteDao.insert(newNote);

                        Intent intent = new Intent(AddNoteActivity.this,NotesActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        Toast.makeText(AddNoteActivity.this, "Encryption Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(AddNoteActivity.this, "Both fields required", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
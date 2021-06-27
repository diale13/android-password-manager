package com.example.ispass.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.NoteDao;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.Note;
import com.example.ispass.models.entities.User;
import com.example.ispass.util.Encryption;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class UpdateNoteActivity extends AppCompatActivity {

    private EditText title, content;
    private Button updateNotes, decryptNote;
    private Integer user_id, note_id;
    private Note note;
    private String decryptText;
    private NoteDao noteDao;
    private UserDao userDao;
    private Encryption enc;
    private Boolean decrypted;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notes);

        title=findViewById(R.id.title);
        content=findViewById(R.id.description);
        updateNotes=findViewById(R.id.updateNote);
        decryptNote=findViewById(R.id.decrypt_note_content);
        decrypted = false;

        noteDao = Room.databaseBuilder(this, IspDb.class, "notes_table").allowMainThreadQueries().build().noteDao();
        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();

        enc = new Encryption();

        Intent i = getIntent();

        note_id = i.getIntExtra("note_id", 0);
        user_id = i.getIntExtra("user_id", 0);
        User user = userDao.getUserById(user_id);
        title.setText(i.getStringExtra("title"));
        content.setText(i.getStringExtra("content"));
        content.setEnabled(false);

        decryptNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        updateNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(content.getText().toString()))
                {
                    try {
                        if (decrypted){
                            saveData(user);
                        }
                        else {
                            decryptText = i.getStringExtra("content");
                            decryptText = enc.decrypt(decryptText, user.password);
                            content.setText(decryptText);
                            saveData(user);
                        }

                    } catch (Exception e) {
                        Toast.makeText(UpdateNoteActivity.this, "Encryption Error" + e.toString(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(UpdateNoteActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        executor = ContextCompat.getMainExecutor(UpdateNoteActivity.this);
        biometricPrompt = new BiometricPrompt((FragmentActivity) UpdateNoteActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(UpdateNoteActivity.this, errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                try {
                    decryptText = i.getStringExtra("content");
                    decryptText = enc.decrypt(decryptText, user.password);
                    content.setText(decryptText);
                    decrypted = true;
                    decryptNote.setEnabled(false);
                    content.setEnabled(true);

                } catch (Exception e) {
                    Toast.makeText(UpdateNoteActivity.this, "Exception" + e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(UpdateNoteActivity.this, "Authentication failed, try again", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication")
                .setSubtitle("Check note using fingerprint authentication")
                .setNegativeButtonText("Close")
                .build();

    }

    private void saveData(User user) throws Exception {
        String text = enc.encrypt(content.getText().toString(), user.password);
        content.setText(text);
        note = noteDao.getUserNoteById(user_id, note_id);
        note.title = title.getText().toString();
        note.content = text;
        noteDao.update(note);

        Intent i = new Intent(UpdateNoteActivity.this,NotesActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("user_id", user_id);
        startActivity(i);
        finish();
    }
}
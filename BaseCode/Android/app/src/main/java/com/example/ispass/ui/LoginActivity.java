package com.example.ispass.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button buttonSignUp, buttonLogin;
    private User user;
    private UserDao userDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Analytics Event
//        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundle = new Bundle();
//        Log.w("SavedInstance: ", String.valueOf(savedInstanceState));
//        savedInstanceState.putString("message", "Integration of firebase completed"); // savedInstanceState es null
//        analytics.logEvent("InitScreen", bundle);

        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        editEmail = findViewById(R.id.et_email);
        editPassword = findViewById(R.id.et_password);
        buttonSignUp = findViewById(R.id.btn_sign_up_intro);
        buttonLogin = findViewById(R.id.btn_log_in_intro);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

       buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Empty email, please try again", Toast.LENGTH_SHORT).show();
                } else {
                    user = userDao.getUserByEmail(email);
                    try {
                        user = userDao.getUserByEmail(email);
                        if (user != null) {
                            Intent i = new Intent(LoginActivity.this, AuthenticationActivity.class);
                            i.putExtra("user", user);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Unregistered user", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
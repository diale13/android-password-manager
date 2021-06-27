package com.example.ispass.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import static java.sql.DriverManager.println;

public class HomeActivity extends AppCompatActivity {

    private TextView UserWelcome;
    private ImageView buttonLogOut, buttonScanQr, buttonProfile;
    private Integer pressed;
    private Integer userId;
    private UserDao userDao;
    private User user;
    private Button buttonPasswords, buttonAnalyzeSecurity, buttonExportPassword, buttonNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pressed = 0;
        buttonNotes = findViewById(R.id.btn_manage_notes);
        buttonPasswords = findViewById(R.id.btn_manage_passwords);
        buttonAnalyzeSecurity = findViewById(R.id.btn_analyze_security);
        buttonExportPassword = findViewById(R.id.btn_export_passwords);
        buttonScanQr = findViewById(R.id.toolbar_home_scan);
        buttonLogOut = findViewById(R.id.toolbar_home_exit);
        buttonProfile = findViewById(R.id.toolbar_home_profile);
        userId = (Integer) getIntent().getSerializableExtra("user_id");
        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        UserWelcome = findViewById(R.id.et_welcome);

        user = userDao.getUserById(userId);

        if (user != null) {
            UserWelcome.setText("WELCOME " + user.username.toUpperCase());
        }

        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                i.putExtra("user_id", user.id);
                startActivity(i);
                finish();
            }
        });

        buttonScanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ScanActivity.class);
                i.putExtra("user_id", user.id);
                startActivity(i);
                finish();
            }
        });

        buttonPasswords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, PasswordsActivity.class);
                i.putExtra("user_id", user.id);
                startActivity(i);
                finish();
            }
        });

        buttonNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, NotesActivity.class);
                i.putExtra("user_id", user.id);
                startActivity(i);
                finish();
            }
        });

        buttonAnalyzeSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, AnalyzeSecurityActivity.class);
                i.putExtra("user_id", user.id);
                startActivity(i);
                finish();
            }
        });

        buttonExportPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ExportPasswordsActivity.class);
                i.putExtra("user_id", user.id);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        pressed++;
        Toast.makeText(this, "If press 3 times, the app will close", Toast.LENGTH_LONG).show();
        if (pressed > 2) {
            super.onBackPressed();
        }
    }



}


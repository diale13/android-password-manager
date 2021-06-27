package com.example.ispass.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class ProfileActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword;
    private ImageView home, logout;
    private Button buttonSeePassword;
    private UserDao userDao;
    private Integer userId;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        editTextUsername = findViewById(R.id.et_username);
        editTextUsername.setKeyListener(null);
        editTextEmail = findViewById(R.id.et_email);
        editTextEmail.setKeyListener(null);
        editTextPassword = findViewById(R.id.et_password);
        editTextPassword.setEnabled(false);
        buttonSeePassword = findViewById(R.id.btn_see_password_up);
        home = findViewById(R.id.toolbar_profile_home);
        logout = findViewById(R.id.toolbar_profile_exit);

        userId = (Integer) getIntent().getSerializableExtra("user_id");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        User user = userDao.getUserById(userId);

        editTextUsername.setText(user.username);
        editTextEmail.setText(user.email);
        editTextPassword.setText(user.password);

        buttonSeePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        executor = ContextCompat.getMainExecutor(ProfileActivity.this);

        biometricPrompt = new BiometricPrompt(ProfileActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(ProfileActivity.this, errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                    editTextPassword.setInputType(1);
                    editTextPassword.setKeyListener(null);
                    buttonSeePassword.setVisibility(View.GONE);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(ProfileActivity.this, "Authentication failed, try again", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication")
                .setSubtitle("Check note using fingerprint authentication")
                .setNegativeButtonText("Close")
                .build();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
        i.putExtra("user_id", userId);
        startActivity(i);
        finish();
    }
}
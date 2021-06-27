package com.example.ispass.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.User;
import com.example.ispass.webApiServices.SessionService;
import com.example.ispass.webapiModels.in.LoginModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {

    private EditText editPassword;
    private UserDao userDao;
    private Button buttonPassword, buttonFingerPrint;
    private User user;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private SessionService sessionService;


    private void webApiLogIn(User user) {
        LoginModel loginModel = new LoginModel();
        loginModel.Email = user.email;
        loginModel.Password = user.password;
        Call<String> call = sessionService.LoginUser(loginModel);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    try {
                        String errorContent = response.errorBody().string();
                        Toast.makeText(AuthenticationActivity.this, errorContent, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    String token = response.body();
                    SharedPreferences preferences = getSharedPreferences("App_data", Context.MODE_PRIVATE);
                    preferences.edit().putString("Authorization_Token", token).apply();
                    Intent i = new Intent(AuthenticationActivity.this, HomeActivity.class);
                    i.putExtra("user_id", user.id);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AuthenticationActivity.this, "Server down you may be restricted from using some functionalities", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        sessionService = new SessionService();
        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();

        user = (User) getIntent().getSerializableExtra("user");
        String email = user.email;
        editPassword = findViewById(R.id.et_password);
        buttonPassword = findViewById(R.id.btn_log_in_password);
        buttonFingerPrint = findViewById(R.id.btn_log_in_fingerprint);

        buttonPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    Toast.makeText(AuthenticationActivity.this, "Empty password, please try again", Toast.LENGTH_SHORT).show();
                } else {
                    User user = userDao.getUserLogin(email, password);
                    if (user == null) {
                        Toast.makeText(AuthenticationActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    } else {
                        webApiLogIn(user);
                        Intent i = new Intent(AuthenticationActivity.this, HomeActivity.class);
                        i.putExtra("user_id", user.id);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(AuthenticationActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(AuthenticationActivity.this, errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                User user = userDao.getUserByEmail(email);
                webApiLogIn(user);
                Intent i = new Intent(AuthenticationActivity.this, HomeActivity.class);
                i.putExtra("user_id", user.id);
                startActivity(i);
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(AuthenticationActivity.this, "Authentication failed, try again", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Login using finger print authentication")
                .setNegativeButtonText("Use Password")
                .build();


        buttonFingerPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AuthenticationActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
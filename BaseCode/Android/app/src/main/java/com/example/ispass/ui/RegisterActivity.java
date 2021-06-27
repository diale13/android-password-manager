package com.example.ispass.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.User;
import com.example.ispass.webApiServices.UserService;
import com.example.ispass.webapiModels.in.RegisterModel;
import com.example.ispass.webapiModels.out.UserModelOut;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;
    private UserDao userDao;

    private UserService userService;
    private Context thisContext;

    private static final String TAG = "MainActivity";
    private String tokenDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        notification();

        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        editTextUsername = findViewById(R.id.et_username);
        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);
        editTextConfirmPassword = findViewById(R.id.et_confirmPassword);
        buttonRegister = findViewById(R.id.btn_sign_up);

        userService = new UserService();
        thisContext = this;

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String passwordConf = editTextConfirmPassword.getText().toString().trim();

                if (password.equals(passwordConf)) {
                    User newUser = new User();
                    newUser.username = userName;
                    newUser.email = email;
                    newUser.password = password;
                    newUser.deviceId = tokenDevice;

                    RegisterModel webApiUserModel = new RegisterModel(newUser, thisContext);
                    userService.CreateUser(webApiUserModel);
                    Call<UserModelOut> call = userService.CreateUser(webApiUserModel);
                    call.enqueue(new Callback<UserModelOut>() {
                        @Override
                        public void onResponse(Call<UserModelOut> call, Response<UserModelOut> response) {
                            if (!response.isSuccessful()) {
                                try {
                                    String errorContent = response.errorBody().string();
                                    Toast.makeText(thisContext, errorContent, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                userDao.insert(newUser);
                                Toast.makeText(RegisterActivity.this, "User created successfully.", Toast.LENGTH_LONG).show();
                                Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(moveToLogin);
                            }

                        }

                        @Override
                        public void onFailure(Call<UserModelOut> call, Throwable t) {
                            Toast.makeText(RegisterActivity.this, t.getMessage(), Toast.LENGTH_LONG);
                        }
                    });

                } else {
                    Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    public void notification() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                String token = task.getResult();
                Log.w("Token of device:", token);
                tokenDevice = token;

                String msg = String.format(token, R.string.msg_token_fmt);
                Log.d(TAG, msg);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.PasswordDao;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.Password;
import com.example.ispass.models.entities.User;
import com.example.ispass.util.Encryption;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText name, password;
    private Button updatePassword, decryptPassword;
    private Integer user_id, password_id;
    private PasswordDao passwordDao;
    private UserDao userDao;
    private User user;
    private Password pass;
    private String decryptText;
    private Encryption enc;
    private Boolean decrypted;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        passwordDao = Room.databaseBuilder(this, IspDb.class, "passwords_table").allowMainThreadQueries().build().passwordDao();
        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        name = findViewById(R.id.password_title);
        password = findViewById(R.id.password_description);
        updatePassword = findViewById(R.id.update_password_button);
        enc = new Encryption();

        decryptPassword=findViewById(R.id.decrypt_password_content);
        decrypted = false;

        Intent i = getIntent();
        user_id = i.getIntExtra("user_id", 0);
        user = userDao.getUserById(user_id);
        password_id = i.getIntExtra("password_id", 0);
        name.setText(i.getStringExtra("password_name"));
        password.setText(i.getStringExtra("password"));
        password.setEnabled(false);


        decryptPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(password.getText().toString()))
                {
                    try {
                        if (decrypted) {
                            saveData(user);
                        }
                        else {
                            decryptText = enc.decrypt(password.getText().toString(), user.password);
                            password.setText(decryptText);
                            saveData(user);
                        }

                    } catch (Exception e) {
                        Toast.makeText(UpdatePasswordActivity.this, "Encryption Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else
                {
                    Toast.makeText(UpdatePasswordActivity.this, "Both Fields Required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        executor = ContextCompat.getMainExecutor(UpdatePasswordActivity.this);
        biometricPrompt = new BiometricPrompt((FragmentActivity) UpdatePasswordActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(UpdatePasswordActivity.this, errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                try {
                    decryptText = enc.decrypt(password.getText().toString(), user.password);
                    password.setText(decryptText);
                    decrypted = true;
                    decryptPassword.setEnabled(false);
                    password.setEnabled(true);

                } catch (Exception e) {
                    Toast.makeText(UpdatePasswordActivity.this, "Exception" + e.toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(UpdatePasswordActivity.this, "Authentication failed, try again", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication")
                .setSubtitle("Check password using fingerprint authentication")
                .setNegativeButtonText("Close")
                .build();
    }

    private void saveData(User user) throws Exception {

        String text = enc.encrypt(password.getText().toString(), user.password);
        pass = passwordDao.getUserPasswordById(user_id, password_id);
        pass.name = name.getText().toString();
        pass.password = text;
        passwordDao.update(pass);

        Intent i=new Intent(UpdatePasswordActivity.this,PasswordsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("user_id", user_id);
        startActivity(i);
        finish();

    }
}
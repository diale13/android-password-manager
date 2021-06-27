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
import com.example.ispass.database.dao.PasswordDao;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.Password;
import com.example.ispass.models.entities.User;
import com.example.ispass.util.Encryption;

import java.nio.charset.Charset;
import java.util.Random;

public class AddPasswordActivity extends AppCompatActivity {

    private EditText name, password;
    private Button addPassword, generatePassword;
    private PasswordDao passwordDao;
    private UserDao userDao;
    private User user;
    private Integer user_id;
    private String password_qr;
    private Encryption enc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_password);

        passwordDao = Room.databaseBuilder(this, IspDb.class, "passwords_table").allowMainThreadQueries().build().passwordDao();
        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        name = findViewById(R.id.password_title);
        password = findViewById(R.id.password_description);
        addPassword = findViewById(R.id.add_password_button);
        generatePassword = findViewById(R.id.generate_password_button);
        password_qr = (String) getIntent().getSerializableExtra("password_qr");
        password.setText(password_qr);
        user_id = (Integer) getIntent().getSerializableExtra("user_id");
        user = userDao.getUserById(user_id);
        enc = new Encryption();

        generatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int leftLimit = 48; // numeral '0'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 20;
                Random random = new Random();

                String generatedPassword = random.ints(leftLimit, rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();


                password.setText(generatedPassword);
            }
        });

        addPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(name.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){

                    try {
                        String text = enc.encrypt(password.getText().toString(), user.password);
                        Password newPassword = new Password();
                        newPassword.name = name.getText().toString();
                        newPassword.password = text;
                        newPassword.user_identifier = user_id;
                        passwordDao.insert(newPassword);

                        Intent intent = new Intent(AddPasswordActivity.this,PasswordsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("user_id", user_id);
                        startActivity(intent);
                        finish();

                    } catch (Exception e) {
                        Toast.makeText(AddPasswordActivity.this, "Encryption Error" + e.toString(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(AddPasswordActivity.this, "Both fields required", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
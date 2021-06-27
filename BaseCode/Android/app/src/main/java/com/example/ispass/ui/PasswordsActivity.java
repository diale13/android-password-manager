package com.example.ispass.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.database.IspDb;
import com.example.ispass.database.dao.PasswordDao;
import com.example.ispass.database.dao.UserDao;
import com.example.ispass.models.entities.Password;
import com.example.ispass.models.entities.User;
import com.example.ispass.ui.adapters.PasswordAdapter;
import com.example.ispass.util.Encryption;
import com.example.ispass.webApiServices.UserService;
import com.example.ispass.webapiModels.in.AccountModel;
import com.example.ispass.webapiModels.in.UserAccountUpdateModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView buttonExit, buttonHome, buttonScan, buttonCloud;
    private Button addPassword;
    private List<Password> passwordList;
    private PasswordDao passwordDao;
    private UserDao userDao;
    private PasswordAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private User user;
    private Integer userId;
    private UserAccountUpdateModel userAccountUpdateModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        buttonExit = findViewById(R.id.toolbar_password_exit);
        buttonHome = findViewById(R.id.toolbar_password_home);
        buttonScan = findViewById(R.id.toolbar_password_scan);
        buttonCloud = findViewById(R.id.toolbar_password_cloud);
        addPassword = findViewById(R.id.btn_add_password);
        recyclerView = findViewById(R.id.recyclerPassword_view);
        coordinatorLayout = findViewById(R.id.layout_password_main);
        passwordDao = Room.databaseBuilder(this, IspDb.class, "passwords_table").allowMainThreadQueries().build().passwordDao();
        userDao = Room.databaseBuilder(this, IspDb.class, "users_table").allowMainThreadQueries().build().userDao();
        userId = (Integer) getIntent().getSerializableExtra("user_id");
        user = userDao.getUserById(userId);
        passwordList = new ArrayList<>();
        userAccountUpdateModel = new UserAccountUpdateModel();

        fetchAllPasswordsFromDatabase(userId);

        adapter = new PasswordAdapter(this,PasswordsActivity.this, passwordList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PasswordsActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PasswordsActivity.this, HomeActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
                finish();
            }
        });
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PasswordsActivity.this, ScanActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
                finish();
            }
        });
        addPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PasswordsActivity.this, AddPasswordActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
            }
        });
        buttonCloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertAllPasswordsToModel(passwordList, user);
                SharedPreferences preferences = getSharedPreferences("App_data", Context.MODE_PRIVATE);
                String token = preferences.getString("Authorization_Token", "");
                UserService userService = new UserService();
                Call<String> call = userService.UpdateUser(userAccountUpdateModel, token);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response <String> response) {
                        if (!response.isSuccessful()) {
                            try {
                                String errorContent = response.errorBody().string();
                                Toast.makeText(PasswordsActivity.this, errorContent, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(PasswordsActivity.this, "Contraseñas agregegadas a la nube correctamente", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Fail", "Failed: " + t.getMessage());
                        Toast.makeText(PasswordsActivity.this, "Verifique conexion a internet para subir su archivo", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void fetchAllPasswordsFromDatabase(Integer userId) {
        List<Password> list = passwordDao.getUserPasswords(userId);
        if (list.size() == 0){
            Toast.makeText(this, "No passwords saved", Toast.LENGTH_SHORT).show();
        }
        else {
            passwordList = list;
        }
    }

    private void convertAllPasswordsToModel(List<Password> passwordList, User user){
        Encryption enc = new Encryption();
        List<AccountModel> accountModelList = new ArrayList<>();
        String decryptedPassword = "";
        for (Password i : passwordList) {
            try {
                decryptedPassword = enc.decrypt(i.password, user.password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            AccountModel accountModel = new AccountModel();
            accountModel.Email = user.email;
            accountModel.Site = i.name;
            accountModel.UserName = user.username;
            accountModel.Password = decryptedPassword;
            accountModelList.add(accountModel);
        }
        userAccountUpdateModel.AccountModels = accountModelList;
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Password item = adapter.getList().get(position);

            adapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item Deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(item, position);
                    recyclerView.scrollToPosition(position);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if (!(event == DISMISS_EVENT_ACTION)) {
                        passwordDao.delete(item);
                    }
                }

            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    };

    @Override
    public void onBackPressed() {
        Intent i = new Intent(PasswordsActivity.this, HomeActivity.class);
        i.putExtra("user_id", userId);
        startActivity(i);
        finish();
    }
}
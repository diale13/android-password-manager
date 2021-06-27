package com.example.ispass.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.webApiServices.LeaksService;
import com.example.ispass.webapiModels.out.LeakOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyzeSecurityActivity extends AppCompatActivity {

    private Button buttonAnalyzeAll, buttonAnalyzeRecent;
    private Integer userId;
    private LeaksService leaksService;
    private ArrayList<LeakOut> foundLeaks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_security);

        userId = (Integer) getIntent().getSerializableExtra("user_id");
        buttonAnalyzeAll = findViewById(R.id.btn_analyze_all);
        buttonAnalyzeRecent = findViewById(R.id.btn_analyze_recent);
        leaksService = new LeaksService();

        buttonAnalyzeRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("App_data", Context.MODE_PRIVATE);
                String token = preferences.getString("Authorization_Token", "");
                Call<List<LeakOut>> call = leaksService.GetNewLeaks(token);
                call.enqueue(new Callback<List<LeakOut>>() {
                    @Override
                    public void onResponse(Call<List<LeakOut>> call, Response<List<LeakOut>> response) {
                        if (!response.isSuccessful()) {
                            try {
                                String errorContent = response.errorBody().string();
                                Toast.makeText(AnalyzeSecurityActivity.this, errorContent, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            foundLeaks = new ArrayList<>(response.body());
                            Intent i = new Intent(AnalyzeSecurityActivity.this, LeaksActivity.class);
                            i.putExtra("leaks_arrayList", foundLeaks);
                            i.putExtra("user_id", userId);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LeakOut>> call, Throwable t) {
                        Toast.makeText(AnalyzeSecurityActivity.this, "Verifique conexion a internet", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        buttonAnalyzeAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("App_data", Context.MODE_PRIVATE);
                String token = preferences.getString("Authorization_Token", "");
                Call<List<LeakOut>> call = leaksService.GetAllLeaks(token);
                call.enqueue(new Callback<List<LeakOut>>() {
                    @Override
                    public void onResponse(Call<List<LeakOut>> call, Response<List<LeakOut>> response) {
                        if (!response.isSuccessful()) {
                            try {
                                String errorContent = response.errorBody().string();
                                Toast.makeText(AnalyzeSecurityActivity.this, errorContent, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            foundLeaks = new ArrayList<>(response.body());
                            Intent i = new Intent(AnalyzeSecurityActivity.this, LeaksActivity.class);
                            i.putExtra("leaks_arrayList", foundLeaks);
                            i.putExtra("user_id", userId);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LeakOut>> call, Throwable t) {
                        Toast.makeText(AnalyzeSecurityActivity.this, "Verify your internet connection", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ImageView buttonHome = findViewById(R.id.toolbar_analyze_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnalyzeSecurityActivity.this, HomeActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
                finish();
            }
        });
        ImageView buttonExit = findViewById(R.id.toolbar_analyze_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AnalyzeSecurityActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(AnalyzeSecurityActivity.this, HomeActivity.class);
        i.putExtra("user_id", userId);
        startActivity(i);
        finish();
    }
}
package com.example.ispass.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.ui.adapters.LeaksAdapter;
import com.example.ispass.ui.adapters.NotesAdapter;
import com.example.ispass.webapiModels.out.LeakOut;

import java.util.ArrayList;
import java.util.List;

public class LeaksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<LeakOut> leaksList;
    private LeaksAdapter adapter;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaks);

        userId = (Integer) getIntent().getSerializableExtra("user_id");
        leaksList = getIntent().getExtras().getParcelableArrayList("leaks_arrayList");
        recyclerView = findViewById(R.id.recyclerLeaks_view);
        ImageView buttonHome = findViewById(R.id.toolbar_leaks_home);
        ImageView buttonExit = findViewById(R.id.toolbar_leaks_exit);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeaksAdapter(this,LeaksActivity.this, leaksList);
        recyclerView.setAdapter(adapter);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LeaksActivity.this, AnalyzeSecurityActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
                finish();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LeaksActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(LeaksActivity.this, AnalyzeSecurityActivity.class);
        i.putExtra("user_id", userId);
        startActivity(i);
        finish();
    }
}
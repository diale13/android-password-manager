package com.example.ispass.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ispass.R;
import com.example.ispass.webApiServices.ExportListService;
import com.example.ispass.webapiModels.out.ExportOut;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExportPasswordsActivity extends AppCompatActivity {

    private Button buttonExport;
    private ImageView buttonExit, buttonHome;
    private Integer userId;
    private ExportListService exportListService;
    private String PATH = Environment.getExternalStorageDirectory() + "/download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_passwords);

        userId = (Integer) getIntent().getSerializableExtra("user_id");
        buttonExport = findViewById(R.id.btn_export);
        exportListService = new ExportListService();


        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("App_data", Context.MODE_PRIVATE);
                String token = preferences.getString("Authorization_Token", "");
                Call<String> call = exportListService.GetExportedZip(token);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (!response.isSuccessful()) {
                            try {
                                String errorContent = response.errorBody().string();
                                Toast.makeText(ExportPasswordsActivity.this, errorContent, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String base64File = response.body();
                            convertFile(base64File);
                            Toast.makeText(ExportPasswordsActivity.this, "Sus contraseñas fueron importadas exitosamente, se encuentran en el archivo de descargas"
                                    , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("Fail", "Failed: " + t.getMessage());
                        Toast.makeText(ExportPasswordsActivity.this, "Verifique conexion a internet para obtener su archivo", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        buttonHome = findViewById(R.id.toolbar_export_home);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExportPasswordsActivity.this, HomeActivity.class);
                i.putExtra("user_id", userId);
                startActivity(i);
                finish();
            }
        });
        buttonExit = findViewById(R.id.toolbar_export_exit);
        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ExportPasswordsActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void convertFile(String base64File) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        FileOutputStream fos = null;
        try {
            if (base64File != null) {

                File dir = new File(PATH);
                dir.mkdirs();
                File outputFile = new File(dir, "passwords.zip");
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                }
                fos = new FileOutputStream(outputFile);
                byte[] decodedString = android.util.Base64.decode(base64File, android.util.Base64.DEFAULT);
                fos.write(decodedString);
                fos.flush();
                fos.close();
                Toast.makeText(getBaseContext(), "file saved", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("Fail", "Failed: " + "File conversion error " +  e.getMessage());
            Toast.makeText(ExportPasswordsActivity.this, "Hubo un problema exportando su archivo", Toast.LENGTH_LONG).show();
        } finally {
            if (fos != null) {
                fos = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ExportPasswordsActivity.this, HomeActivity.class);
        i.putExtra("user_id", userId);
        startActivity(i);
        finish();
    }
}
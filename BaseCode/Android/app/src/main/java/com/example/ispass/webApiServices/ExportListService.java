package com.example.ispass.webApiServices;

import com.example.ispass.interfaces.ExportsInterfaceAPI;
import com.example.ispass.webapiModels.out.ExportOut;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExportListService {
    public ExportsInterfaceAPI exportsInterfaceAPI;

    public ExportListService() {
        RetroCreateExports();
    }

    public Call<String> GetExportedZip(String token) {
        Call<String> zip = exportsInterfaceAPI.GetExportedZip(token);
        return zip;
    }

    private void RetroCreateExports() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(IpManager.getInstance().LOCAL_IP)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        exportsInterfaceAPI = retrofit.create(ExportsInterfaceAPI.class);
    }


}

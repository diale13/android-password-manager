package com.example.ispass.webApiServices;

import com.example.ispass.interfaces.LeakInterfaceAPI;
import com.example.ispass.webapiModels.out.LeakOut;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LeaksService {

    public LeakInterfaceAPI leakInterfaceAPI;

    public LeaksService() {
        RetroCreateLeaks();
    }

    public Call<List<LeakOut>> GetAllLeaks(String token) {
        Call<List<LeakOut>> allLeaks = leakInterfaceAPI.GetAllLeaks(token);
        return allLeaks;
    }

    public Call<List<LeakOut>> GetNewLeaks(String token) {
        Call<List<LeakOut>> newLeaks = leakInterfaceAPI.GetNewLeaks(token);
        return newLeaks;
    }

    private void RetroCreateLeaks() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()

                .baseUrl(IpManager.getInstance().LOCAL_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        leakInterfaceAPI = retrofit.create(LeakInterfaceAPI.class);
    }
}

package com.example.ispass.webApiServices;

import com.example.ispass.interfaces.SessionInterfaceAPI;
import com.example.ispass.webapiModels.in.LoginModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionService {
    public SessionInterfaceAPI sessionInterfaceAPI;

    public SessionService(){
        RetroCreateSession();
    }

    public Call<String> LoginUser(LoginModel login){
        Call<String> string = sessionInterfaceAPI.Login(login);
        return string;
    }

    private void RetroCreateSession(){
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
        sessionInterfaceAPI = retrofit.create(SessionInterfaceAPI.class);
    }

}

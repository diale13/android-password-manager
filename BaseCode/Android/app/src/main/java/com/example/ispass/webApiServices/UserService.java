package com.example.ispass.webApiServices;

import com.example.ispass.interfaces.UserInterfaceApi;
import com.example.ispass.webapiModels.in.RegisterModel;
import com.example.ispass.webapiModels.in.UserAccountUpdateModel;
import com.example.ispass.webapiModels.out.UserModelOut;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserService {

    public UserInterfaceApi userInterfaceAPI;

    public UserService() {
        RetroCreateUser();
    }

    public Call<UserModelOut> CreateUser(RegisterModel signUpModel) {
        Call<UserModelOut> userModelCall = userInterfaceAPI.CreateUser(signUpModel);
        return userModelCall;
    }

    public Call<String> UpdateUser(UserAccountUpdateModel userModel, String token) {
        Call<String> userModelCall = userInterfaceAPI.UpdateUser(userModel, token);
        return userModelCall;
    }

    private void RetroCreateUser() {
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

        userInterfaceAPI = retrofit.create(UserInterfaceApi.class);
    }


}

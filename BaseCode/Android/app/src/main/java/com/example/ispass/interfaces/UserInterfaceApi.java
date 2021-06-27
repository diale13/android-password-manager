package com.example.ispass.interfaces;

import com.example.ispass.webapiModels.in.RegisterModel;
import com.example.ispass.webapiModels.in.UserAccountUpdateModel;
import com.example.ispass.webapiModels.out.UserModelOut;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface UserInterfaceApi {
    @POST("users")
    Call<UserModelOut> CreateUser(@Body RegisterModel signUp);

    @PUT("users/accounts")
    Call<String> UpdateUser(@Body UserAccountUpdateModel user, @Header("Authorization") String token);
}

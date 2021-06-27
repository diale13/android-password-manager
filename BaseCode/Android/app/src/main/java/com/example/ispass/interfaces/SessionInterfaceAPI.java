package com.example.ispass.interfaces;

import com.example.ispass.webapiModels.in.LoginModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SessionInterfaceAPI {
    @POST("sessions")
    Call<String> Login(@Body LoginModel login);
}

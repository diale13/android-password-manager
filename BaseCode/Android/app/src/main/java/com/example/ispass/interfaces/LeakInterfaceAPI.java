package com.example.ispass.interfaces;

import com.example.ispass.webapiModels.out.LeakOut;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;


public interface LeakInterfaceAPI {
    @GET("leaks/new")
    Call<List<LeakOut>> GetNewLeaks(@Header("Authorization") String token);

    @GET("leaks/all")
    Call<List<LeakOut>> GetAllLeaks(@Header("Authorization") String token);
}

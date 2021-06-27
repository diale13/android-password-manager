package com.example.ispass.interfaces;

import com.example.ispass.webapiModels.out.ExportOut;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface ExportsInterfaceAPI {
    @GET("exportedLists/zip")
    Call<String> GetExportedZip(@Header("Authorization") String token);
}

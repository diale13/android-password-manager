package com.example.ispass.webapiModels.in;

import android.content.Context;
import android.provider.Settings;

import com.example.ispass.models.entities.User;
import com.google.gson.annotations.SerializedName;

import android.provider.Settings.Secure;

import java.util.ArrayList;
import java.util.List;

public class RegisterModel {
    @SerializedName("DeviceId")
    public String DeviceId;

    @SerializedName("AccountModels")
    public List<AccountModel> AccountModels;

    @SerializedName("MainEmail")
    public String MainEmail;

    @SerializedName("Password")
    public String Password;

    public RegisterModel(User newUser, Context context) {
        this.DeviceId = newUser.deviceId;
        this.MainEmail = newUser.email;
        this.Password = newUser.password;
        this.AccountModels = ReturnDefaultList();
    }

    private List<AccountModel> ReturnDefaultList() {
        return new ArrayList<AccountModel>();
    }
}

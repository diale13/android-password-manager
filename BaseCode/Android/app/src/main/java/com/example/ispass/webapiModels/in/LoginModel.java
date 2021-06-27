package com.example.ispass.webapiModels.in;

import com.google.gson.annotations.SerializedName;

public class LoginModel {
    @SerializedName("Email")
    public String Email;

    @SerializedName("Password")
    public String Password;
}

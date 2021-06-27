package com.example.ispass.webapiModels.in;

import com.google.gson.annotations.SerializedName;

public class AccountModel {

    @SerializedName("UserName")
    public String UserName;

    @SerializedName("Password")
    public String Password;

    @SerializedName("Site")
    public String Site;

    @SerializedName("Email")
    public String Email;

    public AccountModel() {
        this.Email = "";
        this.Password = "";
        this.Site = "";
        this.UserName = "";
    }
}

package com.example.ispass.webapiModels.in;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserAccountUpdateModel {

    @SerializedName("AccountModels")
    public List<AccountModel> AccountModels;
}

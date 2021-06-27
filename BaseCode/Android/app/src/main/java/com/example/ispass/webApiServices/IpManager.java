package com.example.ispass.webApiServices;

public class IpManager {
    private static IpManager mInstance= null;
    public String LOCAL_IP = "http://10.0.2.2:5000/api/";

    protected IpManager(){}

    public static synchronized IpManager getInstance() {
        if(null == mInstance){
            mInstance = new IpManager();
        }
        return mInstance;
    }
}

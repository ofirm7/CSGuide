package com.example.csguide;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences mySharedPrefrences;


    public SharedPref(Context context) {
        mySharedPrefrences = context.getSharedPreferences("filename", context.MODE_PRIVATE);
    }

    public void SetUsername(String username) {
        SharedPreferences.Editor editor = mySharedPrefrences.edit();
        editor.putString("username", username);
        editor.commit();
    }

    public String GetUsername() {
        String user = mySharedPrefrences.getString("username", "YouRGuest");
        return user;
    }

    public String GetPhoneNumber() {
        return mySharedPrefrences.getString("phoneNumber", "YouRGuest");
    }

}

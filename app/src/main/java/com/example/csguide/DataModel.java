package com.example.csguide;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DataModel {

    static public ArrayList<User> users = new ArrayList<>();
    static public ArrayList<CSItem> csItems= new ArrayList<>();

    public static void usersSave()
    {
        FirebaseDatabase.getInstance().getReference("users").setValue(DataModel.users);

    }

    public static void csItemsSave()
    {
        FirebaseDatabase.getInstance().getReference("csItems").setValue(DataModel.csItems);

    }

}

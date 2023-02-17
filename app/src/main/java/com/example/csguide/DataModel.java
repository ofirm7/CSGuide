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

    //put into the firebase the changes in Class User
    public static void usersUpdate()
    {
        User tempUser;
        for (int i = 0; i < users.size(); i++) {
            users.set(i, new User(users.get(i).getUsername(), users.get(i).getPassword(), users.get(i).getEmail(), users.get(i).getPhoneNumber()));
        }
        usersSave();
    }

}

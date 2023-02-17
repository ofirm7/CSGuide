package com.example.csguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoadingActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbUsersRef, dbCSItemsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        dbUsersRef = database.getReference("users");
        dbCSItemsRef = database.getReference("csItems");

        dbCSItemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<CSItem>> t = new GenericTypeIndicator<ArrayList<CSItem>>() {
                };
                ArrayList<CSItem> fbCSItems = dataSnapshot.getValue(t);
                DataModel.csItems.clear();
                DataModel.csItems.addAll(fbCSItems);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("CSGuide", "Failed to read value.", error.toException());
            }
        });

        dbUsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GenericTypeIndicator<ArrayList<User>> t = new GenericTypeIndicator<ArrayList<User>>() {
                };
                ArrayList<User> fbUsers = dataSnapshot.getValue(t);
                DataModel.users.clear();
                DataModel.users.addAll(fbUsers);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("CSGuide", "Failed to read value.", error.toException());
            }
        });
    }
}
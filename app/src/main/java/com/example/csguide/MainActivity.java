package com.example.csguide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    SharedPref sharedPref;
    AlertDialog.Builder builder;

    ImageButton addCSItemIBtn;
    Dialog addDialog;
    EditText nameOfCSItem;
    Button addCSItem;

    ListView listView;
    List list = new ArrayList();
    EditText searchedTextFlag;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPref = new SharedPref(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        builder = new AlertDialog.Builder(this);

        addCSItemIBtn = findViewById(R.id.addCSItemIB);
        addCSItemIBtn.setOnClickListener(this);

        listView = findViewById(R.id.listViewTry);
        searchedTextFlag = findViewById(R.id.searchedText);

        createList();
        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        searchedTextFlag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                (MainActivity.this).adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(this);

    }

    public void OpenAddMuscleDialog() {
        addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.custom_dialog_add_csitem);
        addDialog.setTitle("Add Item");

        addDialog.setCancelable(true);

        nameOfCSItem = addDialog.findViewById(R.id.nameOfCSItem);
        addCSItem = addDialog.findViewById(R.id.addCSItemBtn);


        addCSItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == addCSItem)
                {
                    DataModel.csItems.add(new CSItem(nameOfCSItem.getText().toString(), null));
                    DataModel.csItemsSave();
                    addDialog.dismiss();
                    restartapp();
                }
            }
        });

        addDialog.show();
    }

    private void createList()
    {
        /*for (User user: DataModel.users) {
            list.add(user.getUsername());
        }
*/

        for (CSItem csitem: DataModel.csItems) {
            list.add(csitem.getName());
        }
        return;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String choosenCSItemName = listView.getItemAtPosition(position).toString();
        position = list.indexOf(choosenCSItemName);

        Intent intent1 = new Intent(this, CSItemActivity.class);
        intent1.putExtra("CSII", position);
        startActivity(intent1);
        finish();
    }

    @Override
    public void onClick(View view) {
        if (view == addCSItemIBtn)
        {
            OpenAddMuscleDialog();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }

        MenuItem item;
        item = menu.getItem(4);
        item.setEnabled(false);
        item.setVisible(false);

        if (sharedPref.GetUsername().equals("YouRGuest")) {
            item = menu.getItem(0);
            item.setEnabled(false);
            item.setVisible(false);

            item = menu.getItem(3);
            item.setEnabled(false);
            item.setVisible(false);

        } else {

            item = menu.getItem(1);
            item.setEnabled(false);
            item.setVisible(false);

            item = menu.getItem(2);
            item.setEnabled(false);
            item.setVisible(false);

            item = menu.getItem(0);
            item.setTitle(sharedPref.GetUsername());
            //item.setTitle(usernameFromInternalFileString);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 0);
            //Toast.makeText(this,"you selected login",Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_register) {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivityForResult(intent, 0);
            return true;
        } else if (id == R.id.action_exit) {
            builder.setMessage("Do you want to logout?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            sharedPref.SetUsername("YouRGuest");
                            Toast.makeText(getApplicationContext(), "You logged out",
                                    Toast.LENGTH_SHORT).show();
                            restartapp();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();
                            Toast.makeText(getApplicationContext(), "You canceled the logout",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Logout");
            alert.show();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    void restartapp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
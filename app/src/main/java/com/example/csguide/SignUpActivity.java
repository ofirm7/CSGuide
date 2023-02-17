package com.example.csguide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button submit, toLoginFromSignUp;
    EditText email, username, pass, passConfirmation, phoneNumber;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sharedPref = new SharedPref(this);
        phoneNumber = findViewById(R.id.phoneNumberSignUp);
        toLoginFromSignUp = findViewById(R.id.toLoginFromSignUp);

        submit = findViewById(R.id.submitSignUp);
        username = findViewById(R.id.usernameSignUp);
        email = findViewById(R.id.emailSignUp);
        pass = findViewById(R.id.passwordSignUp);
        passConfirmation = findViewById(R.id.passwordConfirmationSignUp);
        submit.setOnClickListener(this);
        toLoginFromSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == submit) {
            if (username.getText().toString().equals("") || pass.getText().toString().equals("")) {
                Toast.makeText(this, "fill in all boxes", Toast.LENGTH_SHORT).show();
            } else if (!pass.getText().toString().equals(passConfirmation.getText().toString())) {
                Toast.makeText(this, "passwords not same", Toast.LENGTH_SHORT).show();
                return;
            } else if (!isEmailValid(email.getText().toString())) {
                Toast.makeText(this, "email is not valid", Toast.LENGTH_SHORT).show();
                return;
            } else if (!isPhoneNumberValid()) {
                Toast.makeText(this, "phone number is not valid", Toast.LENGTH_SHORT).show();
                return;
            } else if (pass.getText().toString().length() < 6 || pass.getText().toString().length() > 18 ||
                    pass.getText().toString().contains(" ")) {
                Toast.makeText(this, "passwords isn't valid", Toast.LENGTH_SHORT).show();
                return;
            } else {

                boolean flagU = false;
                boolean flagE = false;
                boolean flagP = false;
                for (int i = 0; i < DataModel.users.size() && !flagU && !flagE; i++) {
                    if (DataModel.users.get(i).getUsername().equals(username.getText().toString())) {
                        flagU = true;
                    }
                    if (DataModel.users.get(i).getEmail().equals(email.getText().toString())) {
                        flagE = true;
                    }
                    if (DataModel.users.get(i).getPhoneNumber().equals(phoneNumber.getText().toString()))
                        flagP = true;
                }

                if (flagU && flagE) {
                    Toast.makeText(this, "username and email are taken", Toast.LENGTH_SHORT).show();
                    return;
                } else if (flagU) {
                    Toast.makeText(this, "username taken", Toast.LENGTH_SHORT).show();
                    return;
                } else if (flagE) {
                    Toast.makeText(this, "email already used", Toast.LENGTH_SHORT).show();
                    return;
                } else if (flagP) {
                    Toast.makeText(this, "phone number already in use", Toast.LENGTH_SHORT).show();
                    return;
                }

                //DataModel.users.add(new User(username.toString(), pass.toString()));
                DataModel.users.add(new User(username.getText().toString(), pass.getText().toString(), email.getText().toString(),
                        phoneNumber.getText().toString()));
                DataModel.usersSave();
                sharedPref.SetUsername(username.getText().toString(), false);
                finish();
            }
        } else if (view == toLoginFromSignUp) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean isPhoneNumberValid()
    {
        Boolean isValid = false;
        char[] phoneNumberCA = phoneNumber.getText().toString().toCharArray();
        if (phoneNumber.getText().toString().length() != 10 || phoneNumberCA[0] != '0')
        {
            return false;
        }
        else
        {
            for (int i = 1; i < phoneNumber.length(); i++)
            {
                for (int j = 49; j < 59; j++)
                {
                    if (phoneNumberCA[i] == j)
                    {
                        isValid = true;
                        j = 59;
                    }
                }
                if (!isValid)
                    return false;
            }
            return true;
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
        item = menu.getItem(0);
        item.setEnabled(false);
        item.setVisible(false);

        /* shut down login item
        item = menu.getItem(1);
        item.setEnabled(false);
        item.setVisible(false);
        */
        item = menu.getItem(2);
        item.setEnabled(false);
        item.setVisible(false);

        item = menu.getItem(3);
        item.setEnabled(false);
        item.setVisible(false);

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
            sharedPref.SetUsername("YouRGuest", false);
            //Toast.makeText(this,"you sure you want to logout?",Toast.LENGTH_LONG).show();
            restartapp();
            return true;
        } else if (id == R.id.action_GoHome) {
            finish();
            return true;
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    void restartapp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

}
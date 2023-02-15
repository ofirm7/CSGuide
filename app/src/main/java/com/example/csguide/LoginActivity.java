package com.example.csguide;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button submit, forgotPasswordBT, showHideBtn;
    EditText usernameOrEmail, pass;
    SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPref = new SharedPref(this);
        submit = findViewById(R.id.submitLogin);
        forgotPasswordBT = findViewById(R.id.forgotMyPasswordLogin);
        usernameOrEmail = findViewById(R.id.emailOrUsernameLogin);
        pass = findViewById(R.id.passwordLogin);

        showHideBtn = findViewById(R.id.showHideBtn);

        showHideBtn.setOnClickListener(this);
        submit.setOnClickListener(this);
        forgotPasswordBT.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submit) {
            if (usernameOrEmail.getText().toString().equals("") || pass.getText().toString().equals("")) {
                Toast.makeText(this, "fill in all boxes", Toast.LENGTH_SHORT).show();
            } else {
                boolean flag = false;
                int temp = 0;
                for (int i = 0; i < DataModel.users.size() && !flag; i++) {
                    if (DataModel.users.get(i).getUsername().equals(usernameOrEmail.getText().toString())
                            || DataModel.users.get(i).getEmail().equals(usernameOrEmail.getText().toString())) {
                        flag = true;
                        temp = i;
                    }
                }
                if (!flag) {
                    Toast.makeText(this, "No account with that username / email", Toast.LENGTH_SHORT).show();
                } else {
                    if (DataModel.users.get(temp).getPassword().equals(pass.getText().toString())
                            && DataModel.users.get(temp).getUsername().equals(usernameOrEmail.getText().toString())
                            || DataModel.users.get(temp).getPassword().equals(pass.getText().toString())
                            && DataModel.users.get(temp).getEmail().equals(usernameOrEmail.getText().toString())) {
                        sharedPref.SetUsername(DataModel.users.get(temp).getUsername());

                        finish();
                    }
                }
            }
        } else if (view == forgotPasswordBT) {
            if (usernameOrEmail.getText().toString().equals("")) {
                Toast.makeText(this, "fill your username ", Toast.LENGTH_SHORT).show();
            } else if (isUserExist()) {
                /*
                Intent i = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                i.putExtra("CPI", usernameOrEmail.getText().toString());
                i.putExtra("CPI2", getPhoneNumber());
                startActivity(i);
                finish();*/
            } else {
                Toast.makeText(this, "No account with that username / email", Toast.LENGTH_SHORT).show();
            }
        } else if (view == showHideBtn) {
            if (showHideBtn.getText().toString() == "Show") {
                pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showHideBtn.setText("Hide");
            } else {
                pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showHideBtn.setText("Show");
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public boolean isUserExist() {
        boolean flag = false;
        for (int i = 0; i < DataModel.users.size() && !flag; i++) {
            if (DataModel.users.get(i).getUsername().equals(usernameOrEmail.getText().toString())
                    || DataModel.users.get(i).getEmail().equals(usernameOrEmail.getText().toString())) {
                flag = true;
            }
        }
        return flag;
    }

    public String getPhoneNumber() {
        boolean flag = false;
        int temp = 0;
        for (int i = 0; i < DataModel.users.size() && !flag; i++) {
            if (DataModel.users.get(i).getUsername().equals(usernameOrEmail.getText().toString())
                    || DataModel.users.get(i).getEmail().equals(usernameOrEmail.getText().toString())) {
                flag = true;
                temp = i;
            }
        }
        return DataModel.users.get(temp).getPhoneNumber();
    }

    void restartapp() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

}
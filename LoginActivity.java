package com.finalproject_heathbundy;

import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button loginButton;
    private Button newAccount;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //connecting to different layouts/views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        newAccount = findViewById(R.id.newAccount);

        //create database through DataBaseHelper
        db = new DatabaseHelper(this);

        //login button logic
        loginButton.setOnClickListener(v -> {

            //creates email and password strings for use
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            //validation that user has input email and password
            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(
                        LoginActivity.this,
                        "Please Enter Email and Password",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (db.checkLogin(userEmail, userPassword)) {
                Toast.makeText(
                        LoginActivity.this,
                        "Login Was Successful",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent = new Intent(LoginActivity.this, WeightTableActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(
                        LoginActivity.this,
                        "Invalid Email or Password, Please Try Again",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        //Create Account Button Logic
        newAccount.setOnClickListener(v ->{
            Intent intent = new Intent(LoginActivity.this,
                    CreateAccountActivity.class);

            startActivity(intent);
        });
    }
}
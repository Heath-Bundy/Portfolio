package com.finalproject_heathbundy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText createEmail;
    private EditText createPassword;
    private EditText reenterPassword;
    private EditText targetWeight;
    private Button createUser;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //uses create account layout
        setContentView(R.layout.create_account);

        //connect to the different views
        createEmail = findViewById(R.id.createEmail);
        createPassword = findViewById(R.id.createPassword);
        reenterPassword = findViewById(R.id.ReenterPassword);
        targetWeight = findViewById(R.id.targetWeight);
        createUser = findViewById(R.id.createUser);

        //connect to the database helper
        db = new DatabaseHelper(this);

        //create account button logic
        createUser.setOnClickListener(v -> {
            String userEmail = createEmail.getText().toString().trim();
            String userPassword = createPassword.getText().toString().trim();
            String confirmPassword = reenterPassword.getText().toString().trim();
            String weightText = targetWeight.getText().toString().trim();

            //data validation - empty fields
            if (userEmail.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty() || weightText.isEmpty()) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        "Please Fill In all Fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            //data validation - passwords need to match
            if (!userPassword.equals(confirmPassword)) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        "Passwords Do Not Match",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            //data validation - email is only used once
            if (db.userExists(userEmail)) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        "Email Already Exists, Please Use Login Page",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            double goalWeight = 0;
            //validation for target weight
            try {
                goalWeight = Double.parseDouble(weightText);
            } catch (NumberFormatException e) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        "Enter Valid Target Weight",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            //save user information to the database
            boolean inserted = db.insertUser(userEmail, userPassword, goalWeight);

            if (inserted) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        "Account Was Created Successfully",
                        Toast.LENGTH_SHORT
                ).show();
                //allow for SMS permisssions to be asked on account creation
                Intent intent = new Intent(
                        CreateAccountActivity.this,
                        SmsPermissionActivity.class);
                intent.putExtra("email", userEmail);

                startActivity(intent);

                finish();

            } else {
                Toast.makeText(
                        CreateAccountActivity.this,
                        "Account Creation Failed",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
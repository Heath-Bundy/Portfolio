package com.finalproject_heathbundy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject_heathbundy.DatabaseHelper;
import com.finalproject_heathbundy.R;
import com.finalproject_heathbundy.repositories.UserRepository;
import com.finalproject_heathbundy.repositories.WeightEntryRepository;
import com.finalproject_heathbundy.services.UserService;
import com.finalproject_heathbundy.services.WeightEntryService;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText createEmail;
    private EditText createPassword;
    private EditText reenterPassword;
    private EditText targetWeight;
    private Button createUser;
    private UserService userService;
    private UserRepository userRepository;

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

        //connects to the service and repository layers
        userService = new UserService(userRepository);
        userRepository = new UserRepository(this);

        //create account button logic
        createUser.setOnClickListener(v -> {
            String userEmail = createEmail.getText().toString().trim();
            String userPassword = createPassword.getText().toString().trim();
            String confirmPassword = reenterPassword.getText().toString().trim();
            String weightText = targetWeight.getText().toString().trim();

            //email validation - empty, valid, and exists
            String emailError = userService.emailValidation(userEmail);
            if (emailError != null) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        emailError,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            //password validation - empty, passwords match, length, characters
            String passwordError = userService.passwordValidation(userPassword, confirmPassword);
            if (passwordError != null) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        passwordError,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            //goal weight validation - empty, ensures a number is used, weight range
            String weightError = userService.goalWeightValidation(weightText);
            if (weightError != null) {
                Toast.makeText(
                        CreateAccountActivity.this,
                        weightError,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            double goalWeight = Double.parseDouble(weightText);

            //inserts user information into the database
            boolean inserted = userRepository.createUser(userEmail, userPassword, goalWeight);

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
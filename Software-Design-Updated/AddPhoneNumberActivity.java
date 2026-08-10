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
import com.finalproject_heathbundy.services.UserService;

public class AddPhoneNumberActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText confirmPhone;
    private Button saveButton;
    private String userEmail;
    private boolean smsEnabled;
    private UserService userService;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phone_number);

        phoneNumber = findViewById(R.id.phoneNumber);
        confirmPhone = findViewById(R.id.confirmPhone);
        saveButton = findViewById(R.id.acceptButton);

        userService = new UserService(userRepository);
        userRepository = new UserRepository(this);


        userEmail = getIntent().getStringExtra("email");
        smsEnabled = getIntent().getBooleanExtra("smsEnabled", false);

        //save button logic
        saveButton.setOnClickListener(v -> {
            String phone = phoneNumber.getText().toString().trim();
            String confirm = confirmPhone.getText().toString().trim();

            //phone number validation
            String phoneError = userService.smsPermissionsValidation(smsEnabled, phone,confirm);
            if (phoneError != null) {
                Toast.makeText(
                        this,
                        phoneError,
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            boolean updatedPhone = userRepository.updatePhoneNumber(userEmail, phone);
            boolean updateSms = userRepository.updateSmsPreference(userEmail, smsEnabled);

            //message if added phone is saved
            if (updatedPhone && updateSms){
                Toast.makeText(this,
                        "Phone Number Saved!",
                        Toast.LENGTH_SHORT
                ).show();

                Intent intent =
                        new Intent(AddPhoneNumberActivity.this, WeightTableActivity.class);

                intent.putExtra("email", userEmail);
                startActivity(intent);

                finish();
            } else {
                Toast.makeText(this,
                        "Failed To Save Phone Number",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}
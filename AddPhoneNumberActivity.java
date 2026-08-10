package com.finalproject_heathbundy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPhoneNumberActivity extends AppCompatActivity {

    private EditText phoneNumber;
    private EditText confirmPhone;
    private Button saveButton;
    private DatabaseHelper db;
    private String userEmail;
    private boolean smsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_phone_number);

        phoneNumber = findViewById(R.id.phoneNumber);
        confirmPhone = findViewById(R.id.confirmPhone);
        saveButton = findViewById(R.id.acceptButton);

        db = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("email");
        smsEnabled = getIntent().getBooleanExtra("smsEnabled", false);

        //save button logic
        saveButton.setOnClickListener(v -> {
            String phone = phoneNumber.getText().toString().trim();
            String confirm = confirmPhone.getText().toString().trim();

            //if user did not input a phone number
            if (phone.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(
                        this, "Enter Phone Number",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            //if phone numbers are not identical
            if (!phone.equals(confirm)){
                Toast.makeText(
                        this,
                        "Phone Numbers Do Not Match, Please Try Again",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            boolean updatedPhone = db.updatePhoneNumber(userEmail, phone);
            boolean updateSms = db.updateSmsPreference(userEmail, smsEnabled);

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
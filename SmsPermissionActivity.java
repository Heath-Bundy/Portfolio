package com.finalproject_heathbundy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SmsPermissionActivity extends AppCompatActivity {

    private Button allowSms;
    private Button denySms;
    private String userEmail;

    private final ActivityResultLauncher<String> smsPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {

                        if (isGranted) {

                            Toast.makeText(
                                    this,
                                    "SMS Notifications Enabled",
                                    Toast.LENGTH_SHORT
                            ).show();

                            openPhoneNumberScreen(true);

                        } else {

                            Toast.makeText(
                                    this,
                                    "SMS Permission Denied. App will continue normally.",
                                    Toast.LENGTH_LONG
                            ).show();

                            openPhoneNumberScreen(false);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_permissions);

        allowSms = findViewById(R.id.allowSms);
        denySms = findViewById(R.id.denySms);

        userEmail = getIntent().getStringExtra("email");
        if (userEmail == null) {
            Toast.makeText(this, "Missing user session", Toast.LENGTH_SHORT).show();
            finish();
        }

        allowSms.setOnClickListener(v -> requestSmsPermission());

        denySms.setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    "SMS Notifications Disabled",
                    Toast.LENGTH_SHORT
            ).show();

            openPhoneNumberScreen(false);
        });
    }

    private void requestSmsPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {

            openPhoneNumberScreen(true);

        } else {

            smsPermissionLauncher.launch(
                    Manifest.permission.SEND_SMS
            );
        }
    }

    private void openPhoneNumberScreen(boolean smsEnabled) {

        Intent intent =
                new Intent(
                        SmsPermissionActivity.this,
                        AddPhoneNumberActivity.class
                );

        intent.putExtra("email", userEmail);
        intent.putExtra("smsEnabled", smsEnabled);

        startActivity(intent);
        finish();
    }
}
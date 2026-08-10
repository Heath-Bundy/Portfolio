package com.finalproject_heathbundy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class AddEntryActivity extends AppCompatActivity {

    private EditText inputDate;
    private EditText inputWeight;
    private Button confirmAdd;
    private Button cancelAdd;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //loads Add Entry Layout
        setContentView(R.layout.add_entry);

        //connect the views
        inputDate = findViewById(R.id.inputDate);
        inputWeight = findViewById(R.id.inputWeight);
        confirmAdd = findViewById(R.id.confirmAdd);
        cancelAdd = findViewById(R.id.cancelAdd);

        //connects database
        db = new DatabaseHelper(this);

        String userEmail = getIntent().getStringExtra("email");

        //Add Entry Logic
        confirmAdd.setOnClickListener(v -> {
            String date =
                    inputDate.getText().toString().trim();
            String weightText =
                    inputWeight.getText().toString().trim();

            //input validation - empty fields
            if(date.isEmpty() || weightText.isEmpty()) {
                Toast.makeText(
                        AddEntryActivity.this,
                        "Please Fill In All Fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            double weight;

            try {
                weight = Double.parseDouble(weightText);
            } catch (NumberFormatException e) {
                Toast.makeText(
                        AddEntryActivity.this,
                        "Enter Valid Weight",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            boolean inserted = db.insertWeightEntry(userEmail, date, weight);

            if(inserted){
                Toast.makeText(
                        AddEntryActivity.this,
                        "Entry Added Successfully",
                        Toast.LENGTH_SHORT
                ).show();

                finish();
            }else{
                Toast.makeText(
                        AddEntryActivity.this,
                        "Failed To Add Entry",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        //cancel button logic
        cancelAdd.setOnClickListener(v -> finish());
    }
}
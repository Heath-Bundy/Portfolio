package com.finalproject_heathbundy.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject_heathbundy.DatabaseHelper;
import com.finalproject_heathbundy.R;
import com.finalproject_heathbundy.repositories.WeightEntryRepository;
import com.finalproject_heathbundy.services.WeightEntryService;


public class AddEntryActivity extends AppCompatActivity {

    private EditText inputDate;
    private EditText inputWeight;
    private Button confirmAdd;
    private Button cancelAdd;
    private WeightEntryService weightEntryService;
    private WeightEntryRepository weightEntryRepository;

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

        //connects service and repository layers
        weightEntryService = new WeightEntryService();
        weightEntryRepository = new WeightEntryRepository(this);

        String userEmail = getIntent().getStringExtra("email");

        //Add Entry Logic
        confirmAdd.setOnClickListener(v -> {
            String date =
                    inputDate.getText().toString().trim();
            String weightText =
                    inputWeight.getText().toString().trim();

            //date validation
            String dateError = weightEntryService.dateValidation(date);
            if (dateError != null) {
                Toast.makeText(
                        AddEntryActivity.this,
                        dateError, Toast.LENGTH_SHORT).show();
                return;
            }

            //weight validation
            String weightError = weightEntryService.weightValidation(weightText);
            if (weightError != null) {
                Toast.makeText(
                        AddEntryActivity.this,
                        weightError,
                        Toast.LENGTH_SHORT).show();
                return;
            }

            double weight = Double.parseDouble(weightText);

            boolean inserted = weightEntryRepository.addWeightEntry(userEmail, date, weight);


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
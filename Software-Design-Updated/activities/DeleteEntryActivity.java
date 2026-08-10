package com.finalproject_heathbundy.activities;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject_heathbundy.R;
import com.finalproject_heathbundy.models.WeightEntry;
import com.finalproject_heathbundy.repositories.WeightEntryRepository;

import java.util.HashMap;
import java.util.List;


public class DeleteEntryActivity extends AppCompatActivity {

    private TableLayout deleteTable;
    private Button confirmDeleteButton;
    private Button cancelButton;
    private String userEmail;

    private WeightEntryRepository weightEntryRepository;

    // Stores CheckBox -> Entry ID
    private HashMap<CheckBox, Integer> selectedEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Delete Entry Layout
        setContentView(R.layout.delete_entry);

        // Connect views
        deleteTable = findViewById(R.id.deleteTable);
        confirmDeleteButton = findViewById(R.id.confirmDeleteButton);
        cancelButton = findViewById(R.id.cancelButton);
        weightEntryRepository = new WeightEntryRepository(this);
        userEmail = getIntent().getStringExtra("email");
        selectedEntries = new HashMap<>();

        // Load entries from database
        loadEntries();

        // Delete selected entries
        confirmDeleteButton.setOnClickListener(v -> {

            int deleteCount = 0;

            for (CheckBox checkBox : selectedEntries.keySet()) {

                if (checkBox.isChecked()) {

                    int id = selectedEntries.get(checkBox);

                    if (weightEntryRepository.deleteEntry(id)){
                        deleteCount++;
                    }
                }
            }

            Toast.makeText(
                    DeleteEntryActivity.this,
                    deleteCount + " Entries Deleted",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
        });

        // Return to Weight Table
        cancelButton.setOnClickListener(v -> finish());
    }

    private void loadEntries() {

        List<WeightEntry> entries = weightEntryRepository.getEntriesForUser(userEmail);

        boolean alternate = false;

        for (WeightEntry entry : entries){
            int id = entry.getId();
            String date = entry.getDate();
            double weight = entry.getWeight();

            TableRow row = new TableRow(this);

            // Alternate row colors
            if (alternate) {
                row.setBackgroundResource(R.color.purplish_white);
            } else {
                row.setBackgroundResource(R.color.gentlemans_suit);
            }

            alternate = !alternate;

            // Checkbox
            CheckBox checkBox = new CheckBox(this);
            selectedEntries.put(checkBox, id);

            // Date column
            TextView dateView = new TextView(this);
            dateView.setText(date);
            dateView.setGravity(Gravity.CENTER);
            dateView.setTypeface(null, Typeface.BOLD);
            dateView.setPadding(12, 12, 12, 12);
            dateView.setTextColor(
                    getResources().getColor(R.color.black));

            // Weight column
            TextView weightView = new TextView(this);
            weightView.setText(String.valueOf(weight));
            weightView.setGravity(Gravity.CENTER);
            weightView.setTypeface(null, Typeface.BOLD);
            weightView.setPadding(12, 12, 12, 12);
            weightView.setTextColor(
                    getResources().getColor(R.color.black));

            // Column widths
            TableRow.LayoutParams checkParams =
                    new TableRow.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            0.5f
                    );

            TableRow.LayoutParams textParams =
                    new TableRow.LayoutParams(
                            0,
                            TableRow.LayoutParams.WRAP_CONTENT,
                            1f
                    );

            checkBox.setLayoutParams(checkParams);
            dateView.setLayoutParams(textParams);
            weightView.setLayoutParams(textParams);

            row.addView(checkBox);
            row.addView(dateView);
            row.addView(weightView);

            deleteTable.addView(row);
        }
    }
}
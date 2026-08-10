package com.finalproject_heathbundy.activities;

import android.graphics.Typeface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject_heathbundy.R;
import com.finalproject_heathbundy.models.WeightEntry;
import com.finalproject_heathbundy.repositories.UserRepository;
import com.finalproject_heathbundy.repositories.WeightEntryRepository;
import com.finalproject_heathbundy.services.TrendResult;
import com.finalproject_heathbundy.services.WeightAnalyzer;

import java.util.List;

public class WeightTableActivity extends AppCompatActivity {

    private Button addButton;
    private Button deleteButton;
    private TableLayout weightTable;
    private TextView targetWeightText;
    private TextView projectedDateText;
    private String userEmail;

    private WeightEntryRepository weightEntryRepository;
    private UserRepository userRepository;
    private WeightAnalyzer weightAnalyzer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //connects to the weight table layout
        setContentView(R.layout.weight_table);

        weightEntryRepository = new WeightEntryRepository(this);
        userRepository = new UserRepository(this);
        weightAnalyzer = new WeightAnalyzer();

        //connects buttons to the xml
        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        weightTable = findViewById(R.id.weightTable);
        targetWeightText = findViewById(R.id.targetWeight);
        projectedDateText = findViewById(R.id.projectedDate);

        userEmail = getIntent().getStringExtra("email");

        //checks for null email
        if (userEmail == null) {
            finish();
            return;
        }

        double targetWeight = userRepository.getTargetWeight(userEmail);

        targetWeightText.setText(
                "Target Weight: " +
                        targetWeight +
                        " lbs");

        loadEntries();
        loadTrendProjection();

        //Add Entry Button Logic
        addButton.setOnClickListener(v -> {
            Intent intent =
                    new Intent(WeightTableActivity.this, AddEntryActivity.class);

            intent.putExtra("email", userEmail);

            startActivity(intent);
        });

        //Delete Button Logic
        deleteButton.setOnClickListener(v -> {
            Intent intent =
                    new Intent(WeightTableActivity.this, DeleteEntryActivity.class);

            intent.putExtra("email", userEmail);

            startActivity(intent);
        });
    }

    private void loadEntries() {
        List<WeightEntry> entries = weightEntryRepository.getEntriesForUser(userEmail);
        List<WeightEntry> sortedEntries = WeightAnalyzer.sortChronologically(entries);

        boolean alternate = false;

        for (WeightEntry entry : sortedEntries) {
            String date = entry.getDate();
            double weight = entry.getWeight();

            TableRow row = new TableRow(this);

            //layout - alternate colors
            if(alternate) {
                row.setBackgroundResource(R.color.purplish_white);
            } else {
                row.setBackgroundResource(R.color.gentlemans_suit);
            }

            alternate = !alternate;

            TextView dateView = new TextView(this);
            dateView.setText(date);
            dateView.setPadding(12, 12, 12, 12);
            dateView.setGravity(Gravity.CENTER);
            dateView.setTypeface(null, Typeface.BOLD);
            dateView.setTextColor(getResources().getColor(R.color.black));

            TextView weightView = new TextView(this);
            weightView.setText(String.valueOf(weight));
            weightView.setPadding(12, 12, 12, 12);
            weightView.setGravity(Gravity.CENTER);
            weightView.setTypeface(null, Typeface.BOLD);
            weightView.setTextColor(getResources().getColor(R.color.black));


            row.addView(dateView);
            row.addView(weightView);

            weightTable.addView(row);
        }
    }

    private  void loadTrendProjection() {
        List<WeightEntry> entries = weightEntryRepository.getEntriesForUser(userEmail);
        double targetWeight = userRepository.getTargetWeight(userEmail);

        TrendResult result = weightAnalyzer.analyzeTrend(entries, targetWeight);

        if (result.hasDate()) {
            projectedDateText.setText("Projected Goal Date: " + result.getProjectedDate());
        }else {
            projectedDateText.setText(result.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(weightTable.getChildCount() > 1) {
            weightTable.removeViews(
                    1,
                    weightTable.getChildCount() - 1);
        }

        loadEntries();
        loadTrendProjection();
    }
}
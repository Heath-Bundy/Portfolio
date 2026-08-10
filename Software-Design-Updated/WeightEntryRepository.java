package com.finalproject_heathbundy.repositories;

import android.content.Context;
import android.database.Cursor;

import com.finalproject_heathbundy.DatabaseHelper;
import com.finalproject_heathbundy.models.WeightEntry;

import java.util.ArrayList;
import java.util.List;

public class WeightEntryRepository {
    private final DatabaseHelper db;

    public WeightEntryRepository(Context context) {
        this.db = new DatabaseHelper(context);
    }

    public List<WeightEntry> getEntriesForUser(String email) {

        List<WeightEntry> entries = new ArrayList<>();
        Cursor cursor = db.getAllWeightEntries(email);

        while (cursor.moveToNext()){
            entries.add(new WeightEntry(
                    cursor.getInt(0),
                    cursor.getString(2),
                    cursor.getDouble(3)
            ));
        }

        cursor.close();
        return entries;
    }

    public  boolean addWeightEntry(String email, String entryDate, double weight){
        return db.insertWeightEntry(email, entryDate,weight);
    }

    public boolean deleteEntry(int id){
        return db.deleteEntry(id);
    }
}

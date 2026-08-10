package com.finalproject_heathbundy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeightTracker.db";
    private static final  int DATABASE_VERSION = 4;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ENTRIES = "entries";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //create a table for login information
        String createUsersTable =
                "CREATE TABLE " + TABLE_USERS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE," +
                "password TEXT," +
                "targetWeight REAL," +
                "phoneNumber TEXT," +
                "smsEnabled INTEGER DEFAULT 0)";

        //create a table for weight entries
        String createEntryTable =
                "CREATE TABLE " + TABLE_ENTRIES + "(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email TEXT," +
                        "entryDate TEXT," +
                        "weight REAL)";

        db.execSQL(createUsersTable);
        db.execSQL(createEntryTable);
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

    // create account
    public boolean insertUser(String email, String password, double targetWeight) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("targetWeight", targetWeight);

        long result = db.insert(TABLE_USERS, null, values);

        Log.d("DB_INSERT", "Result = " + result);


        return result != -1;
    }

    //weight entry
    public  boolean insertWeightEntry(String email, String entryDate, double weight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("entryDate", entryDate);
        values.put("weight", weight);

        long result = db.insert(TABLE_ENTRIES, null, values);

        return result != -1;
    }

    public Cursor getAllWeightEntries(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_ENTRIES +
                        " WHERE email=? ORDER BY id DESC",
                new String[]{email}
        );
    }

    //checking if email is already used
    public boolean userExists(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email=?",
                new String[]{email});

        boolean exists = cursor.getCount() > 0;

        cursor.close();

        return exists;
    }

    //login verification
    public boolean checkLogin(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password});

        boolean success = cursor.getCount() > 0;

        cursor.close();

        return success;
    }

    //get goal weight from database
    public double getTargetWeight(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT targetWeight FROM users WHERE email=?",
                new String[]{email});

        double targetWeight = 0;

        if(cursor.moveToFirst()) {
            targetWeight = cursor.getDouble(0);
        }

        cursor.close();

        return targetWeight;
    }

    //adds phone number to the database
    public boolean updatePhoneNumber(String email, String phoneNumber) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("phoneNumber", phoneNumber);

        int rows = db.update(
                TABLE_USERS,
                values,
                "email=?",
                new String[]{email}
        );

        return rows > 0;
    }

    //adds user selected persmissions to the database
    public boolean updateSmsPreference(String email, boolean enabled) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("smsEnabled", enabled ? 1 : 0);

        int rows = db.update(
                TABLE_USERS,
                values,
                "email=?",
                new String[]{email}
        );

        return rows > 0;
    }

    public boolean isSmsEnabled(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT smsEnabled FROM users WHERE email=?",
                new String[]{email}
        );

        boolean enabled = false;

        if(cursor.moveToFirst()) {
            enabled = cursor.getInt(0) == 1;
        }

        cursor.close();

        return enabled;
    }

    //retrieves phone number from the database
    public String getPhoneNumber(String email) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT phoneNumber FROM users WHERE email=?",
                new String[]{email}
        );

        String phone = "";

        if(cursor.moveToFirst()) {
            phone = cursor.getString(0);
        }

        cursor.close();

        return phone;
    }

    public boolean deleteEntry(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int rowsDeleted =
                db.delete(
                        TABLE_ENTRIES,
                        "id=?",
                        new String[]{String.valueOf(id)}
                );

        return rowsDeleted > 0;
    }
}
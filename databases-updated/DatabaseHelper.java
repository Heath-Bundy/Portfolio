package com.finalproject_heathbundy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.finalproject_heathbundy.services.HashPassword;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "WeightTracker.db";
    private static final  int DATABASE_VERSION = 6;
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ENTRIES = "entries";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
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
                        "user_id INTEGER NOT NULL," +
                        "entryDate TEXT," +
                        "weight REAL," +
                        "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS +
                        "(id) ON DELETE CASCADE)";

        db.execSQL(createUsersTable);
        db.execSQL(createEntryTable);
    }

    @Override
    public  void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            // Add user_id to entries, without dropping any existing data
            db.execSQL("ALTER TABLE " + TABLE_ENTRIES + " ADD COLUMN user_id INTEGER");

            // Backfill user_id using the old email column, matching against users.id
            db.execSQL(
                    "UPDATE " + TABLE_ENTRIES +
                            " SET user_id = (SELECT id FROM " + TABLE_USERS +
                            " WHERE " + TABLE_USERS + ".email = " + TABLE_ENTRIES + ".email)"
            );

            db.execSQL("CREATE INDEX IF NOT EXISTS idx_entries_user_id ON " + TABLE_ENTRIES + "(user_id)");
        }
    }

    public int getUserId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_USERS + " WHERE email=?", new String[]{email});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    // create account
    public boolean insertUser(String email, String hashedPassword, double targetWeight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", hashedPassword);
        values.put("targetWeight", targetWeight);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    //weight entry
    public  boolean insertWeightEntry(int userId, String entryDate, double weight) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("entryDate", entryDate);
        values.put("weight", weight);

        long result = db.insert(TABLE_ENTRIES, null, values);

        return result != -1;
    }

    public Cursor getAllWeightEntries(int userId) {

        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM " + TABLE_ENTRIES +
                        " WHERE user_id=? ORDER BY id DESC",
                new String[]{String.valueOf(userId)}
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
    public boolean checkLogin(String email, String enteredPassword) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT password FROM " + TABLE_USERS + " WHERE email=?",
                new String[]{email}
        );

        boolean isValid = false;
        if (cursor.moveToFirst()) {
            String storedHash = cursor.getString(0);
            isValid = HashPassword.verify(enteredPassword, storedHash);
        }
        cursor.close();
        return isValid;
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
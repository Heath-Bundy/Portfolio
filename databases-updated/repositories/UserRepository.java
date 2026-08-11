package com.finalproject_heathbundy.repositories;

import android.content.Context;

import com.finalproject_heathbundy.DatabaseHelper;
import com.finalproject_heathbundy.services.HashPassword;

public class UserRepository {
    private final DatabaseHelper db;

    public UserRepository(Context context) {
        this.db = new DatabaseHelper(context);
    }

    public boolean createUser(String email, String password, double targetWeight) {
        String hashedPassword = HashPassword.hash(password);
        return db.insertUser(email, hashedPassword, targetWeight);
    }

    public int getUserId(String email) {
        return db.getUserId(email);
    }

    public boolean emailExists(String email) {
        return db.userExists(email);
    }

    public boolean verifyLogin(String email, String password) {
        return db.checkLogin(email, password);
    }

    public double getTargetWeight(String email) {
        return db.getTargetWeight(email);
    }

    public boolean updatePhoneNumber(String email, String phoneNumber) {
        return db.updatePhoneNumber(email, phoneNumber);
    }

    public String getPhoneNumber(String email) {
        return db.getPhoneNumber(email);
    }

    public boolean updateSmsPreference(String email, boolean enabled) {
        return db.updateSmsPreference(email, enabled);
    }

    public boolean isSmsEnabled(String email) {
        return db.isSmsEnabled(email);
    }
}
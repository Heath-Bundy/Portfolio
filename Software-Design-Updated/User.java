package com.finalproject_heathbundy.models;

public class User {
    private final String email;
    private final double targetWeight;
    private final String phoneNumber;
    private final boolean smsEnabled;

    public User(String email, double targetWeight, String phoneNumber, boolean smsEnabled) {
        this.email = email;
        this.targetWeight = targetWeight;
        this.phoneNumber = phoneNumber;
        this.smsEnabled = smsEnabled;
    }

    public String getEmail() {
        return email;
    }

    public double getTargetWeight() {
        return targetWeight;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }
}

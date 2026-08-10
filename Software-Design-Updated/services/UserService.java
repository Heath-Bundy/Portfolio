package com.finalproject_heathbundy.services;

import android.util.Patterns;

import com.finalproject_heathbundy.repositories.UserRepository;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public String emailValidation(String userEmail){
        if (userEmail.isEmpty()) {
            return "Please enter an email address";
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            return "Please enter a valid email address";
        }
        if (userRepository.emailExists(userEmail)){
            return "An account associated with this email already exists";
        }
        return null;
    }

    public String passwordValidation(String userPassword, String confirmPassword){
        if (userPassword.isEmpty()) {
            return "Please enter a password";
        }
        if (!userPassword.equals(confirmPassword)){
            return "Passwords do not match, please try again";
        }
        if (userPassword.length() < 8 || userPassword.length() > 64){
            return "Password must be between 8 and 64 characters";
        }
        if (!userPassword.matches("^[\\x21-\\x7E]+$")){
            return "Password contains an invalid character. Only use printable ASCII characters";
        }
        return null;
    }

    public String goalWeightValidation(String weightText){
        if (weightText.isEmpty()) {
            return "Target Weight is empty, please enter a Target Weight";
        }

        double targetWeight = 0;

        try {
            targetWeight = Double.parseDouble(weightText);
        } catch (NumberFormatException e) {
            return "Target Weight must be a number, please try again";
        }
        if (targetWeight < 50 || targetWeight > 1000){
            return "Target Weight must be between 50 and 1,000";
        }
        return null;
    }

    public String smsPermissionsValidation(boolean smsEnabled, String phoneNumber, String confirmPhoneNumber){
        if (!smsEnabled){
            return null;
        }
        if (phoneNumber.isEmpty()){
            return "Phone Number is empty, please enter a valid phone number";
        }
        if (!phoneNumber.equals(confirmPhoneNumber)){
            return "Phone Numbers do not match, please try again";
        }
        if (phoneNumber.length() != 10){
            return "Invalid Phone Number Length. Phone Number must be 10 digits in length";
        }
        return null;
    }
}
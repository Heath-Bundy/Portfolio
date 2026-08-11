package com.finalproject_heathbundy.services;

import org.mindrot.jbcrypt.BCrypt;

public class HashPassword {

    // hashes a plain password, bcrypt creates the salt automatically
    public static String hash(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verifies a login attempt against the stored bcrypt hash
    public static boolean verify(String enteredPassword, String storedHash) {
        return BCrypt.checkpw(enteredPassword, storedHash);
    }
}
package com.finalproject_heathbundy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.finalproject_heathbundy.services.HashPassword;

import org.junit.Test;

public class HashPasswordTest {
    @Test
    public void verify_returnsTrue_forCorrectPassword() {
        String hashed = HashPassword.hash("myPassword123");

        assertTrue(HashPassword.verify("myPassword123", hashed));
    }

    @Test
    public void verify_returnsFalse_forIncorrectPassword() {
        String hashed = HashPassword.hash("myPassword123");

        assertFalse(HashPassword.verify("wrongPassword", hashed));
    }

    @Test
    public void hash_producesDifferentHashesForSamePassword_onEachCall() {
        String hash1 = HashPassword.hash("samePassword");
        String hash2 = HashPassword.hash("samePassword");

        assertNotEquals(hash1, hash2);
    }

    @Test
    public void verify_worksCorrectly_regardlessOfWhichHashCallWasUsed() {
        String hash1 = HashPassword.hash("samePassword");
        String hash2 = HashPassword.hash("samePassword");

        // Even though hash1 and hash2 are different strings (different embedded salts),
        // both should still correctly verify the same original password
        assertTrue(HashPassword.verify("samePassword", hash1));
        assertTrue(HashPassword.verify("samePassword", hash2));

        }
    }
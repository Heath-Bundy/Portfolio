package com.finalproject_heathbundy;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.finalproject_heathbundy.repositories.UserRepository;
import com.finalproject_heathbundy.services.UserService;

import org.junit.Test;

public class UserServiceTest {

    //creates a fake repository for testing, treats email@test.com as it already exists
    private static class FakeUserRepository extends UserRepository {
        public FakeUserRepository() {
            super(null);
        }

        @Override
        public boolean emailExists(String email) {
            return email.equals("email@test.com");
        }
    }

    private final UserService userService = new UserService(new FakeUserRepository());

    // email validation tests
    @Test
    public  void emailValidation_empty() {
        assertNotNull(userService.emailValidation(""));
    }

    @Test
    public void emailValidation_invalidFormat() {
        assertNotNull(userService.emailValidation("not-an-email"));
    }

    @Test
    public void emailValidation_exists() {
        assertNotNull(userService.emailValidation("email@test.com"));
    }

    @Test
    public void emailValidation_newEmail() {
        assertNull(userService.emailValidation("newemail@test.com"));
    }

    // password validation tests
    @Test
    public void passValidation_empty(){
        assertNotNull(userService.passwordValidation("","password123"));
    }

    @Test
    public void passValidation_match(){
        assertNotNull(userService.passwordValidation("password1","password123"));
    }

    @Test
    public void passValidation_short(){
        assertNotNull(userService.passwordValidation("short","short"));
    }

    @Test
    public void passValidation_exactly8(){
        String pass8 = "a".repeat(8);
        assertNull(userService.passwordValidation(pass8,pass8));
    }

    @Test
    public void passValidation_long(){
        String tooLong = "a".repeat(65);
        assertNotNull(userService.passwordValidation(tooLong, tooLong));
    }

    @Test
    public void passValidation_exactly64(){
        String pass64 = "a".repeat(64);
        assertNull(userService.passwordValidation(pass64, pass64));
    }

    @Test
    public void passValidation_validCharacter(){
        String pass = "Aa1!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        assertNull(userService.passwordValidation(pass, pass));
    }

    // goal weight validation tests
    @Test
    public void goalWeight_empty(){
        assertNotNull(userService.goalWeightValidation(""));
    }

    @Test
    public void goalWeight_checkForNumber(){
        assertNotNull(userService.goalWeightValidation("10.6+q"));
    }

    @Test
    public void goalWeight_low(){
        assertNotNull(userService.goalWeightValidation("49"));
    }

    @Test
    public void goalWeight_exactly50(){
        assertNull(userService.goalWeightValidation("50"));
    }

    @Test
    public void goalWeight_high(){
        assertNotNull(userService.goalWeightValidation("1001"));
    }

    @Test
    public void goalWeight_exactly1000(){
        assertNull(userService.goalWeightValidation("1000"));
    }

    @Test
    public void goalWeight_validWeight(){
        assertNull(userService.goalWeightValidation("200"));
    }

    //sms permission validation tests
    @Test
    public void smsPermission_skipsPhoneEntrySmsDisabled(){
        assertNull(userService.smsPermissionsValidation(false, "",""));
    }

    @Test
    public void smsPermission_empty(){
        assertNotNull(userService.smsPermissionsValidation(true, "",""));
    }

    @Test
    public void smsPermission_phoneNumberMatch(){
        assertNotNull(userService.smsPermissionsValidation(true,"1234567890","1234567891"));
    }

    @Test
    public void smsPermission_phoneNumberShort(){
        assertNotNull(userService.smsPermissionsValidation(true,"123456789","123456789"));
    }

    @Test
    public void smsPermissions_phoneNumberLong(){
        assertNotNull(userService.smsPermissionsValidation(true, "123456789123456789", "123456789123456789"));
    }

    @Test
    public void smsPermissions_phoneNumberEquals10(){
        assertNull(userService.smsPermissionsValidation(true, "1234567890","1234567890"));
    }
}
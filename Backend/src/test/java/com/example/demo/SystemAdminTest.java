package com.example.demo;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SystemAdminTest {
    SystemAdmin systemAdmin=SystemAdmin.getSystemInstance();
    @Test
    void createUser() throws IOException {
        // this an already created user
        User user = new User();
        user.setEmail("ahmedyasser882000@mail.com");

        user.setFirstName("Ahmed");
        user.setLastName("Yasser");
        user.setPassword("4444");
        //will return false if user already exists;
        assertFalse(systemAdmin.createUser(user));

    }

    @Test
    void checkSignIn() {
        //valid sign in
        User user = new User();
        user.setEmail("ahmedyasser882000@mail.com");

        user.setFirstName("Ahmed");
        user.setLastName("Yasser");
        user.setPassword("4444");

        assertTrue(systemAdmin.checkSignIn(user));
        // if we changed the password
        user.setPassword("43141234");
        assertFalse(systemAdmin.checkSignIn(user));
    }
}
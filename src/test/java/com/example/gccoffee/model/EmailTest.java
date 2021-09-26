package com.example.gccoffee.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    public void testInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            var email = new Email("acccc");
        });
    }

    @Test
    public void testValidEmail() {
        var email = new Email("qwer@naver.com");
        assertEquals("qwer@naver.com", email.getAddress());
    }



    @Test
    public void testEqEmail() {
        var email = new Email("qwer@naver.com");
        var email2 = new Email("qwer@naver.com");
        assertEquals("qwer@naver.com", email);
    }




}
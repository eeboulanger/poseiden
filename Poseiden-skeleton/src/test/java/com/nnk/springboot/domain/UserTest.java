package com.nnk.springboot.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Joey", "myPassword", "Joe Doe", "USER");
    }

    @Test
    public void getterAndSetterTest() {
        assertEquals("Joey", user.getUsername());
        assertEquals("myPassword", user.getPassword());
        assertEquals("Joe Doe", user.getFullname());
        assertEquals("USER", user.getRole());
    }
}

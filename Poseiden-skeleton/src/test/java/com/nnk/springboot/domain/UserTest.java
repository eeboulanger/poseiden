package com.nnk.springboot.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {
    private static Validator validator;
    private User user;

    @BeforeAll
    public static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @BeforeEach
    public void setUp() {
        user = new User("Joey", "myPassword", "Joe Doe", "USER");
    }

    @Test
    public void validationTest() {
        User user = new User("Joey", "myPassword", "Joe Doe", "USER");

        var violations = validator.validate(user);
        assertTrue(violations.isEmpty());

        user.setUsername("");
        violations = validator.validate(user);
        assertEquals(1, violations.size());

        user.setPassword("");
        violations = validator.validate(user);
        assertEquals(2, violations.size());

        user.setFullname("");
        violations = validator.validate(user);
        assertEquals(3, violations.size());

        user.setRole("");
        violations = validator.validate(user);
        assertEquals(4, violations.size());
    }

    @Test
    public void getterAndSetterTest() {
        assertEquals("Joey", user.getUsername());
        assertEquals("myPassword", user.getPassword());
        assertEquals("Joe Doe", user.getFullname());
        assertEquals("USER", user.getRole());
    }
}

package com.nnk.springboot.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDTOTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void validationTest() {
        UserDTO userDTO = new UserDTO("Joey", "validPassword@11", "Joe Doe", "USER");

        var violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty());

        userDTO.setUsername("");
        violations = validator.validate(userDTO);
        assertEquals(2, violations.size());//Not empty, not blank

        userDTO.setPassword("");
        violations = validator.validate(userDTO);
        assertEquals(5, violations.size()); //Not blank, Size & Pattern

        userDTO.setFullname("");
        violations = validator.validate(userDTO);
        assertEquals(7, violations.size());//Not empty, not blank

        userDTO.setRole("");
        violations = validator.validate(userDTO);
        assertEquals(9, violations.size());//Not empty, not blank
    }
}

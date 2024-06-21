package com.nnk.springboot.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class BidListTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void validationTest() {
        //Given valid bid
        BidList bid = new BidList("Valid account", "Valid type", 20);
        //when
        var violations = validator.validate(bid);
        //then
        assertTrue(violations.isEmpty());

        //Given Account blank and Empty
        bid.setAccount("");
        violations = validator.validate(bid);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class)));
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotEmpty.class)));

        //Given Type blank and Empty
        bid.setAccount("Valid account");
        bid.setType("");
        violations = validator.validate(bid);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class)));
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotEmpty.class)));

    }
}

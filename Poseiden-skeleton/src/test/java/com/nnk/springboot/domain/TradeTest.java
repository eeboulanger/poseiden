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

public class TradeTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void validationTest() {
        //Given valid trade
        Trade trade = new Trade("Valid account", "Valid type", 10d);
        //when
        var violations = validator.validate(trade);
        //then
        assertTrue(violations.isEmpty());

        //Given Account blank and Empty
        trade.setAccount("");
        violations = validator.validate(trade);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class)));
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotEmpty.class)));

        //Given Type blank and Empty
        trade.setAccount("Valid account");
        trade.setType("");
        violations = validator.validate(trade);

        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotBlank.class)));
        assertTrue(violations.stream().anyMatch(violation
                -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotEmpty.class)));

    }
}

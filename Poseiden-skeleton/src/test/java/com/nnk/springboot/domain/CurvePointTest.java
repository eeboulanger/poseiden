package com.nnk.springboot.domain;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurvePointTest {
    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    public void validationTest() {
        CurvePoint curvePoint = new CurvePoint((byte) 1, 10d, 10d);

        var violations = validator.validate(curvePoint);

        assertTrue(violations.isEmpty());

        curvePoint.setCurveId(null); //Invalid check NotNull
        violations=validator.validate(curvePoint);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(
                violation -> violation.getConstraintDescriptor().getAnnotation().annotationType().equals(NotNull.class)
        ));
    }

    @Test
    public void getterAndSetterTest() {
        Timestamp date = Timestamp.from(Instant.now());
        CurvePoint curvePoint = new CurvePoint((byte) 1, 10d, 10d);
        curvePoint.setAsOfDate(date);
        curvePoint.setCreationDate(date);

        assertEquals((byte) 1, curvePoint.getCurveId());
        assertEquals(10d, curvePoint.getTerm());
        assertEquals(10d, curvePoint.getValue());
        assertEquals(date, curvePoint.getAsOfDate());
        assertEquals(date, curvePoint.getCreationDate());
    }
}

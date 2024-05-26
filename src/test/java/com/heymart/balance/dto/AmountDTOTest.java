package com.heymart.balance.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class AmountDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAmountIsValid_thenNoConstraintViolations() {
        AmountDTO amountDTO = new AmountDTO();
        amountDTO.setAmount(10.0);

        Set<ConstraintViolation<AmountDTO>> violations = validator.validate(amountDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenAmountIsNegative_thenOneConstraintViolation() {
        AmountDTO amountDTO = new AmountDTO();
        amountDTO.setAmount(-5.0);

        Set<ConstraintViolation<AmountDTO>> violations = validator.validate(amountDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<AmountDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath()).hasToString("amount");
        assertThat(violation.getMessage()).hasToString("The amount must be positive");
    }

    @Test
    void testSettersAndGetters() {
        AmountDTO amountDTO = new AmountDTO();
        amountDTO.setAmount(20.0);

        assertThat(amountDTO.getAmount()).isEqualTo(20.0);
    }
}

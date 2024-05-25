package com.heymart.balance.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CheckoutDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsAreValid_thenNoConstraintViolations() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(10.0, "supermarketId", "userId");

        Set<ConstraintViolation<CheckoutDTO>> violations = validator.validate(checkoutDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenAmountIsInvalid_thenOneConstraintViolation() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(0.0, "supermarketId", "userId");

        Set<ConstraintViolation<CheckoutDTO>> violations = validator.validate(checkoutDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<CheckoutDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("amount");
        assertThat(violation.getMessage()).isEqualTo("The amount must be greater than 0");
    }

    @Test
    void whenSupermarketIdIsNull_thenOneConstraintViolation() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(10.0, null, "userId");

        Set<ConstraintViolation<CheckoutDTO>> violations = validator.validate(checkoutDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<CheckoutDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("supermarketId");
        assertThat(violation.getMessage()).isEqualTo("Transaction supermarketId cannot be null");
    }

    @Test
    void whenUserIdIsNull_thenOneConstraintViolation() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(10.0, "supermarketId", null);

        Set<ConstraintViolation<CheckoutDTO>> violations = validator.validate(checkoutDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<CheckoutDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("userId");
        assertThat(violation.getMessage()).isEqualTo("Transaction userId cannot be null");
    }

    @Test
    void testSettersAndGetters() {
        CheckoutDTO checkoutDTO = new CheckoutDTO();
        checkoutDTO.setAmount(20.0);
        checkoutDTO.setSupermarketId("newSupermarketId");
        checkoutDTO.setUserId("newUserId");

        assertThat(checkoutDTO.getAmount()).isEqualTo(20.0);
        assertThat(checkoutDTO.getSupermarketId()).isEqualTo("newSupermarketId");
        assertThat(checkoutDTO.getUserId()).isEqualTo("newUserId");
    }
}

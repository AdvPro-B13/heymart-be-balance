package com.heymart.balance.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CheckoutDTOTest {
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
        assertThat(violation.getPropertyPath()).hasToString("amount");
        assertThat(violation.getMessage()).hasToString("The amount must be greater than 0");
    }

    @Test
    void whenSupermarketIdIsNull_thenOneConstraintViolation() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(10.0, null, "userId");

        Set<ConstraintViolation<CheckoutDTO>> violations = validator.validate(checkoutDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<CheckoutDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath()).hasToString("supermarketId");
        assertThat(violation.getMessage()).hasToString("Transaction supermarketId cannot be null");
    }

    @Test
    void whenUserIdIsNull_thenOneConstraintViolation() {
        CheckoutDTO checkoutDTO = new CheckoutDTO(10.0, "supermarketId", null);

        Set<ConstraintViolation<CheckoutDTO>> violations = validator.validate(checkoutDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<CheckoutDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath()).hasToString("userId");
        assertThat(violation.getMessage()).hasToString("Transaction userId cannot be null");
    }

    @Test
    void testSettersAndGetters() {
        CheckoutDTO checkoutDTO = new CheckoutDTO();
        checkoutDTO.setAmount(20.0);
        checkoutDTO.setSupermarketId("newSupermarketId");
        checkoutDTO.setUserId("newUserId");

        assertThat(checkoutDTO.getAmount()).isEqualTo(20.0);
        assertThat(checkoutDTO.getSupermarketId()).hasToString("newSupermarketId");
        assertThat(checkoutDTO.getUserId()).hasToString("newUserId");
    }
}

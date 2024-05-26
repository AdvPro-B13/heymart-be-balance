package com.heymart.balance.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionDTOTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsAreValid_thenNoConstraintViolations() {
        TransactionDTO transactionDTO = new TransactionDTO(10.0, "TOPUP", "USER");

        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenAmountIsInvalid_thenOneConstraintViolation() {
        TransactionDTO transactionDTO = new TransactionDTO(0.0, "TOPUP", "USER");

        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath()).hasToString("amount");
        assertThat(violation.getMessage()).hasToString("The amount must be greater than 0");
    }

    @Test
    void whenTransactionTypeIsNull_thenOneConstraintViolation() {
        TransactionDTO transactionDTO = new TransactionDTO(10.0, null, "USER");

        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath()).hasToString("transactionType");
        assertThat(violation.getMessage()).hasToString("Transaction type cannot be null");
    }

    @Test
    void whenOwnerTypeIsNull_thenOneConstraintViolation() {
        TransactionDTO transactionDTO = new TransactionDTO(10.0, "TOPUP", null);

        Set<ConstraintViolation<TransactionDTO>> violations = validator.validate(transactionDTO);

        assertThat(violations).hasSize(1);
        ConstraintViolation<TransactionDTO> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath()).hasToString("ownerType");
        assertThat(violation.getMessage()).hasToString("Owner type cannot be null");
    }

    @Test
    void testSettersAndGetters() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(20.0);
        transactionDTO.setTransactionType("WITHDRAWAL");
        transactionDTO.setOwnerType("USER");

        assertThat(transactionDTO.getAmount()).isEqualTo(20.0);
        assertThat(transactionDTO.getTransactionType()).hasToString("WITHDRAWAL");
        assertThat(transactionDTO.getOwnerType()).hasToString("USER");
    }
}

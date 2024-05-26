package com.heymart.balance.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void testNoArgsConstructor() {
        // Test that no-args constructor initializes the object
        UserDTO user = new UserDTO();
        assertNull(user.getId(), "Id should be null");
        assertNull(user.getSupermarketId(), "SupermarketId should be null");
    }

    @Test
    void testAllArgsConstructor() {
        // Test that all-args constructor sets the values correctly
        UserDTO user = new UserDTO("1", "S1");
        assertEquals("1", user.getId(), "Id should match the constructor argument");
        assertEquals("S1", user.getSupermarketId(), "SupermarketId should match the constructor argument");
    }

    @Test
    void testSetAndGetId() {
        // Test the setId and getId methods
        UserDTO user = new UserDTO();
        user.setId("2");
        assertEquals("2", user.getId(), "Id should be '2' after setId");
    }

    @Test
    void testSetAndGetSupermarketId() {
        // Test the setSupermarketId and getSupermarketId methods
        UserDTO user = new UserDTO();
        user.setSupermarketId("S2");
        assertEquals("S2", user.getSupermarketId(), "SupermarketId should be 'S2' after setSupermarketId");
    }
}
package com.cda.winit;

import com.cda.winit.user.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    @Test
    public void testUserId() {
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());
    }

    @Test
    public void testUserFirstname() {
        String firstName = "elea";
        user.setFirstName(firstName);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    public void testUserLastname() {
        String lastName = "volt";
        user.setLastName(lastName);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    public void testUserEmail() {
        String email = "elea@gmail.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testUserCity() {
        String city = "Bordeaux";
        user.setCity(city);
        assertEquals(city, user.getCity());
    }

    @Test
    public void testUserPassword() {
        String password = "1234";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testUserIsAcceptTerms() {
        Boolean isEnabled = true;
        user.setIsAcceptTerms(isEnabled);
        assertEquals(isEnabled, user.getIsAcceptTerms());
    }

    @Test
    public void testUserCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt());
    }

    @Test
    public void testUserUpdatedAt() {
        LocalDateTime updatedAt = LocalDateTime.now();
        user.setUpdatedAt(updatedAt);
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    public void testUserRole() {
        String role = "ROLE_USER";
        user.setRole(role);
        assertEquals(role, user.getRole());
    }
}

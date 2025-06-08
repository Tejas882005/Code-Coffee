package com.atmapp.service;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atmapp.dao.UserDAO;
import com.atmapp.exception.AuthenticationException;
import com.atmapp.model.User;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setCustomerNumber(123456);
        testUser.setPin(1234);
    }

    @Test
    void testAuthenticate_ValidCredentials_ReturnsUser() throws SQLException, AuthenticationException {
        when(userDAO.authenticate(123456, 1234)).thenReturn(testUser);

        User result = authService.authenticate(123456, 1234);

        assertNotNull(result);
        assertEquals(123456, result.getCustomerNumber());
        assertEquals(1, result.getUserId());
        verify(userDAO, times(1)).authenticate(123456, 1234);
    }

    @Test
    void testAuthenticate_InvalidCredentials_ThrowsAuthenticationException() throws SQLException {
        when(userDAO.authenticate(123456, 9999)).thenReturn(null);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () -> {
            authService.authenticate(123456, 9999);
        });
        assertEquals("Invalid customer number or PIN", exception.getMessage());
        verify(userDAO, times(1)).authenticate(123456, 9999);
    }

    @Test
    void testAuthenticate_DatabaseError_ThrowsSQLException() throws SQLException {
        when(userDAO.authenticate(123456, 1234)).thenThrow(new SQLException("Database error"));

        assertThrows(SQLException.class, () -> {
            authService.authenticate(123456, 1234);
        });
        verify(userDAO, times(1)).authenticate(123456, 1234);
    }
}
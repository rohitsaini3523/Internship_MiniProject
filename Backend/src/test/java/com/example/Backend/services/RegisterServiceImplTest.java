package com.example.Backend.services;

import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserRegistrationException;
import com.example.Backend.model.UserRegister;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserRegisterValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class RegisterServiceImplTest {

    @Mock
    private UserRepository testUserRepository;
    @InjectMocks
    private RegisterServiceImpl registerService;
    UserRegister expectedUserRegister, user;
    @InjectMocks
    UserRegisterValidator userRegisterValidator;

    @BeforeEach
    void setUp() {
        registerService = new RegisterServiceImpl(testUserRepository, userRegisterValidator);
        expectedUserRegister = UserRegister.builder().username("rs3523").password("Rohit@123").email("rohit@gmail.com").build();
    }

    @Test
    void itShouldRegister() {
        user = UserRegister.builder().username("rs3523").password("Rohit@123").email("rohitsaini3523@gmail.com").build();
        when(testUserRepository.existsByUsername(user.getUsername())).thenReturn(false);
        String result = registerService.register(user);
        assertThat(result).isEqualTo("Registered");
    }

    @Test
    void itShouldThrow_InvalidInputExceptionForInvalidInput() {
        String username = "";
        String password = "password";
        String email = "email.com";
        Exception exception = assertThrows(InvalidInputException.class, () -> {
            registerService.register(UserRegister.builder().username(username).password(password).email(email).build());
        });
        assertEquals("Invalid Register details", exception.getMessage());
    }


    @Test
    void itShouldThrow_UserRegistrationExceptionForRegisteredUser() {
        UserRegister alreadyRegisteredUser = new UserRegister();
        alreadyRegisteredUser.setUsername("existingUser");
        alreadyRegisteredUser.setEmail("existingUser@gmail.com");
        alreadyRegisteredUser.setPassword("Password@123");
        when(testUserRepository.existsByUsername(alreadyRegisteredUser.getUsername())).thenReturn(true);
        assertThrows(UserRegistrationException.class, () -> registerService.register(alreadyRegisteredUser));

    }
}
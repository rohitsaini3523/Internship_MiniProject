package com.example.Backend.services;

import com.example.Backend.entity.UserContactDetails;
import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserLogin;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserLoginValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
class LoginServiceTest {
    @Autowired
    UserRepository testUserRepository;
    LoginServiceInterface loginServiceInterface;
    UserRegisterDetails user;
    UserContactDetails userContactDetails;
    UserLogin expectedUserLogin;
    UserLoginValidator userLoginValidator;

    @BeforeEach
    void setUp() {
        userLoginValidator = new UserLoginValidator();
        loginServiceInterface = new LoginService(testUserRepository, userLoginValidator);
        userContactDetails = UserContactDetails.builder()
                .username("rs3523")
                .phoneNumber("9568000766")
                .build();
        user = UserRegisterDetails.builder()
                .username("rs3523")
                .password("Rohit@123")
                .email("rohit@gmail.com")
                .userContactDetailsSet(Set.of(userContactDetails))
                .build();
        testUserRepository.save(user);
        expectedUserLogin = UserLogin.builder().username("rs3523").password("Rohit@123").build();
    }

    @AfterEach
    void tearDown() {
        log.info("Delete all Data in Database");
        testUserRepository.deleteAll();
    }


    @Test
    void itShouldGetUserDetails() {
        String username = "rs3523";
        UserLogin result = loginServiceInterface.getUserDetails(username);
        assertNotNull(result);
        assertThat(result).isEqualTo(expectedUserLogin);
    }

    @Test
    void itShouldLogin() {
        String username = "rs3523";
        String password = "Rohit@123";
        String result = loginServiceInterface.login(UserLogin.builder().username(username).password(password).build());
        assertThat(result).isEqualTo("Found");
    }

    @Test
    void itShouldThrow_UserNotFoundException() {
        String username = "noUser";
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            loginServiceInterface.getUserDetails(username);
        });
        assertEquals("User doesn't exists", exception.getMessage());
    }

    @Test
    void itShouldThrow_InvalidInputExceptionForWrongPassword() {
        String username = "rs3523";
        String password = "wrongPassword";
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            loginServiceInterface.login(UserLogin.builder().username(username).password(password).build());
        });
        assertEquals("Wrong Password!", exception.getMessage());
    }

    @Test
    void itShouldThrow_InvalidInputExceptionForInvalidInput() {
        String username = "";
        String password = "password";
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            loginServiceInterface.login(UserLogin.builder().username(username).password(password).build());
        });
        assertEquals("Invalid Input details", exception.getMessage());
    }
}
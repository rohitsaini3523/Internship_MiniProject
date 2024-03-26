package com.example.Backend.services;

import com.example.Backend.entity.UserContactDetails;
import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserLogin;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserLoginValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LoginServiceImplTest {
    @Mock
    UserRepository testUserRepository;
    @InjectMocks
    LoginServiceImpl loginService;
    @InjectMocks
    UserLoginValidator userLoginValidator;
    UserRegisterDetails user;
    UserContactDetails userContactDetails;
    UserLogin expectedUserLogin;

    @BeforeEach
    void setUp() {
        loginService = new LoginServiceImpl(testUserRepository, userLoginValidator);
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
        when(testUserRepository.findByUsername(username)).thenReturn(user);
        UserLogin result = loginService.getUserDetails(username);
        assertNotNull(result);
        assertThat(result).isEqualTo(expectedUserLogin);
    }

    @Test
    void itShouldLogin() {
        UserLogin userLogin = new UserLogin();
        userLogin.setUsername("rs3523");
        userLogin.setPassword("Rohit@123");
        when(testUserRepository.findByUsername(userLogin.getUsername())).thenReturn(user);
        when(testUserRepository.findByUsernameAndPassword(userLogin)).thenReturn(user);
        String result = loginService.login(userLogin);
        assertNotNull(result);
        assertThat(result).isEqualTo("Found");
    }

    @Test
    void itShouldThrow_UserNotFoundException() {
        String username = "noUser";
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            loginService.getUserDetails(username);
        });
        assertEquals("User doesn't exists", exception.getMessage());
    }

    @Test
    void itShouldThrow_InvalidInputExceptionForWrongPassword() {
        String username = "rs3523";
        String password = "wrongPassword";
        when(testUserRepository.findByUsername(username)).thenReturn(user);
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            loginService.login(UserLogin.builder().username(username).password(password).build());
        });
        assertEquals("Wrong Password!", exception.getMessage());
    }

    @Test
    void itShouldThrow_InvalidInputExceptionForInvalidInput() {
        String username = "";
        String password = "password";
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            loginService.login(UserLogin.builder().username(username).password(password).build());
        });
        assertEquals("Invalid Input details", exception.getMessage());
    }
}
package com.example.Backend.services;

import com.example.Backend.entity.UserContactDetails;
import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.model.UserRegister;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserRegisterValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
class RegisterServiceTest {

    @Autowired
    UserRepository testUserRepository;
    RegisterServiceInterface registerServiceInterface;
    UserRegisterDetails user;
    UserContactDetails userContactDetails;
    UserRegister expectedUserRegister;
    UserRegisterValidator userRegisterValidator;

    @BeforeEach
    void setUp() {
        userRegisterValidator = new UserRegisterValidator();
        registerServiceInterface = new RegisterService(testUserRepository, userRegisterValidator);
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
        expectedUserRegister = UserRegister.builder().username("rs3523").password("Rohit@123").email("rohit@gmail.com").build();
    }

    @AfterEach
    void tearDown() {
        log.info("Delete all Data in Database");
        testUserRepository.deleteAll();
    }

    @Test
    void itShouldRegister() {
        String username = "rohit";
        String password = "Rohit@123";
        String email = "rohitsaini@gmail.com";
        String result = registerServiceInterface.register(UserRegister.builder().username(username).password(password).email(email).build());
        assertThat(result).isEqualTo("Registered");
    }

    @Test
    void itShouldThrow_InvalidInputExceptionForInvalidInput() {
        String username = "";
        String password = "password";
        String email = "email.com";
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            registerServiceInterface.register(UserRegister.builder().username(username).password(password).email(email).build());
        });
        assertEquals("Invalid Register details", exception.getMessage());
    }


//    @Test
//    void itShouldThrow_DataIntegrityViolationExceptionForRegisteredUser() {
//        String username = "rs3523";
//        String password = "Rohit@123";
//        String email = "rohits@gmail.com";
//        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
//            registerServiceInterface.register(UserRegister.builder().username(username).password(password).email(email).build());
//        });
//        Boolean isErrorOccured = exception.toString().contains("primary key violation");
//        log.info("Ans: "+ isErrorOccured);
//        assertEquals(true,isErrorOccured);
//    }
}
package com.example.Backend.services;

import com.example.Backend.entity.UserContactDetails;
import com.example.Backend.entity.UserRegisterDetails;
import com.example.Backend.exceptions.InvalidInputException;
import com.example.Backend.exceptions.UserNotFoundException;
import com.example.Backend.model.UserContact;
import com.example.Backend.model.UserLogin;
import com.example.Backend.respository.UserContactRepository;
import com.example.Backend.respository.UserRepository;
import com.example.Backend.validator.UserContactValidator;
import com.example.Backend.validator.UserLoginValidator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
class ContactServiceTest {

    @Autowired
    UserRepository testUserRepository;
    @Autowired
    UserContactRepository testContactRepository;
    ContactServiceInterface contactServiceInterface;
    UserRegisterDetails user;
    UserContactDetails userContactDetails;
    List<UserContact> expectedContactDetails;
    UserContactValidator userContactValidator;

    @BeforeEach
    void setUp() {
        expectedContactDetails = new ArrayList<>();
        userContactValidator = new UserContactValidator();
        contactServiceInterface = new ContactService(testContactRepository, testUserRepository, userContactValidator);
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
        expectedContactDetails.add(UserContact.builder().username("rs3523").phoneNumber("9568000766").build());
    }

    @AfterEach
    void tearDown() {
        log.info("Delete all Data in Database");
        testUserRepository.deleteAll();
    }

    @Test
    void itShouldGetContactDetails() {
        String username = "rs3523";
        List<UserContact> result = contactServiceInterface.getContactDetails(username);
        assertNotNull(result);
        assertThat(result).isEqualTo(expectedContactDetails);
    }

    @Test
    void itShouldAddContactDetails() {
        String username = "rs3523";
        String phoneNumber = "9999977777";
        String result = contactServiceInterface.addContactDetails(username, UserContact.builder().username(username).phoneNumber(phoneNumber).build());
        assertThat(result).isEqualTo("Added");
    }

    @Test
    void itShouldThrow_UserNotFoundExceptionForGettingContactDetails() {
        String username = "noUser";
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            contactServiceInterface.getContactDetails(username);
        });
        assertEquals("User Contacts don't exist for username: " + username, exception.getMessage());
    }

    @Test
    void itShouldThrow_InvalidInputExceptionForInvalidInput() {
        String username = "";
        String phoneNumber = "0989";
        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> {
            contactServiceInterface.addContactDetails(username, UserContact.builder().username(username).phoneNumber(phoneNumber).build());
        });
        assertEquals("Invalid Contact details", exception.getMessage());
    }

    @Test
    void itShouldThrow_UserNotFoundExceptionForAddingContactDetails() {
        String username = "noUser";
        String phoneNumber = "8888877777";
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            contactServiceInterface.addContactDetails(username, UserContact.builder().username(username).phoneNumber(phoneNumber).build());

        });
        assertEquals("User with " + username + " username doesn't exists", exception.getMessage());
    }
}
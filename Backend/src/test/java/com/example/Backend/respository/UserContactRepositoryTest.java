package com.example.Backend.respository;

import com.example.Backend.entity.UserContactDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserContactRepositoryTest {

    @Autowired
    private UserContactRepository testUserContactRepository;
    UserContactDetails expectedUserContactDetails;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        testUserContactRepository.deleteAll();
    }

    @Test
    void findByUsername() {
        String username = "Rohit";
        expectedUserContactDetails = UserContactDetails.builder()
                .username("Rohit")
                .phoneNumber("9368000766")
                .build();
        testUserContactRepository.save(expectedUserContactDetails);
        assertNotNull(expectedUserContactDetails);
        UserContactDetails result = testUserContactRepository.findByUsername(username);
        assertThat(result).isEqualTo(expectedUserContactDetails);
    }

    @Test
    void findAllByUsername() {
        String username = "rs3523";
        List<UserContactDetails> userContactDetailsList = new ArrayList<>();
        UserContactDetails userContactDetails1 = UserContactDetails.builder()
                .username("rs3523")
                .phoneNumber("9568000766")
                .build();
        testUserContactRepository.save(userContactDetails1);
        userContactDetailsList.add(userContactDetails1);
        UserContactDetails userContactDetails2 = UserContactDetails.builder()
                .username("rs3523")
                .phoneNumber("9268000766")
                .build();
        testUserContactRepository.save(userContactDetails2);
        userContactDetailsList.add(userContactDetails2);
        assertNotNull(userContactDetails1);
        assertNotNull(userContactDetails2);
        List<UserContactDetails> result = testUserContactRepository.findAllByUsername(username);
        assertThat(result).isEqualTo(userContactDetailsList);

    }
}